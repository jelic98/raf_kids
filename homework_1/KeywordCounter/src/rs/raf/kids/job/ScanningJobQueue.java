package rs.raf.kids.job;

import rs.raf.kids.core.Res;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ScanningJobQueue implements JobQueue {

    private final BlockingQueue<Job> jobs;

    public ScanningJobQueue() {
        jobs = new ArrayBlockingQueue<>(Res.CONST_JOB_QUEUE_SIZE);
    }

    @Override
    public void enqueue(Job job) throws InterruptedException {
        jobs.put(job);
    }

    @Override
    public Job dequeue() throws InterruptedException {
        return jobs.take();
    }
}
