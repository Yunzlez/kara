package be.zlz.kara.bin.modules.webhook

import be.zlz.kara.bin.config.logger
import be.zlz.kara.bin.domain.Event
import be.zlz.kara.bin.domain.Response
import be.zlz.kara.bin.domain.enums.Interpretation
import be.zlz.kara.bin.dto.ErrorDTO
import be.zlz.kara.bin.dto.v11.ResponseOrigin
import be.zlz.kara.bin.modules.KaraModule
import be.zlz.kara.bin.util.KARA_UA
import be.zlz.kara.bin.util.KARA_VERSION_STRING
import com.fasterxml.jackson.module.kotlin.readValue
import com.google.common.cache.Cache
import com.google.common.util.concurrent.RateLimiter
import org.apache.logging.log4j.util.Strings
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import java.io.IOException
import java.net.URI
import java.net.URISyntaxException
import java.net.http.HttpClient
import java.net.http.HttpHeaders
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration
import java.time.temporal.ChronoUnit
import java.util.concurrent.TimeUnit
import kotlin.collections.HashMap


@Service
@Suppress("UnstableApiUsage")
class Webhook(private val ratelimiterCache: Cache<Long, RateLimiter>): KaraModule(WEBHOOK_NAME) {

    private val log by logger()

    private val client: HttpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.of(15, ChronoUnit.SECONDS))
            .followRedirects(HttpClient.Redirect.NEVER)
            .build()

    companion object {
        val WEBHOOK_NAME = "webhook"
        private val HEADER_WHITELIST = hashSetOf(
                "authorization",
                "accept",
                "accept-charset",
                "accept-encoding",
                "accept-language",
                "last-modified",
                "etag",
                "if-match",
                "if-none-match",
                "if-modified-since",
                "if-unmodified-since",
                "vary"
        )
    }

    override fun handleEventSync(config: String, event: Event): Response? {
        return execute(config) //todo use Event
    }

    override fun handleEventAsync(config: String, event: Event) {
        execute(config)
    }

    //todo max response size (limiting stream)
    //todo timeouts
    //todo set via header for webhooks that proxy original call
    //todo limit header size
    //todo handle failures: Transparent => reply with failure, otherwise ignore & store last failure
    //todo suspend bins that fail too much, unsuspend after x time
    private fun execute(config: String): Response? {
        val context = om.readValue<WebhookSettings>(config)

        var rateLimiter = ratelimiterCache.getIfPresent(context.binId)
        if (rateLimiter == null) {
            log.debug("Creating new rate limiter for {}", context.binId)
            rateLimiter = RateLimiter.create(2.0) //todo from config (using factory)
            ratelimiterCache.put(context.binId, rateLimiter!!)
        }
        if (!rateLimiter.tryAcquire(250, TimeUnit.MILLISECONDS)) {
            return null
        }

        val requestBuilder = HttpRequest.newBuilder()

        for ((key, value) in context.headers) {
            if (HEADER_WHITELIST.contains(key.toLowerCase())) {
                log.debug("Addding header: {}: {}", key, value)
                requestBuilder.header(key, value)
            }
        }

        requestBuilder.header("User-Agent", KARA_UA)
        if (context.mode == WebhookMode.PROXY) {
            requestBuilder.header("Via", KARA_VERSION_STRING)
        }
        when (context.method) {
            HttpMethod.GET -> requestBuilder.GET()
            HttpMethod.DELETE -> requestBuilder.DELETE()
            else -> requestBuilder.method(context.method.name, HttpRequest.BodyPublishers.ofByteArray(context.data))
        }

        try {
            requestBuilder.uri(URI(context.destination))
            requestBuilder.timeout(Duration.of(3, ChronoUnit.SECONDS))
            val response = client.send(requestBuilder.build(), HttpResponse.BodyHandlers.ofByteArray())
            return Response(
                    HttpStatus.valueOf(response.statusCode()),
                    response.headers().firstValue("content-type").orElse(""),
                    response.body(),
                    convertHeaders(response.headers()),
                    Interpretation.TEXT,
                    ResponseOrigin.WEBHOOK
            )
        } catch (e: URISyntaxException) {
            log.debug("Failed to parse webhook URI: ", e)
            return null
        } catch (e: IOException) {
            log.debug("Failed to make HTTP call to webhook target", e)
            if (context.mode == WebhookMode.PROXY) {
                return Response(
                        HttpStatus.BAD_GATEWAY,
                        MediaType.APPLICATION_JSON_VALUE,
                        om.writeValueAsBytes(ErrorDTO(HttpStatus.BAD_GATEWAY.value().toString(), "I/O error while contacting backend")),
                        emptyMap(), //todo headers?
                        Interpretation.TEXT,
                        ResponseOrigin.WEBHOOK
                )
            }
            return null
        }
    }

    private fun convertHeaders(headers: HttpHeaders): Map<String, String> {
        val res = HashMap<String, String>()
        for (entry in headers.map().entries) {
            res[entry.key] = Strings.join(entry.value, ',')
        }
        return res
    }
}
