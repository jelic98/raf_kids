package rs.raf.kids.job;

public class Job {

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
}
