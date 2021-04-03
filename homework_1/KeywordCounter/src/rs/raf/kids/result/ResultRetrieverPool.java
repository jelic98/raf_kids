package rs.raf.kids.result;

import rs.raf.kids.job.Job;
import rs.raf.kids.job.ScanType;g
import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ResultRetrieverPool implements ResultRetriever {

    private static class Result {

        private Job job;
        private Map<String, Integer> counts;

        private Result(Job job, Map<String, Integer> counts) {
            this.job = job;
            this.counts = counts;
        }

        public String getPath() {
            return job.getPath();
        }

        public ScanType getScanType() {
            return job.getScanType();
        }

        public Map<String, Integer> getCounts() {
            return counts;
        }
    }

    private List<Result> results;

    public ResultRetrieverPool() {
        results = new LinkedList<>();
    }

    @Override
    public Map<String, Integer> getResult(Query query) {
        Map<String, Integer> counts = new HashMap<>();

        for(Result result : results) {
            if(shouldCombineResults(result, query.getScanType(), query.getPath())) {
                combineResults(counts, result);
            }
        }

        return counts;
    }

    @Override
    public Map<String, Integer> queryResult(Query query) {
        return null;
    }

    @Override
    public Map<String, Map<String, Integer>> getSummary(ScanType scanType) {
        return null;
    }

    @Override
    public Map<String, Map<String, Integer>> querySummary(ScanType scanType) {
        return null;
    }

    @Override
    public void clearSummary(ScanType scanType) {

    }

    @Override
    public void addResult(Job job, Map<String, Integer> counts) {
        Result result = new Result(job, counts);

        results.add(result);
    }

    private boolean shouldCombineResults(Result result, ScanType scanType, String path) {
        if(scanType == ScanType.FILE) {
            path = new File(path).getAbsolutePath();
        }

        boolean typeOk = result.getScanType() == scanType;
        boolean parentOk = childInParent(result.getPath(), path);

        return typeOk && parentOk;
    }

    private boolean childInParent(String child, String parent) {
        return child.startsWith(parent);
    }

    private void combineResults(Map<String, Integer> counts, Result result) {
        for(Map.Entry<String, Integer> e : result.getCounts().entrySet()) {
            String keyword = e.getKey();
            int count = e.getValue();

            counts.putIfAbsent(keyword, 0);
            count += counts.get(keyword);

            counts.put(keyword, count);
        }
    }
}
