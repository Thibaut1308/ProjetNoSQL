package org.example;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;


public class Main {
    public static void main(String[] args) {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017/");
        MongoDatabase database = mongoClient.getDatabase("UniProt");
        MongoCollection<Document> collection =  database.getCollection("UniProt");

        Document query = new Document("EC number","1.14.14.1");

        // Get the first document
        Document result = collection.find(query).first();
        // Process the result
        if (result != null) {
            System.out.println("Found document: " + result.toJson());
        } else {
            System.out.println("Document not found.");
        }

        // Close the MongoDB connection
        mongoClient.close();
    }
}