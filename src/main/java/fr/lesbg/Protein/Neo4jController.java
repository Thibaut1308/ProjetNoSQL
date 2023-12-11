package fr.lesbg.Protein;

import fr.lesbg.neo4j.Connector;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/neo4j")
public class Neo4jController {

    private final Connector connector = Connector.getInstance();

    @GetMapping("/entry/{entry}")
    public ProteinData getProteinFromEntry(@PathVariable("entry") String entry) {
        return connector.getProteinFromEntry(entry);
    }

    @GetMapping("/entryName/{entryName}")
    public ProteinData getProteinFromEntryName(@PathVariable String entryName) {
        return connector.getProteinFromName(entryName);
    }

    @GetMapping("/description/{description}")
    public ProteinData getProteinFromDescription(@PathVariable String description) {
        return connector.getProteinFromDescription(description);
    }

    @GetMapping("/neighbours/{entry}")
    public List<ProteinData> getProteinNeighbours(@PathVariable String entry) {
        ProteinData proteinData = connector.getProteinFromEntry(entry);
        return connector.getNeighborsFromProtein(proteinData);
    }

    @GetMapping("/neighboursAndNeighboursOfNeighbours/{entry}")
    public List<ProteinLinks> getProteinNeighboursAndNeighboursOfNeighbours(@PathVariable("entry") String entry) {
        ProteinData proteinData = connector.getProteinFromEntry(entry);
        return connector.getNeighborsAndNeigborsofNeighborsFromProtein(proteinData);
    }
}
