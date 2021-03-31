package rs.raf.kids.crawler;

import rs.raf.kids.job.Job;

public interface PathCrawler {

    void addPath(String path, Job.ScanType scanType);
    void stop();
}
