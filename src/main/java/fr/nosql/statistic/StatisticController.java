package fr.nosql.statistic;

import fr.nosql.mongodb.MongoDBDao;
import fr.nosql.neo4j.Connector;
import fr.nosql.protein.ProteinData;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/statistic")
public class StatisticController {

    private final MongoDBDao mongoDBDao = MongoDBDao.getInstance();
    private final Connector connector = Connector.getInstance();

    @RequestMapping("/proteinCount")
    public long countProtein() {
        return mongoDBDao.countProteins();
    }

    @RequestMapping("/undescribedProteinCount")
    public List<ProteinData> countUndescribedProtein() {
        return mongoDBDao.getUndescribedProtein();
    }

    @RequestMapping("/globals")
    public List<StatisticData> getGlobalStatistic() {
        long proteinCount = mongoDBDao.countProteins();
        long undescribedProteinCount = mongoDBDao.getUndescribedProtein().size();
        String mostUsedInterPro = mongoDBDao.mostUsedInterProSequence();
        int countProteinsWithoutInterPro = mongoDBDao.countProteinsWithoutInterpro();
        return List.of(
                new StatisticData("Protein count", String.valueOf(proteinCount)),
                new StatisticData("Undescribed protein count", String.valueOf(undescribedProteinCount)),
                new StatisticData("Most used interPro sequence", mostUsedInterPro),
                new StatisticData("Protein without interPro", String.valueOf(countProteinsWithoutInterPro))
        );
    }

    @RequestMapping("/{entryFirst}/{entrySecond}")
    public List<StatisticData> getStatisticByComparaison(@PathVariable("entryFirst")String entryFirst, @PathVariable("entrySecond")String entrySecond) {
        String commonInterPro = mongoDBDao.getCommonInterPro(entryFirst, entrySecond);

        return List.of(
                new StatisticData("Common interPro", commonInterPro)
        );
    }


}
