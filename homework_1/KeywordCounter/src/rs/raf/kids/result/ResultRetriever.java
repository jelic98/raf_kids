package rs.raf.kids.result;

import rs.raf.kids.job.Job;
import rs.raf.kids.util.Query;
import java.util.Map;

public interface ResultRetriever {

    Map<String, Integer> getResult(Query query);
    Map<String, Integer> queryResult(Query query);

    Map<String, Map<String, Integer>> getSummary(Job.ScanType scanType);
    Map<String, Map<String, Integer>> querySummary(Job.ScanType scanType);
    void clearSummary(Job.ScanType scanType);

    void addResult(Job job, Map<String, Integer> counts);
}
