package rs.raf.kids.crawler;

import rs.raf.kids.scan.ScanType;

public interface PathCrawler {

    void addPath(String path, ScanType scanType);
}
