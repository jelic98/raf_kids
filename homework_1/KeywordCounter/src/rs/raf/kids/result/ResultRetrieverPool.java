package rs.raf.kids.result;

import rs.raf.kids.job.Job;
import java.util.Map;
import java.util.concurrent.Future;

public class ResultRetrieverPool implements ResultRetriever {

    @Override
    public Map<String, Integer> getResult(String query) {
        return null;
    }

    @Override
    public Map<String, Integer> queryResult(String query) {
        return null;
    }

    @Override
    public Map<String, Map<String, Integer>> getSummary(Job.ScanType scanType) {
        return null;
    }

    @Override
    public Map<String, Map<String, Integer>> querySummary(Job.ScanType scanType) {
        return null;
    }

    @Override
    public void clearSummary(Job.ScanType scanType) {

    }

    @Override
    public void addResult(String path, Future<Map<String, Integer>> result) {

    }
}
