package rs.raf.kids.crawler;

import rs.raf.kids.scan.ScanType;
import java.util.HashMap;
import java.util.Map;

public class CrawlerDispatcher implements PathCrawler {

    private Map<ScanType, PathCrawler> crawlers;

    public CrawlerDispatcher() {
        crawlers = new HashMap<>();
        crawlers.put(ScanType.FILE, new DirectoryCrawler());
        crawlers.put(ScanType.WEB, new WebCrawler());
    }

    @Override
    public void addPath(String path, ScanType scanType) {
        crawlers.get(scanType).addPath(path, scanType);
    }
}
