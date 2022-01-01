package cm.reactive;

import com.mongodb.client.*;

import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

public class NonReactive {

    public static void main(String[] args) {
        MongoClient client = MongoClients.create("mongodb://127.0.0.1:27017/");
        MongoDatabase database = client.getDatabase("test_db");
        MongoCollection<Document> toys = database.getCollection("quotes");

       // database.listCollections().forEach(action->System.out.println(action.toString()));
       // MongoTemplate template = (MongoTemplate) MongoClients.create("mongodb://127.0.0.1:27017/");
        FindIterable iter = toys.find();
        MongoCursor cur = iter.cursor();
        System.out.println(toys.countDocuments());
        while(cur.hasNext()){
            Document data = (Document)cur.next();
            System.out.println(data.toString());
        }
    }
}
