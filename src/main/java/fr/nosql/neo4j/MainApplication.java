package fr.nosql.neo4j;

import fr.nosql.protein.ProteinData;
import fr.nosql.protein.ProteinLinks;

import java.util.List;

public class MainApplication {

    public static void main(String[] args) {
        Connector connectorNeo4j = Connector.getInstance();
        ProteinData proteinData = connectorNeo4j.getProteinFromEntry("A0A087X1C5");

        List<ProteinData> proteinDataList = connectorNeo4j.getNeighborsFromProtein(proteinData);

        List<ProteinLinks> proteinDataList2 = connectorNeo4j.getNeighborsAndNeigborsofNeighborsFromProtein(proteinData);
        for (ProteinLinks proteinLinks : proteinDataList2) {
            System.out.println(proteinLinks);
        }
        System.out.println(proteinDataList2.size());

        connectorNeo4j.close();
    }
}
