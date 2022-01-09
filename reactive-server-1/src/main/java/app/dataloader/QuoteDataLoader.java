package app.dataloader;

import app.model.Quote;
import com.mongodb.reactivestreams.client.MongoCollection;
import org.bson.Document;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.function.Supplier;
import java.util.logging.Logger;

/**
 *
 * End to End Reactive Data Loader with various configurations which can have different implications
 * It is important to understand the implications of what to use when and how it can be accommodated
 * in the end to serve the business purpose.
 *
 */
@Component
public class QuoteDataLoader implements ApplicationRunner {

    private static final Logger log = Logger.getLogger("QuoteDataLoader");

    private final ReactiveMongoTemplate reactiveMongoTemplate;

    public QuoteDataLoader(ReactiveMongoTemplate reactiveMongoTemplate) {
        this.reactiveMongoTemplate = reactiveMongoTemplate;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        var idGenerator = getIdSupplier();

        var bufferedReader = new BufferedReader(
                new InputStreamReader(getClass()
                        .getClassLoader()
                        .getResourceAsStream("pg2000.txt")));
        reactiveMongoTemplate.dropCollection("quotes").block();

        CountDownLatch latch = new CountDownLatch(1);
        reactiveMongoTemplate.dropCollection("quotes");
        Mono<MongoCollection<Document>> collection = reactiveMongoTemplate.getCollection("quotes");
        System.out.println("Inside Data Loader");

     /*   var s = Flux.fromStream(bufferedReader.lines()
                                .filter(l -> !l.trim().isEmpty()))
                                .map((quote) -> {
                                    System.out.println(Thread.currentThread().getName());
                                    return reactiveMongoTemplate.insert(new Quote(idGenerator.get(), "Quote", quote), "quotes").subscribe(quote1->System.out.println(Thread.currentThread().getName()));
                            }).blockLast();*/

            Flux.fromStream(bufferedReader.lines().filter(l -> !l.trim().isEmpty()))
                                    .subscribe(quote->{
                                        System.out.println(Thread.currentThread().getName());
                                        // This is just synchronous execution wrapped in Flux.
                                        reactiveMongoTemplate.insert(new Quote(UUID.randomUUID().toString(), "Quote", quote), "quotes").block();// blocks the main thread
                                        // and hence all the lines below works
                                        // This line makes it reactive and parallel .subscribe(s->System.out.println(Thread.currentThread().getName()));
                                    });

            System.out.println("Here it is");
        //latch.await();
        // there is a chance that count documents mono gets executed even before the above to insert documents
        // gets executed. Since all of this is reactive and asynchronous, it becomes extremely difficult
        // to manage so many threads going around.
        // All reactive threads are daemon threads from thread pool and the spring threads are the non-daemon threads-- need to confirm though.
        // Now how to wait till all the save happens ?? do we need to chain methods.
        // So inorder to think in terms of reactive programming we need to
        // make sure that the
        CountDownLatch latch1 = new CountDownLatch(1);
        collection.subscribe(data -> data.countDocuments().subscribe(new Subscriber<Long>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                       s.request(1);
                    }

                    @Override
                    public void onNext(Long aLong) {
                        System.out.println("Count"+aLong);
                        latch1.countDown();

                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                }));
        latch1.await();
        System.out.println("Over");

    }

    private Supplier<String> getIdSupplier() {
        return new Supplier<String>() {
            @Override
            public String get() {
                return String.format("%05d", Calendar.getInstance().getTimeInMillis());
            }
        };
    }
}
