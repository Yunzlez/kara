package be.zlz.kara.bin.services.webhook;

import be.zlz.kara.bin.domain.Reply;
import org.springframework.stereotype.Service;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Map;

@Service
public class WebhookService {

    private final HttpClient client;

    private static final String KARA_UA = "kara-webhook";

    public WebhookService() {
        client = HttpClient.newBuilder()
                .connectTimeout(Duration.of(15, ChronoUnit.SECONDS))
                .followRedirects(HttpClient.Redirect.NEVER)
                .build();
    }


    public Reply runWebhook(WebhookContext context) {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        for (Map.Entry<String, String> entry : context.getHeaders().entrySet()) {
            requestBuilder.header(entry.getKey(), entry.getValue());
        }
        requestBuilder.header("User-Agent", KARA_UA);

        switch (context.getMethod()) {
            case GET:
                requestBuilder.GET();
            case DELETE:
                requestBuilder.DELETE();
            default:
                requestBuilder.method(context.getMethod().name(), HttpRequest.BodyPublishers.ofByteArray(context.getData()));
        }

        return new Reply(
        );
    }
}
