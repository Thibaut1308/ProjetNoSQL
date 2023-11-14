package MongoDB;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MongoDBDao {

    private static volatile MongoDBDao instance;
    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection collection;
    public MongoDBDao() {
        setMongoClient(MongoClients.create("mongodb://localhost:27017/"));
        setDatabase(mongoClient.getDatabase("UniProt"));
        setCollection(database.getCollection("UniProt"));
        System.out.println("Opened MongoDB connection with database " + database.getName() + " with collection " + collection.getNamespace().getCollectionName());
    }

    public void closeConnection() {
        getMongoClient().close();
        System.out.println("MongoDB connection closed successfully.");
    }

    public static MongoDBDao getInstance() {
        MongoDBDao result = instance;
        if (result != null) {
            return result;
        }
        synchronized(MongoDBDao.class) {
            if (instance == null) {
                instance = new MongoDBDao();
            }
            return instance;
        }
    }

}
