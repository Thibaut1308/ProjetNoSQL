package org.example;

import MongoDB.MongoDBDao;
import Protein.ProteinData;
import com.google.gson.Gson;
import org.bson.Document;


public class Main {
    public static void main(String[] args) {

        MongoDBDao mongoDatabase = MongoDBDao.getInstance();
        // Query all documents where field "EC number" equals "1.14.14.1
        Document query = new Document("EC number","1.14.14.1");
        // Get the first document
        Document result = (Document) mongoDatabase.getCollection().find(query).first();

        // Process the result
        if (result != null) {
            // Map JSON into ProteinData object
            Gson gson = new Gson();
            ProteinData data = gson.fromJson(result.toJson(),ProteinData.class);
            System.out.println("Found protein: " + data.getProteinName());
        } else {
            System.out.println("Document not found.");
        }
        // Close the MongoDB connection
        mongoDatabase.closeConnection();
    }
}