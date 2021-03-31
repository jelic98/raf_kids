package rs.raf.kids.result;

import rs.raf.kids.job.Job;
import java.util.Map;
import java.util.concurrent.Future;

public interface ResultRetriever {

    Map<String, Integer> getResult(String query);
    Map<String, Integer> queryResult(String query);

    Map<String, Map<String, Integer>> getSummary(Job.ScanType scanType);
    Map<String, Map<String, Integer>> querySummary(Job.ScanType scanType);
    void clearSummary(Job.ScanType scanType);

    void addResult(String path, Future<Map<String, Integer>> result);
}
