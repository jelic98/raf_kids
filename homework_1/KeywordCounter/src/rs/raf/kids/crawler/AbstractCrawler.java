package rs.raf.kids.crawler;

import rs.raf.kids.job.Job;
import rs.raf.kids.job.JobQueue;
import rs.raf.kids.job.ScanType;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

abstract class AbstractCrawler implements PathCrawler {

    private final JobQueue jobQueue;
    private final ExecutorService pool;

    protected AbstractCrawler(JobQueue jobQueue) {
        this.jobQueue = jobQueue;

        pool = Executors.newSingleThreadExecutor();
    }

    @Override
    public void addPath(String path, ScanType scanType) {
        pool.execute(new Runnable() {
            @Override
            public void run() {
                crawl(path);
            }
        });
    }

    @Override
    public void stop() {
        pool.shutdownNow();
    }

    protected void addJob(String path, ScanType scanType) {
        try {
            jobQueue.enqueue(new Job(path, scanType));
        }catch(InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected abstract void crawl(String path);
}
