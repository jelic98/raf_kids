package rs.raf.kids.crawler;

import rs.raf.kids.job.JobQueue;
import rs.raf.kids.job.ScanType;
import java.util.HashMap;
import java.util.Map;

public class CrawlerDispatcher implements PathCrawler {

    private Map<ScanType, PathCrawler> crawlers;

    public CrawlerDispatcher(JobQueue jobQueue) {
        crawlers = new HashMap<>();
        crawlers.put(ScanType.FILE, new DirectoryCrawler(jobQueue));
        crawlers.put(ScanType.WEB, new WebCrawler(jobQueue));
    }

    @Override
    public void addPath(String path, ScanType scanType) {
        crawlers.get(scanType).addPath(path, scanType);
    }

    @Override
    public void stop() {
        for(PathCrawler crawler : crawlers.values()) {
            crawler.stop();
        }
    }
}
