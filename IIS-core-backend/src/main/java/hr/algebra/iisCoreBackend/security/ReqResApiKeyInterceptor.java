package hr.algebra.iisCoreBackend.security;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.lang.NonNull;

import java.io.IOException;

public class ReqResApiKeyInterceptor implements ClientHttpRequestInterceptor {

    private final String apiKey;

    public ReqResApiKeyInterceptor(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    @NonNull
    public ClientHttpResponse intercept(
            @NonNull HttpRequest request,
            @NonNull byte[] body,
            @NonNull ClientHttpRequestExecution execution) throws IOException {

        request.getHeaders().add("x-api-key", apiKey);

        System.out.println("REST DEBUG: Sending request to: " + request.getURI());

        return execution.execute(request, body);
    }
}