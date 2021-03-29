package rs.raf.kids.job;

import rs.raf.kids.scan.FileScannerPool;
import rs.raf.kids.scan.PathScanner;
import rs.raf.kids.scan.ScanType;
import rs.raf.kids.scan.WebScannerPool;
import java.util.HashMap;
import java.util.Map;

public class JobDispatcher {

    private JobQueue jobQueue;
    private Map<ScanType, PathScanner> scanners;
    private Thread thread;

    public JobDispatcher(JobQueue jobQueue) {
        this.jobQueue = jobQueue;

        scanners = new HashMap<>();
        scanners.put(ScanType.FILE, new FileScannerPool());
        scanners.put(ScanType.WEB, new WebScannerPool());

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
                ScanType scanType = job.getScanType();

                if(scanType == ScanType.POISON) {
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
