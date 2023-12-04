package org.example;

import MongoDB.MongoDBDao;
import Protein.ProteinData;
import com.google.gson.Gson;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;


public class Main {

    public static void main(String[] args) {
        MongoDBDao mongoDBDao = MongoDBDao.getInstance();
        // Testing queries
        /*
        List<ProteinData> query = mongoDBDao.getProteinByEC("1.14.14.1");
        List<ProteinData> query2 = mongoDBDao.getProteinByName("transport");
        List<ProteinData> query3 = mongoDBDao.getProteinByInterPro("IPR011009");
        List<ProteinData> query4 = mongoDBDao.getProteinByGO("centrosome");
        ProteinData query5 = mongoDBDao.getProteinByEntryId("A0A087X1C5");
        ProteinData query6 = mongoDBDao.getProteinByEntryName("CP2D7_HUMAN");
        List<ProteinData> query7 = mongoDBDao.getProteinBySequence("AVVKLARHR");
        mongoDBDao.closeConnection();
         */
    }


}