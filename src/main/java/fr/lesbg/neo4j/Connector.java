package fr.lesbg.neo4j;

import fr.lesbg.Protein.ProteinData;
import fr.lesbg.Protein.ProteinDataBuilder;
import fr.lesbg.Protein.ProteinLinks;
import fr.lesbg.Protein.ProteinLinksBuilder;
import org.neo4j.driver.*;
import org.neo4j.driver.Record;

import java.util.ArrayList;
import java.util.List;

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

    public List<ProteinLinks> createLinksReturnProteinNeighborsAndNeighborsOfNeighbors(String entry) {
        try (Session session = driver.session()) {
            return session.executeWrite(tx -> {
                Result result = tx.run(
                    """
                        MATCH (source:Protein {entry: $proteinName})-[:HAS_DOMAIN]->(d)
                        WITH source, COLLECT(id(d)) AS sourceDomains
                        MATCH (source)-[:HAS_DOMAIN]->(commonDomain)<-[:HAS_DOMAIN]-(neighbor:Protein)
                        WHERE NOT source = neighbor
                        WITH source, sourceDomains, neighbor, COLLECT(id(commonDomain)) AS commonDomains
                        MATCH (neighbor)-[:HAS_DOMAIN]->(d)
                        WITH source, sourceDomains, neighbor, commonDomains, COLLECT(id(d)) AS neighborDomains
                        MERGE (source)-[r:SHARES_DOMAIN_WITH]->(neighbor)
                        WITH source, neighbor, r, SIZE(apoc.coll.intersection(sourceDomains, neighborDomains)) * 1.0 / SIZE(apoc.coll.union(sourceDomains, neighborDomains)) AS jaccard
                        WHERE jaccard >= 0.5
                        SET r.jaccard = jaccard
                        
                        WITH r, neighbor, source
                        MATCH (neighbor)-[:HAS_DOMAIN]->(d2)
                        WITH r, neighbor, source, COLLECT(id(d2)) AS sourceDomains2
                        MATCH (neighbor)-[:HAS_DOMAIN]->(commonDomain2)<-[:HAS_DOMAIN]-(neighborNeighbor:Protein)
                        WHERE NOT neighbor = neighborNeighbor AND NOT source = neighborNeighbor
                        WITH r, source, neighbor, sourceDomains2, neighborNeighbor, COLLECT(id(commonDomain2)) AS commonDomains2
                        MATCH (neighborNeighbor)-[:HAS_DOMAIN]->(d2)
                        WITH r, source, neighbor, sourceDomains2, neighborNeighbor, commonDomains2, COLLECT(id(d2)) AS neighborDomains2
                        MERGE (neighbor)-[r2:SHARES_DOMAIN_WITH]->(neighborNeighbor)
                        WITH source, neighbor, neighborNeighbor,r, r2, SIZE(apoc.coll.intersection(sourceDomains2, neighborDomains2)) * 1.0 / SIZE(apoc.coll.union(sourceDomains2, neighborDomains2)) AS jaccard2
                        WHERE jaccard2 >= 0.5
                        WITH source, neighbor, neighborNeighbor, r, r2, jaccard2
                        SET r2.jaccard2 = jaccard2
                        WITH source, neighbor, neighborNeighbor, r, r2
                        
                        RETURN source, neighbor, neighborNeighbor, r, r2;
                            """,
                    Values.parameters("proteinName", entry)
            );
                List<ProteinLinks> proteinLinks = new ArrayList<>();
                while (result.hasNext()) {
                    Record record = result.next();
                    ProteinDataBuilder proteinDataBuilder = new ProteinDataBuilder();
                    Value source = record.get("source");
                    Value neighbor = record.get("neighbor");
                    Value neighborNeighbor = record.get("neighborNeighbor");

                    ProteinData sourceProtein = buildProteinData(proteinDataBuilder, source);

                    ProteinData neighborProtein = buildProteinData(proteinDataBuilder, neighbor);

                    ProteinData neighborNeighborProtein =  buildProteinData(proteinDataBuilder, neighborNeighbor);

                    ProteinLinksBuilder proteinLinksBuilder = new ProteinLinksBuilder();
                    proteinLinksBuilder.setSource(sourceProtein)
                            .setNeighbor(neighborProtein)
                            .setNeighborOfNeighbor(neighborNeighborProtein);
                    proteinLinks.add(proteinLinksBuilder.createProteinLinks());
                }
                return proteinLinks;
            });
        }
    }

    private ProteinData buildProteinData(ProteinDataBuilder proteinDataBuilder, Value source) {
        proteinDataBuilder.setEntry(source.get("entry").asString())
                .setEntryName(source.get("entryName").asString())
                .setProteinNames(source.get("proteinNames").asString())
                .setInterPro(source.get("interPro").asString())
                .setSequence(source.get("sequence").asString())
                .setEcNumber(source.get("ecNumber").asString())
                .setGeneOntology(source.get("geneOntology").asString());
        return proteinDataBuilder.createProteinData();
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
        this.createLinksReturnProteinNeighborsAndNeighborsOfNeighbors(protein.getEntry()); // Create links between proteins if they share a domain
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

    public void deleteRedundantLinks() {
        try (Session session = driver.session()) {
            session.executeWrite(tx -> {
                tx.run(
                        """
                            MATCH (a:Protein)-[r:SHARES_DOMAIN_WITH]->(b:Protein)
                            WHERE id(a) < id(b) // Pour éviter de traiter deux fois la même paire
                            WITH a, b, COLLECT(r) AS rels
                            FOREACH (rel IN rels | DELETE rel);
                        """
                );
                return null;
            });
        }
    }

    public List<ProteinLinks> getNeighborsAndNeigborsofNeighborsFromProtein(ProteinData protein) {
        List<ProteinLinks> pl = this.createLinksReturnProteinNeighborsAndNeighborsOfNeighbors(protein.getEntry()); // Create links between proteins if they share a domain
        this.deleteRedundantLinks();
        return pl;
    }
}
