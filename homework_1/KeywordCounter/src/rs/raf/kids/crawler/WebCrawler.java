package rs.raf.kids.crawler;

import rs.raf.kids.job.JobQueue;
import rs.raf.kids.scan.ScanType;

class WebCrawler implements PathCrawler {

    private JobQueue jobQueue;

    public WebCrawler(JobQueue jobQueue) {
        this.jobQueue = jobQueue;
    }

    @Override
    public void addPath(String path, ScanType scanType) {
        // TODO Add job to JobQueue
    }
}
