package rs.raf.kids.crawler;

import rs.raf.kids.job.ScanType;

public interface PathCrawler {

    void addPath(String path, ScanType scanType);
    void stop();
}
