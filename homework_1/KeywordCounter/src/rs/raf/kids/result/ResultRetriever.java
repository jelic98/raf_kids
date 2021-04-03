package rs.raf.kids.result;

import rs.raf.kids.job.Job;
import rs.raf.kids.job.ScanType;

import java.util.Map;

public interface ResultRetriever {

    Map<String, Integer> getResult(Query query);
    Map<String, Integer> queryResult(Query query);

    Map<String, Map<String, Integer>> getSummary(ScanType scanType);
    Map<String, Map<String, Integer>> querySummary(ScanType scanType);
    void clearSummary(ScanType scanType);

    void addResult(Job job, Map<String, Integer> counts);
}
