package rs.raf.kids.job;

import java.util.Map;
import java.util.concurrent.Future;

public class Job {

    public enum ScanType {
        FILE,
        WEB,
        POISON
    }

    private final String path;
    private final ScanType scanType;

    public Job() {
        this(null, ScanType.POISON);
    }

    public Job(String path, ScanType scanType) {
        this.path = path;
        this.scanType = scanType;
    }

    public String getPath() {
        return path;
    }

    public ScanType getScanType() {
        return scanType;
    }

    public Future<Map<String, Integer>> initiate() {
        return null;
    }
}
