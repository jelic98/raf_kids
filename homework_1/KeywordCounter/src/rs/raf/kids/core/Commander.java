package rs.raf.kids.core;

import rs.raf.kids.job.Job;
import rs.raf.kids.job.JobDispatcher;
import rs.raf.kids.job.JobQueue;
import rs.raf.kids.job.ScanningJobQueue;
import rs.raf.kids.log.Log;
import rs.raf.kids.crawler.CrawlerDispatcher;
import rs.raf.kids.scan.ScanType;

class Commander {

    private JobQueue jobQueue;
    private JobDispatcher jobDispatcher;
    private CrawlerDispatcher crawlerDispatcher;

    Commander() {
        jobQueue = new ScanningJobQueue();
        jobDispatcher = new JobDispatcher(jobQueue);
        crawlerDispatcher = new CrawlerDispatcher(jobQueue);
    }

    void startThreads() {
        Log.i(Res.INFO_START_THREADS);

        jobDispatcher.start();
    }

    void stopThreads() {
        Log.i(Res.INFO_STOP_THREADS);

        try {
            jobQueue.enqueue(new Job());
        }catch(InterruptedException e) {
            e.printStackTrace();
        }
    }

    void addDirectory(String path) {
        Log.i(Res.INFO_ADD_DIRECTORY);

        crawlerDispatcher.addPath(path, ScanType.FILE);
    }

    void addWeb(String domain) {
        Log.i(Res.INFO_ADD_WEB);

        crawlerDispatcher.addPath(domain, ScanType.WEB);
    }

    void getResultSync(String query) {
        Log.i(Res.INFO_GET_RESULT_SYNC);
    }

    void getResultAsync(String query) {
        Log.i(Res.INFO_GET_RESULT_ASYNC);
    }

    void clearSummaryFile() {
        Log.i(Res.INFO_CLEAR_SUMMARY_FILE);
    }

    void clearSummaryWeb() {
        Log.i(Res.INFO_CLEAR_SUMMARY_WEB);
    }
}
