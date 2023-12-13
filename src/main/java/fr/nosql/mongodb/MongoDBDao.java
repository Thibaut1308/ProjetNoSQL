package fr.nosql.mongodb;

import com.google.gson.Gson;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import fr.nosql.protein.ProteinData;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mongodb.client.model.Aggregates.count;
import static com.mongodb.client.model.Aggregates.match;

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
        synchronized (MongoDBDao.class) {
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

        return (result != null) ? gson.fromJson(result.toJson(), ProteinData.class) : null;
    }

    public ProteinData getProteinByEntryName(String entryName) {
        Document query = new Document("Entry Name", entryName);
        Document result = (Document) collection.find(query).first();

        return (result != null) ? gson.fromJson(result.toJson(), ProteinData.class) : null;
    }

    /***
     *
     * @param name : fragment of the complete name
     * @return List of proteins containing the fragment
     */
    public List<ProteinData> getProteinByName(String name) {
        List<ProteinData> results = new ArrayList<>();
        String regex = ".*" + name + ".*";
        Document query = new Document("Protein names", new Document("$regex", regex));
        collection.find(query).forEach(doc -> {
            Document temp = (Document) doc;
            results.add(gson.fromJson(temp.toJson(), ProteinData.class));
        });
        return results;
    }

    public List<ProteinData> getProteinByInterPro(String interPro) {
        List<ProteinData> results = new ArrayList<>();
        String regex = ".*" + interPro + ".*";
        Document query = new Document("InterPro", new Document("$regex", regex));
        collection.find(query).forEach(doc -> {
            Document temp = (Document) doc;
            results.add(gson.fromJson(temp.toJson(), ProteinData.class));
        });
        return results;
    }

    public List<ProteinData> getProteinBySequence(String sequence) {
        List<ProteinData> results = new ArrayList<>();
        String regex = ".*" + sequence + ".*";
        Document query = new Document("Sequence", new Document("$regex", regex));
        collection.find(query).forEach(doc -> {
            Document temp = (Document) doc;
            results.add(gson.fromJson(temp.toJson(), ProteinData.class));
        });
        return results;
    }

    public List<ProteinData> getProteinByGO(String geneOntology) {
        List<ProteinData> results = new ArrayList<>();
        String regex = ".*" + geneOntology + ".*";
        Document query = new Document("Gene Ontology (GO)", new Document("$regex", regex));
        collection.find(query).forEach(doc -> {
            Document temp = (Document) doc;
            results.add(gson.fromJson(temp.toJson(), ProteinData.class));
        });
        return results;
    }

    public long countProteins() {
        collection.aggregate(Arrays.asList(
                match(Filters.eq("Entry", "A4D161")),
                count())).first();

        return collection.countDocuments();
    }

    public List<ProteinData> getUndescribedProtein() {
        List<ProteinData> results = new ArrayList<>();
        collection.aggregate(Arrays.asList(
                match(Filters.and(Filters.eq("EC Number", null), Filters.eq("Gene Ontology (GO)", null))))).forEach(doc -> {
            Document temp = (Document) doc;
            results.add(gson.fromJson(temp.toJson(), ProteinData.class));
        });
        return results;
    }

    public String mostUsedInterProSequence() {
        Document result = (Document) collection.aggregate(Arrays.asList(
                        Aggregates.match(Filters.ne("InterPro", null)),  // Filtrer les documents avec "InterPro" non nul
                        Aggregates.group("$InterPro", Accumulators.sum("count", 1)),
                        Aggregates.sort(Sorts.descending("count")),
                        Aggregates.limit(1)))
                .first();

        if (result != null) {
            return result.getString("_id");
        } else {
            return null;
        }
    }


    public int countProteinsWithoutInterpro() {
        Document result = (Document) collection.aggregate(Arrays.asList(
                        Aggregates.match(Filters.eq("InterPro", null)),  // Filtrer les documents avec "InterPro" non nul
                        Aggregates.group("$InterPro", Accumulators.sum("count", 1))))
                .first();

        if (result != null) {
            System.out.println("Result Document: " + result.toJson());
            return result.getInteger("count");
        } else {
            return 0;
        }
    }

    public String getCommonInterPro(String entryFirst, String entrySecond) {
        ProteinData firstProt = this.getProteinByEntryId(entryFirst);
        ProteinData secondProt = this.getProteinByEntryId(entrySecond);

        System.out.println(firstProt);
        if (firstProt != null && secondProt != null) {
            if (firstProt.getInterPro() == null || secondProt.getInterPro() == null) {
                return null;
            }
            List<String> firstInterPro = Arrays.asList(firstProt.getInterPro().split(";"));
            List<String> secondInterPro = Arrays.asList(secondProt.getInterPro().split(";"));
            firstInterPro.retainAll(secondInterPro);
            StringBuilder result = new StringBuilder();
            for (String interPro : firstInterPro) {
                result.append(interPro).append(";");
            }
            if (!result.isEmpty()) {
                result.deleteCharAt(result.length() - 1);
            }
            return result.toString();
        } else {
            return null;
        }
    }
}
