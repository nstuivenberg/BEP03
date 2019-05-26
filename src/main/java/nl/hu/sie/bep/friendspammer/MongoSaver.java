package nl.hu.sie.bep.friendspammer;

import org.bson.Document;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoCredential;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoSaver {

    public static boolean saveEmail(String to, String from, String subject, String text, Boolean html) {
        String userName = "spammer";
        String password = "hamspam";
        String database = "friendspammer";

        MongoCredential credential = MongoCredential.createCredential(userName, database, password.toCharArray());

        boolean success = true;

        MongoClientURI uri = new MongoClientURI("mongodb+srv://nynke:nynke@sjaak-vtp1c.mongodb.net/test?retryWrites=true");

        try (MongoClient mongoClient = new MongoClient(uri)) {

            MongoDatabase db = mongoClient.getDatabase( database );

            MongoCollection<Document> c = db.getCollection("email");

            Document  doc = new Document ("to", to)
                    .append("from", from)
                    .append("subject", subject)
                    .append("text", text)
                    .append("asHtml", html);
            c.insertOne(doc);
        } catch (MongoException mongoException) {
            System.out.println("XXXXXXXXXXXXXXXXXX ERROR WHILE SAVING TO MONGO XXXXXXXXXXXXXXXXXXXXXXXXXX");
            mongoException.printStackTrace();
            success = false;
        }

        return success;

    }

}
