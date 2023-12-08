package fr.lesbg.MongoDB;

import fr.lesbg.Protein.ProteinData;
import com.google.gson.Gson;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class MongoDBDao {

    private static volatile MongoDBDao instance;
    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection collection;
    private Gson gson = new Gson();
    public MongoDBDao() {
        setMongoClient(MongoClients.create("mongodb://localhost:27017/"));
        setDatabase(mongoClient.getDatabase("UniProt"));
        setCollection(database.getCollection("UniProt"));
        System.out.println("Opened fr.lesbg.MongoDB connection with database " + database.getName() + " with collection " + collection.getNamespace().getCollectionName());
    }

    public void closeConnection() {
        getMongoClient().close();
        System.out.println("fr.lesbg.MongoDB connection closed successfully.");
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

    public List<ProteinData> getProteinByEC(String ecNumber) {
        List<ProteinData> results = new ArrayList<>();
        Document query = new Document("EC number", ecNumber);
        collection.find(query).forEach(doc -> {
            Document temp = (Document) doc;
            results.add(gson.fromJson(temp.toJson(), ProteinData.class));
        });
        return results;
    }

    public ProteinData getProteinByEntryId(String id) {
        Document query = new Document("Entry", id);
        Document result = (Document) collection.find(query).first();

        return (result != null)?gson.fromJson(result.toJson(), ProteinData.class):null;
    }

    public ProteinData getProteinByEntryName(String entryName) {
        Document query = new Document("Entry Name", entryName);
        Document result = (Document) collection.find(query).first();

        return (result != null)?gson.fromJson(result.toJson(), ProteinData.class):null;
    }

    /***
     *
     * @param name : fragment of the complete name
     * @return List of proteins containing the fragment
     */
    public List<ProteinData> getProteinByName(String name) {
        List<ProteinData> results = new ArrayList<>();
        String regex = ".*"+name+".*";
        Document query = new Document("fr.lesbg.Protein names", new Document("$regex", regex));
        collection.find(query).forEach(doc -> {
            Document temp = (Document) doc;
            results.add(gson.fromJson(temp.toJson(), ProteinData.class));
        });
        return results;
    }

    public List<ProteinData> getProteinByInterPro(String interPro) {
        List<ProteinData> results = new ArrayList<>();
        String regex = ".*"+interPro+".*";
        Document query = new Document("InterPro", new Document("$regex", regex));
        collection.find(query).forEach(doc -> {
            Document temp = (Document) doc;
            results.add(gson.fromJson(temp.toJson(), ProteinData.class));
        });
        return results;
    }

    public List<ProteinData> getProteinBySequence(String sequence) {
        List<ProteinData> results = new ArrayList<>();
        String regex = ".*"+sequence+".*";
        Document query = new Document("Sequence", new Document("$regex", regex));
        collection.find(query).forEach(doc -> {
            Document temp = (Document) doc;
            results.add(gson.fromJson(temp.toJson(), ProteinData.class));
        });
        return results;
    }

    public List<ProteinData> getProteinByGO(String geneOntology) {
        List<ProteinData> results = new ArrayList<>();
        String regex = ".*"+geneOntology+".*";
        Document query = new Document("Gene Ontology (GO)", new Document("$regex", regex));
        collection.find(query).forEach(doc -> {
            Document temp = (Document) doc;
            results.add(gson.fromJson(temp.toJson(), ProteinData.class));
        });
        return results;
    }


}
