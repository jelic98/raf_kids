package rs.raf.kids.crawler;

import rs.raf.kids.job.Job;
import rs.raf.kids.job.JobQueue;

import java.util.HashMap;
import java.util.Map;

public class CrawlerDispatcher implements PathCrawler {

    private Map<Job.ScanType, PathCrawler> crawlers;

    public CrawlerDispatcher(JobQueue jobQueue) {
        crawlers = new HashMap<>();
        crawlers.put(Job.ScanType.FILE, new DirectoryCrawler(jobQueue));
        crawlers.put(Job.ScanType.WEB, new WebCrawler(jobQueue));
    }

    @Override
    public void addPath(String path, Job.ScanType scanType) {
        crawlers.get(scanType).addPath(path, scanType);
    }
}
