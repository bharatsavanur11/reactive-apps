package cm.reactive;

import app.model.Quote;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static org.springframework.http.MediaType.APPLICATION_JSON;

public class ReactiveClient {

    public static void main(String[] args) throws InterruptedException {

        WebClient client = WebClient.builder()
                .baseUrl("http://localhost:8080")
                .defaultCookie("cookieKey", "cookieValue")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

       Flux<Quote> quoteFluxes = client.get()
                .uri("/quotes-reactive").accept(APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(Quote.class);

       quoteFluxes.subscribe(System.out::println);
      Mono.when(quoteFluxes).block();
      System.out.println(quoteFluxes);
    }
}
