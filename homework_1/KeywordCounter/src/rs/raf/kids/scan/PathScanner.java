package rs.raf.kids.scan;

import rs.raf.kids.job.Job;

public interface PathScanner {

    void addPath(String path, Job.ScanType scanType);
}
