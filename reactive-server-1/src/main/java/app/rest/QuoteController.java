package app.rest;

import app.dao.QuoteMongoReactiveRepository;
import app.model.Quote;
import lombok.extern.log4j.Log4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.logging.Logger;

@RestController
public class QuoteController {

    private static final Logger log = Logger.getLogger("QuoteController");

    private static long DELAY_BY_TIME = 1000;

    private final ReactiveMongoTemplate reactiveMongoTemplate;

    private final QuoteMongoReactiveRepository quoteMongoReactiveRepository;

    public QuoteController(ReactiveMongoTemplate reactiveMongoTemplate, QuoteMongoReactiveRepository quoteMongoReactiveRepository) {
        this.reactiveMongoTemplate = reactiveMongoTemplate;
        this.quoteMongoReactiveRepository = quoteMongoReactiveRepository;
    }

    @GetMapping("/quotes-reactive")
    public Flux<Quote> getQuoteFlux() throws InterruptedException {
        log.info("Calling Normal Flux App");
        return reactiveMongoTemplate.findAll(Quote.class, "quotes").delayElements(Duration.ofMillis(300));
    }

    @GetMapping("/quotes-reactive-spring-data")
    public Flux<Quote> getQuoteFluxSpringData() throws InterruptedException {
        log.info("Calling Normal Flux App");
        quoteMongoReactiveRepository.count().subscribe(s->System.out.println(s.longValue()));
        return quoteMongoReactiveRepository.findAll().delayElements(Duration.ofMillis(100));
    }

    @GetMapping("/quotes-reactive-pageable")
    public Flux<Quote> getPageableQuotes(final @RequestParam("page") int page, final @RequestParam("size") int size) {
        log.info("Calling Pageable Flux App with page="+page +"size="+size);
        return quoteMongoReactiveRepository.findAllByIdNotNullOrderByIdAsc(PageRequest.of(page, size)).delayElements(Duration.ofMillis(DELAY_BY_TIME));

    }

}
