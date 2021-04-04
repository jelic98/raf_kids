package rs.raf.kids.result;

import rs.raf.kids.job.ScanType;
import java.util.*;

public class ResultRetrieverPool implements ResultRetriever {

    private Set<Result> results;
    private AbstractResultHandler getResultHandler, queryResultHandler;

    public ResultRetrieverPool() {
        results = new HashSet<>();
        getResultHandler = new GetResultHandler(results);
        queryResultHandler = new QueryResultHandler(results);
    }

    @Override
    public Map<String, Integer> getResult(Query query) {
        return getResultHandler.handleResult(query);
    }

    @Override
    public Map<String, Integer> queryResult(Query query) {
        return queryResultHandler.handleResult(query);
    }

    @Override
    public Map<String, Map<String, Integer>> getSummary(ScanType scanType) {
        return getResultHandler.handleSummary(scanType);
    }

    @Override
    public Map<String, Map<String, Integer>> querySummary(ScanType scanType) {
        return queryResultHandler.handleSummary(scanType);
    }

    @Override
    public void clearSummary(ScanType scanType) {
        Iterator<Result> i = results.iterator();

        while(i.hasNext()) {
            Result result = i.next();

            if(result.getScanType() == scanType) {
                i.remove();
            }
        }
    }

    @Override
    public void addResult(Result result) {
        results.remove(result);
        results.add(result);
    }
}
