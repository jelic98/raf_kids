package rs.raf.kids.result;

import rs.raf.kids.job.Job;

import java.util.Map;
import java.util.concurrent.Future;

public interface ResultRetriever {

    Map<String, Integer> getResult(String query);
    Map<String, Integer> queryResult(String query);

    Map<String, Map<String, Integer>> getSummary(Job.ScanType summaryType);
    Map<String, Map<String, Integer>> querySummary(Job.ScanType summaryType);
    void clearSummary(Job.ScanType summaryType);

    void addCorpusResult(String corpusName, Future<Map<String, Integer>> corpusResult);
}
