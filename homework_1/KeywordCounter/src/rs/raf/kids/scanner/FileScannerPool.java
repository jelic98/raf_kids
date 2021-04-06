package rs.raf.kids.scanner;

import rs.raf.kids.core.Property;
import rs.raf.kids.job.Job;
import rs.raf.kids.result.ResultRetriever;
import rs.raf.kids.scraper.Scraper;
import rs.raf.kids.scraper.FileScraper;
import java.io.File;
import java.util.LinkedList;
import java.util.Queue;

public class FileScannerPool extends AbstractScanner {

    private final Queue<Job> jobBuffer;
    private final long BUFFER_SIZE_MAX;
    private final long BUFFER_TIMEOUT_MAX;
    private long bufferSize;
    private Thread thread;

    public FileScannerPool(ResultRetriever resultRetriever) {
        super(resultRetriever);

        jobBuffer = new LinkedList<>();
        BUFFER_SIZE_MAX = Long.parseLong(Property.FILE_SCANNING_SIZE_LIMIT.get());
        BUFFER_TIMEOUT_MAX = Long.parseLong(Property.BUFFER_TIMEOUT.get());
    }

    @Override
    protected Scraper getScraper() {
        return new FileScraper();
    }

    @Override
    public void scanJobPath(Job job) {
        addJob(job);

        if(thread != null && thread.isAlive()) {
            thread.interrupt();
        }

        if(bufferSize > BUFFER_SIZE_MAX) {
            clearBuffer();
        }else {
            thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(BUFFER_TIMEOUT_MAX);
                        clearBuffer();
                    }catch(InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });
            thread.setDaemon(true);
            thread.start();
        }
    }

    @Override
    public void stop() {
        super.stop();

        if(thread != null) {
            thread.interrupt();
        }
    }

    private void addJob(Job job) {
        String path = job.getPath();
        File file = new File(path);

        bufferSize += file.length();

        jobBuffer.add(job);
    }

    private void clearBuffer() {
        bufferSize = 0;

        super.scanJobBuffer(jobBuffer);
    }
}
