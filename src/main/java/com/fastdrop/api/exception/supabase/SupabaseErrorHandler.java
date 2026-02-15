package com.fastdrop.api.exception.supabase;

import com.fastdrop.api.exception.SupabaseException;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

import java.util.function.Function;

public final class SupabaseErrorHandler {

    private SupabaseErrorHandler() {
    }

    public static Function<ClientResponse, Mono<? extends Throwable>> error(String message) {
        return response ->
                response.bodyToMono(String.class)
                        .defaultIfEmpty("")
                        .map(body -> new SupabaseException(message + " | status=" + response.statusCode() + " | body=" + body));
    }
}
