package rs.raf.kids.scanner;

import rs.raf.kids.job.Job;
import java.util.Map;

public interface PathScanner {

    void scanJobPath(Job job);
    void publishResult(Job job, Map<String, Integer> result);
    void stop();
}
