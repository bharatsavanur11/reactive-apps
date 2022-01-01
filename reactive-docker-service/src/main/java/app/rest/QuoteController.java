package app.rest;

import app.model.Quote;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;

@RestController
public class QuoteController{

    private static long DELAY_BY_TIME=1000;

    private final ReactiveMongoTemplate reactiveMongoTemplate;

    public QuoteController(ReactiveMongoTemplate reactiveMongoTemplate) {
        this.reactiveMongoTemplate = reactiveMongoTemplate;
    }

    @GetMapping("/quotes-reactive")
    public Flux<Quote> getQuoteFlux() throws InterruptedException {
        System.out.println("Inside Calling Flux");
        return reactiveMongoTemplate.findAll(Quote.class,"quotes").delayElements(Duration.ofMillis(300));
    }

}
