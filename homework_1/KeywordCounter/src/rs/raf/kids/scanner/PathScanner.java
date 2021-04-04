package rs.raf.kids.scanner;

import rs.raf.kids.job.Job;
import rs.raf.kids.result.Result;

public interface PathScanner {

    void scanJobPath(Job job);
    Result publishResult(Job job);
    void stop();
}
