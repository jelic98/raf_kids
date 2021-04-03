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

    private Queue<Job> jobBuffer;
    private long bufferSize;
    private final long BUFFER_SIZE_MAX;

    public FileScannerPool(ResultRetriever resultRetriever) {
        super(resultRetriever);

        jobBuffer = new LinkedList<>();
        BUFFER_SIZE_MAX = Long.parseLong(Property.FILE_SCANNING_SIZE_LIMIT.get());
    }

    @Override
    protected Scraper getScraper() {
        return new FileScraper();
    }

    @Override
    public void scanJobPath(Job job) {
        addJob(job);

        if(bufferSize > BUFFER_SIZE_MAX) {
            clearBuffer();
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
