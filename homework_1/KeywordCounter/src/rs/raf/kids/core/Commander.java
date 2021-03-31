package rs.raf.kids.core;

import rs.raf.kids.job.Job;
import rs.raf.kids.job.JobDispatcher;
import rs.raf.kids.job.JobQueue;
import rs.raf.kids.job.ScanningJobQueue;
import rs.raf.kids.log.Log;
import rs.raf.kids.crawler.CrawlerDispatcher;
import rs.raf.kids.result.ResultRetriever;
import rs.raf.kids.result.ResultRetrieverPool;

class Commander {

    private JobQueue jobQueue;
    private JobDispatcher jobDispatcher;
    private CrawlerDispatcher crawlerDispatcher;
    private ResultRetriever resultRetriever;

    Commander() {
        jobQueue = new ScanningJobQueue();
        jobDispatcher = new JobDispatcher(jobQueue);
        crawlerDispatcher = new CrawlerDispatcher(jobQueue);
        resultRetriever = new ResultRetrieverPool();
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

        crawlerDispatcher.addPath(path, Job.ScanType.FILE);
    }

    void addWeb(String domain) {
        Log.i(Res.INFO_ADD_WEB);

        crawlerDispatcher.addPath(domain, Job.ScanType.WEB);
    }

    void getResultSync(String query) {
        Log.i(Res.INFO_GET_RESULT_SYNC);

        if(query.endsWith(Res.CMD_SUMMARY)) {
            resultRetriever.getSummary(Job.ScanType.FILE);
        }else {
            resultRetriever.getResult(query);
        }
    }

    void getResultAsync(String query) {
        Log.i(Res.INFO_GET_RESULT_ASYNC);

        if(query.endsWith(Res.CMD_SUMMARY)) {
            resultRetriever.querySummary(Job.ScanType.FILE);
        }else {
            resultRetriever.queryResult(query);
        }
    }

    void clearSummaryFile() {
        Log.i(Res.INFO_CLEAR_SUMMARY_FILE);

        resultRetriever.clearSummary(Job.ScanType.FILE);
    }

    void clearSummaryWeb() {
        Log.i(Res.INFO_CLEAR_SUMMARY_WEB);

        resultRetriever.clearSummary(Job.ScanType.WEB);
    }
}
