package rs.raf.kids.scanner;

import rs.raf.kids.core.Property;
import rs.raf.kids.job.Job;
import rs.raf.kids.result.ResultRetriever;
import rs.raf.kids.scraper.Scraper;
import rs.raf.kids.util.WordCounter;
import java.util.HashMap;
import java.util.Map;

abstract class AbstractScanner implements PathScanner {

    private ResultRetriever resultRetriever;
    private Scraper scraper;
    private WordCounter counter;
    private String[] keywords;

    protected AbstractScanner(ResultRetriever resultRetriever) {
        this.resultRetriever = resultRetriever;

        scraper = getScraper();
        counter = new WordCounter();
        keywords = Property.KEYWORDS.get().split(",");
    }

    @Override
    public void scanJobPath(Job job) {
        String path = job.getPath();
        String content = scraper.getContent(path);

        Map<String, Integer> counts = new HashMap<>();

        for(String keyword : keywords) {
            int count = counter.count(content, keyword);
            counts.put(keyword, count);
        }

        publishResult(job, counts);
    }

    @Override
    public void publishResult(Job job, Map<String, Integer> result) {
        resultRetriever.addResult(job, result);
    }

    protected abstract Scraper getScraper();
}
