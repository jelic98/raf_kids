package rs.raf.kids.scanner;

import rs.raf.kids.job.Job;

public interface PathScanner {

    void scanPath(String path, Job.ScanType scanType);
}
