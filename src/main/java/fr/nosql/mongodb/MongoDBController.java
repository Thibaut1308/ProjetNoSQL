package fr.nosql.mongodb;

import fr.nosql.protein.ProteinData;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/mongodb")
public class MongoDBController {

    private final MongoDBDao mongoDBDao = MongoDBDao.getInstance();

    @RequestMapping("/entry/{entryId}")
    public ProteinData getProteinFromEntryId(@PathVariable("entryId")String entryId) {
        return mongoDBDao.getProteinByEntryId(entryId);
    }

    @RequestMapping("/entryName/{entryName}")
    public ProteinData getProteinFromEntryName(@PathVariable("entryName") String entryName) {
        return mongoDBDao.getProteinByEntryName(entryName);
    }

    @RequestMapping("/description/{description}")
    public List<ProteinData> getProteinsFromDescription(@PathVariable("description") String description) {
        return mongoDBDao.getProteinByEC(description);
    }

    @RequestMapping("/go/{goDesc}")
    public List<ProteinData> getProteinsFromGo(@PathVariable("goDesc") String go) {
        return mongoDBDao.getProteinByGO(go);
    }

    @GetMapping("/sequence/{sequence}")
    public List<ProteinData> getProteinFromSequence(@PathVariable("sequence") String sequence) {
        return mongoDBDao.getProteinBySequence(sequence);
    }

    @GetMapping("/interpro/{interpro}")
    public List<ProteinData> getProteinFromInterpro(@PathVariable("interpro") String interpro) {
        return mongoDBDao.getProteinByInterPro(interpro);
    }

    @GetMapping("/proteinNames/{proteinNames}")
    public List<ProteinData> getProteinByNames(@PathVariable("proteinNames") String proteinNames) {
        return mongoDBDao.getProteinByName(proteinNames);
    }
}
