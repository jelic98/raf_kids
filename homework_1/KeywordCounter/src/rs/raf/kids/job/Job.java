package rs.raf.kids.job;

import rs.raf.kids.core.Res;

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

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Job) {
            Job job = (Job) obj;

            boolean pathOk = job.path.equals(path);
            boolean scanTypeOk = job.scanType.equals(scanType);

            return pathOk && scanTypeOk;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Res.CONST_HASH_PRIME * path.hashCode() + scanType.hashCode();
    }
}
