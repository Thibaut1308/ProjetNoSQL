package neo4j;

import Protein.ProteinData;
import org.neo4j.driver.*;

import java.util.List;
import java.util.Map;

public class MainApplication {

    public static void main(String[] args) {
        Connector connectorNeo4j = Connector.getInstance();
        ProteinData proteinData = connectorNeo4j.getProteinFromEntry("A0A087X1C5");
        System.out.println(proteinData);

        List<ProteinData> proteinDataList = connectorNeo4j.getNeighborsFromProtein(proteinData);
        System.out.println(proteinDataList);

        Map<ProteinData, List<ProteinData>> proteinDataMap = connectorNeo4j.getNeighborsAndNeigborsofNeighborsFromProtein(proteinData);
        System.out.println(proteinDataMap);
        connectorNeo4j.close();
    }
}
