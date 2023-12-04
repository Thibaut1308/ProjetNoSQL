package neo4j;

import Protein.ProteinData;
import Protein.ProteinDataBuilder;
import org.neo4j.driver.*;
import org.neo4j.driver.Record;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Connector {

    private static Connector instance;
    private final Driver driver;

    private Connector(String uri, String user, String password) {
        driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
    }

    public static Connector getInstance() {
        if (instance == null) {
            instance = new Connector(Neo4jIdentifiers.URI, Neo4jIdentifiers.USER, Neo4jIdentifiers.PASSWORD);
        }
        return instance;
    }

    public void close() {
        driver.close();
    }

    public void createLinks(String entry) {
        try (Session session = driver.session()) {
            session.run(
                    """
                    MATCH (source:Protein {entry: '$proteinName'})-[:HAS_DOMAIN]->(commonDomain)<-[:HAS_DOMAIN]-(neighbor:Protein)
                    MERGE (source)-[:SHARES_DOMAIN_WITH]->(neighbor)
                    WITH source, neighbor
                    MATCH (neighbor)-[:HAS_DOMAIN]->(commonDomainNeighbor)<-[:HAS_DOMAIN]-(neighborNeighbor:Protein)
                    MERGE (neighbor)-[:SHARES_DOMAIN_WITH]->(neighborNeighbor);
                    """,
                    Values.parameters("proteinName", entry)
            );
        }
    }

    public ProteinData getProteinFromEntry(String entry) {
        try (Session session = driver.session()) {
            return session.executeRead(tx -> {
                Result result = tx.run(
                        """
                        MATCH (protein:Protein {entry: $entry})
                        RETURN DISTINCT protein;
                        """,
                        Values.parameters("entry", entry)
                );
                ProteinDataBuilder proteinDataBuilder = new ProteinDataBuilder();
                if (result.hasNext()) {
                    Record record = result.next();
                    Value protein = record.get("protein");
                    proteinDataBuilder.setEntry(protein.get("entry").asString())
                            .setEntryName(protein.get("entryName").asString())
                            .setProteinNames(protein.get("proteinNames").asString())
                            .setInterPro(protein.get("interPro").asString())
                            .setSequence(protein.get("sequence").asString())
                            .setEcNumber(protein.get("ecNumber").asString())
                            .setGeneOntology(protein.get("geneOntology").asString());
                }
                return proteinDataBuilder.createProteinData();
            });
        }
    }

    public ProteinData getProteinFromName(String entryName) {
        try (Session session = driver.session()) {
            return session.executeRead(tx -> {
                Result result = tx.run(
                        """
                        MATCH (protein:Protein {entryName: $name})
                        RETURN DISTINCT protein;
                        """,
                        Values.parameters("name", entryName)
                );
                ProteinDataBuilder proteinDataBuilder = new ProteinDataBuilder();
                if (result.hasNext()) {
                    Record record = result.next();
                    Value protein = record.get("protein");
                    proteinDataBuilder.setEntry(protein.get("entry").asString())
                            .setEntryName(protein.get("entryName").asString())
                            .setProteinNames(protein.get("proteinNames").asString())
                            .setInterPro(protein.get("interPro").asString())
                            .setSequence(protein.get("sequence").asString())
                            .setEcNumber(protein.get("ecNumber").asString())
                            .setGeneOntology(protein.get("geneOntology").asString());
                }
                return proteinDataBuilder.createProteinData();
            });
        }
    }

    public ProteinData getProteinFromDescription(String proteinNames) {
        try (Session session = driver.session()) {
            return session.executeRead(tx -> {
                Result result = tx.run(
                        """
                        MATCH (protein:Protein {proteinNames: $proteinNames})
                        RETURN DISTINCT protein;
                        """,
                        Values.parameters("proteinNames", proteinNames)
                );
                ProteinDataBuilder proteinDataBuilder = new ProteinDataBuilder();
                if (result.hasNext()) {
                    Record record = result.next();
                    Value protein = record.get("protein");
                    proteinDataBuilder.setEntry(protein.get("entry").asString())
                            .setEntryName(protein.get("entryName").asString())
                            .setProteinNames(protein.get("proteinNames").asString())
                            .setInterPro(protein.get("interPro").asString())
                            .setSequence(protein.get("sequence").asString())
                            .setEcNumber(protein.get("ecNumber").asString())
                            .setGeneOntology(protein.get("geneOntology").asString());
                }
                return proteinDataBuilder.createProteinData();
            });
        }
    }

    public List<ProteinData> getNeighborsFromProtein(ProteinData protein) {
        this.createLinks(protein.getEntry()); // Create links between proteins if they share a domain
        try (Session session = driver.session()) {
            return session.executeRead(tx -> {
                Result result = tx.run(
                        """
                        MATCH (source:Protein {entry: $proteinName})-[:SHARES_DOMAIN_WITH]->(neighbor:Protein)
                        RETURN DISTINCT neighbor;
                        """,
                        Values.parameters("proteinName", protein.getEntry())
                );
                List<ProteinData> proteinDataList = new ArrayList<>();
                while (result.hasNext()) {
                    Record record = result.next();
                    Value neighbor = record.get("neighbor");
                    ProteinDataBuilder proteinDataBuilder = new ProteinDataBuilder();
                    proteinDataBuilder.setEntry(neighbor.get("entry").asString())
                            .setEntryName(neighbor.get("entryName").asString())
                            .setProteinNames(neighbor.get("proteinNames").asString())
                            .setInterPro(neighbor.get("interPro").asString())
                            .setSequence(neighbor.get("sequence").asString())
                            .setEcNumber(neighbor.get("ecNumber").asString())
                            .setGeneOntology(neighbor.get("geneOntology").asString());
                    proteinDataList.add(proteinDataBuilder.createProteinData());
                }
                return proteinDataList;
            });
        }
    }

    public Map<ProteinData, List<ProteinData>> getNeighborsAndNeigborsofNeighborsFromProtein(ProteinData protein) {
        this.createLinks(protein.getEntry()); // Create links between proteins if they share a domain
        Map<ProteinData, List<ProteinData>> neighborsAndNeighborsOfNeighbors = new HashMap<>();
        List<ProteinData> neighbors = this.getNeighborsFromProtein(protein);
        for (ProteinData neighbor : neighbors) {
            this.createLinks(neighbor.getEntry()); // Create links between proteins if they share a domain
            List<ProteinData> neighborsOfNeighbor = this.getNeighborsFromProtein(neighbor);
            neighborsAndNeighborsOfNeighbors.put(neighbor, neighborsOfNeighbor);
        }
        return neighborsAndNeighborsOfNeighbors;
    }
}
