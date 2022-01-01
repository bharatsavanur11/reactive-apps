package cm.reactive;


import com.mongodb.client.result.InsertOneResult;
import org.bson.Document;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;

import java.util.concurrent.CountDownLatch;

@SpringBootApplication
public class ReactiveApplication {

	public static void main(String[] args) throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(3);
		MongoClient client = MongoClients.create("mongodb://127.0.0.1:27017/");
		MongoDatabase database = client.getDatabase("test");
		MongoCollection<Document> toys = database.getCollection("toys");
		Document toy1 = new Document("testReactive1", "testReactive2");
		Document toy2 = new Document("testReactive2", "testReactive2");
		System.out.println("Inside Reactive App");
		toys.insertOne(toy1).subscribe(new Subscriber<InsertOneResult>() {
			@Override
			public void onSubscribe(Subscription s) {
				System.out.println("OnSubscribe"+ Thread.currentThread().getName());
				s.request(1);
			}
			@Override
			public void onNext(InsertOneResult insertOneResult) {
				System.out.println("Insert Thread"+ Thread.currentThread().getName());
				System.out.println("Inserted-->1"+insertOneResult.getInsertedId());
			}

			@Override
			public void onError(Throwable t) {

			}

			@Override
			public void onComplete() {
				System.out.println("Complete Thread"+ Thread.currentThread().getName());
				latch.countDown();
			}
		});
		toys.insertOne(toy2).subscribe(new Subscriber<InsertOneResult>() {
			@Override
			public void onSubscribe(Subscription s) {
				s.request(1);
			}

			@Override
			public void onNext(InsertOneResult insertOneResult) {
				System.out.println("Inserted-->2"+insertOneResult.getInsertedId());

			}

			@Override
			public void onError(Throwable t) {
				System.out.println("Total Count"+t);
			}

			@Override
			public void onComplete() {
				System.out.println("Completed");
				latch.countDown();
			}
		});
		toys.countDocuments().subscribe(new Subscriber<Long>() {
			@Override
			public void onSubscribe(Subscription s) {
				s.request(1);
			}

			@Override
			public void onNext(Long aLong) {
				System.out.println("Completed=="+aLong);

			}

			@Override
			public void onError(Throwable t) {
				System.out.println("Error");
			}

			@Override
			public void onComplete() {
				System.out.println("Complete");
				latch.countDown();
			}
		});
		latch.await();
	}
}
