package nl.hu.sie.bep.friendspammer;

import org.bson.Document;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class MongoSaver {

    private MongoSaver() {}

    private static Logger logger = LoggerFactory.getLogger(MongoSaver.class);

    static boolean saveEmail(String to, String from, String subject, String text, Boolean html) {
        String database = "friendspammer";


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
            logger.error("XXXXXXXXXXXXXXXXX ERROR WHILE SAVING TO MONGO XXXXXXXXXXXXXXXXXXXXXXXXXX");
            logger.error("Stacktrace: ", mongoException);
            success = false;
        }

        return success;

    }

}
