package be.zlz.kara.bin.services.webhook

import be.zlz.kara.bin.config.logger
import be.zlz.kara.bin.domain.Reply
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
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.HashMap
import kotlin.collections.HashSet


@Service
@Suppress("UnstableApiUsage")
class WebhookService(private val ratelimiterCache: Cache<Long, RateLimiter>) {

    private val log by logger()

    private val client: HttpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.of(15, ChronoUnit.SECONDS))
            .followRedirects(HttpClient.Redirect.NEVER)
            .build()

    companion object {
        private const val KARA_UA = "kara-webhook"
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

    //todo max response size (limiting stream)
    //todo timeouts
    //todo via for webhooks that proxy original call
    //todo limit header size
    //todo handle failures: Transparent => reply with failure, otherwise ignore & store last failure
    fun runWebhook(context: WebhookContext): Optional<Reply> {
        var rateLimiter = ratelimiterCache.getIfPresent(context.binId)
        if (rateLimiter == null) {
            rateLimiter = RateLimiter.create(2.0) //todo from config (using factory)
            ratelimiterCache.put(context.binId, rateLimiter!!)
        }
        if (!rateLimiter.tryAcquire(250, TimeUnit.MILLISECONDS)) {
            return Optional.empty()
        }

        val requestBuilder = HttpRequest.newBuilder()

        for ((key, value) in context.headers) {
            if (HEADER_WHITELIST.contains(key.toLowerCase())) {
                requestBuilder.header(key, value)
            }
        }

        requestBuilder.header("User-Agent", KARA_UA)
        if (context.isTransparent) {
            requestBuilder.header("Via", KARA_UA)
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
            return Optional.of(Reply(
                    HttpStatus.valueOf(response.statusCode()),
                    response.headers().firstValue("content-type").orElse(""),
                    String(response.body()),
                    emptyMap(),
                    convertHeaders(response.headers()),
                    true
            ))
        } catch (e: URISyntaxException) {
            log.debug("Failed to parse webhook URI: ", e)
            return Optional.empty()
        } catch (e: IOException) {
            log.debug("Failed to make HTTP call to webhook target", e)
            if (context.isTransparent) {
                return Optional.of(Reply(
                        HttpStatus.BAD_GATEWAY,
                        MediaType.APPLICATION_JSON_VALUE,
                        "",
                        emptyMap(),
                        emptyMap(), //todo
                        true
                ))
            }
            return Optional.empty()
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
