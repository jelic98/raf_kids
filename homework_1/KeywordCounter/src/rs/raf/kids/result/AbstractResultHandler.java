package rs.raf.kids.result;

import rs.raf.kids.job.ScanType;
import rs.raf.kids.log.Log;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

abstract class AbstractResultHandler {

    private static final Map<Query, Map<String, Integer>> cacheResult;
    private static final Map<ScanType, Map<String, Map<String, Integer>>> cacheSummary;

    static {
        cacheResult = new ConcurrentHashMap<>();
        cacheSummary = new ConcurrentHashMap<>();
    }

    private final Set<Result> results;

    AbstractResultHandler(Set<Result> results) {
        this.results = results;
    }

    Map<String, Integer> handleResult(Query query) {
        if(cacheResult.containsKey(query)) {
            return cacheResult.get(query);
        }

        Map<String, Integer> counts = new HashMap<>();

        for(Result result : results) {
            if(handleResult(result, query, counts)) {
                return null;
            }
        }

        if(counts.isEmpty()) {
            return null;
        }

        cacheResult.putIfAbsent(query, counts);

        return counts;
    }

    Map<String, Map<String, Integer>> handleSummary(ScanType scanType) {
        if(cacheSummary.containsKey(scanType)) {
            return cacheSummary.get(scanType);
        }

        Map<String, Map<String, Integer>> summary = new HashMap<>();

        for(Result result : results) {
            while(handleSummary(result, scanType, summary));
        }

        if(summary.isEmpty()) {
           return null;
        }

        cacheSummary.putIfAbsent(scanType, summary);

        return summary;
    }

    void clearSummary(ScanType scanType) {
        cacheSummary.remove(scanType);
    }

    protected boolean shouldCombineResults(Result result, ScanType scanType, String parent) {
        boolean typeOk = result.getScanType() == scanType;
        boolean parentOk = parent.equals(getParent(result));

        return typeOk && parentOk;
    }

    protected void combineResults(Map<String, Integer> counts, Result result) {
        for(Map.Entry<String, Integer> e : result.getCounts().entrySet()) {
            String keyword = e.getKey();
            int count = e.getValue();

            counts.putIfAbsent(keyword, 0);
            count += counts.get(keyword);

            counts.put(keyword, count);
        }
    }

    protected boolean shouldSummarizeResults(Result result, ScanType scanType) {
        return result.getScanType() == scanType;
    }

    protected void summarizeResults(Map<String, Map<String, Integer>> summary, Result result) {
        String parent = getParent(result);

        if(parent != null) {
            summary.putIfAbsent(parent, new HashMap<>());
            combineResults(summary.get(parent), result);
        }
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

    private boolean handleSummary(Result result, ScanType scanType, Map<String, Map<String, Integer>> summary) {
        if(shouldSummarizeResults(result, scanType)) {
            if(result.isDone()) {
                summarizeResults(summary, result);
            }else {
                return true;
            }
        }

        return false;
    }

    protected abstract boolean handleResult(Result result, Query query, Map<String, Integer> counts);
}
