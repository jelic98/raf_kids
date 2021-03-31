package rs.raf.kids.job;

import rs.raf.kids.scan.FileScannerPool;
import rs.raf.kids.scan.PathScanner;
import rs.raf.kids.scan.WebScannerPool;
import java.util.HashMap;
import java.util.Map;

public class JobDispatcher {

    private JobQueue jobQueue;
    private Map<Job.ScanType, PathScanner> scanners;
    private Thread thread;

    public JobDispatcher(JobQueue jobQueue) {
        this.jobQueue = jobQueue;

        scanners = new HashMap<>();
        scanners.put(Job.ScanType.FILE, new FileScannerPool());
        scanners.put(Job.ScanType.WEB, new WebScannerPool());

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                dispatchJobs();
            }
        });
    }

    private void dispatchJobs() {
        while(true) {
            try {
                Job job = jobQueue.dequeue();
                Job.ScanType scanType = job.getScanType();

                if(scanType == Job.ScanType.POISON) {
                    break;
                }

                scanners.get(scanType).addPath(job.getPath(), scanType);
            }catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void start() {
        thread.start();
    }
}
