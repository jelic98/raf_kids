package rs.raf.kids.scanner;

import rs.raf.kids.core.Property;
import rs.raf.kids.job.Job;
import rs.raf.kids.scraper.Scraper;
import rs.raf.kids.util.WordCounter;
import java.util.HashMap;
import java.util.Map;

abstract class AbstractScanner implements PathScanner {

    private Scraper scraper;
    private WordCounter counter;
    private Map<String, Map<String, Integer>> counts;
    private String[] keywords;

    protected AbstractScanner() {
        scraper = getScraper();
        counter = new WordCounter();
        counts = new HashMap<>();
        keywords = Property.KEYWORDS.get().split(",");
    }

    @Override
    public void scanPath(String path, Job.ScanType scanType) {
        String content = scraper.getContent(path);

        Map<String, Integer> pathCounts = new HashMap<>();

        for(String keyword : keywords) {
            int count = counter.count(content, keyword);
            pathCounts.put(keyword, count);
        }

        counts.put(path, pathCounts);
    }

    protected abstract Scraper getScraper();
}
