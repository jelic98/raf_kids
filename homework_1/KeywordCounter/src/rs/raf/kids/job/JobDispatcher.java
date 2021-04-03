package rs.raf.kids.job;

import rs.raf.kids.result.ResultRetriever;
import rs.raf.kids.scanner.FileScannerPool;
import rs.raf.kids.scanner.PathScanner;
import rs.raf.kids.scanner.WebScannerPool;
import java.util.HashMap;
import java.util.Map;

public class JobDispatcher {

    private JobQueue jobQueue;
    private Map<ScanType, PathScanner> scanners;
    private Thread thread;

    public JobDispatcher(JobQueue jobQueue, ResultRetriever resultRetriever) {
        this.jobQueue = jobQueue;

        scanners = new HashMap<>();
        scanners.put(ScanType.FILE, new FileScannerPool(resultRetriever));
        scanners.put(ScanType.WEB, new WebScannerPool(resultRetriever));

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                dispatchJobs();
            }
        });
        thread.setDaemon(true);
    }

    private void dispatchJobs() {
        while(true) {
            try {
                Job job = jobQueue.dequeue();
                ScanType scanType = job.getScanType();

                if(scanType == ScanType.POISON) {
                    for(PathScanner scanner : scanners.values()) {
                        scanner.stop();
                    }

                    break;
                }

                scanners.get(scanType).scanJobPath(job);
            }catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void start() {
        thread.start();
    }
}
