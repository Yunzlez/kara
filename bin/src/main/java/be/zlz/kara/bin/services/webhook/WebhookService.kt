package be.zlz.kara.bin.services.webhook

import be.zlz.kara.bin.domain.Reply
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import java.net.URI
import java.net.URISyntaxException
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.time.Duration
import java.time.temporal.ChronoUnit


@Service
class WebhookService {

    private val client: HttpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.of(15, ChronoUnit.SECONDS))
            .followRedirects(HttpClient.Redirect.NEVER)
            .build()

    companion object {
        private const val KARA_UA = "kara-webhook"
    }

    fun runWebhook(context: WebhookContext): Reply {
        val requestBuilder = HttpRequest.newBuilder()

        for ((key, value) in context.headers) {
            requestBuilder.header(key, value)
        }

        requestBuilder.header("User-Agent", KARA_UA)
        when (context.method) {
            HttpMethod.GET -> requestBuilder.GET()
            HttpMethod.DELETE -> requestBuilder.DELETE()
            else -> requestBuilder.method(context.method.name, HttpRequest.BodyPublishers.ofByteArray(context.data))
        }

        try {
            requestBuilder.uri(URI(context.destination))
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }

        return Reply()
    }


}
