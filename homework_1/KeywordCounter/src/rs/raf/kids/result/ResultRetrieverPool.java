package rs.raf.kids.result;

import rs.raf.kids.core.Res;
import rs.raf.kids.job.Job;
import rs.raf.kids.job.ScanType;
import rs.raf.kids.log.Log;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

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
        Map<String, Map<String, Integer>> summary = new HashMap<>();

        for(Result result : results) {
            if(shouldSummarizeResults(result, scanType)) {
                summarizeResults(summary, result);
            }
        }

        return summary;
    }

    @Override
    public Map<String, Map<String, Integer>> querySummary(ScanType scanType) {
        return null;
    }

    @Override
    public void clearSummary(ScanType scanType) {
        results.removeIf(result -> result.getScanType() == scanType);
    }

    @Override
    public void addResult(Job job, Map<String, Integer> counts) {
        Result result = new Result(job, counts);

        results.add(result);
    }

    private boolean shouldCombineResults(Result result, ScanType scanType, String parent) {
        if(scanType == ScanType.FILE) {
            parent = new File(parent).getAbsolutePath();
        }

        boolean typeOk = result.getScanType() == scanType;
        boolean parentOk = childInParent(result.getPath(), parent);

        return typeOk && parentOk;
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

    private boolean shouldSummarizeResults(Result result, ScanType scanType) {
        return result.getScanType() == scanType;
    }

    private void summarizeResults(Map<String, Map<String, Integer>> summary, Result result) {
        String parent = getParent(result);

        if(parent != null) {
            summary.putIfAbsent(parent, new HashMap<>());
            combineResults(summary.get(parent), result);
        }
    }

    private boolean childInParent(String child, String parent) {
        return child.startsWith(parent);
    }

    private String getParent(Result result) {
        ScanType scanType = result.getScanType();
        String path = result.getPath();
        String parent;

        if(scanType == ScanType.FILE) {
            return new File(path).getParentFile().getName();
        }else if(scanType == ScanType.WEB) {
            try {
                parent = new URI(path).getHost();
            }catch(URISyntaxException e) {
                return null;
            }

            return parent.startsWith("www.") ? parent.substring(4) : parent;
        }else {
            return null;
        }
    }
}
