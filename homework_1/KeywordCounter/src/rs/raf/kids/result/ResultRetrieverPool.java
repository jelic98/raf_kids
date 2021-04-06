package rs.raf.kids.result;

import rs.raf.kids.core.Res;
import rs.raf.kids.job.ScanType;
import rs.raf.kids.log.Log;
import java.util.*;
import java.util.concurrent.*;

public class ResultRetrieverPool implements ResultRetriever {

    private final Set<Result> results;
    private final AbstractResultHandler getResultHandler, queryResultHandler;
    private final ExecutorService pool;

    public ResultRetrieverPool() {
        results = Collections.newSetFromMap(new ConcurrentHashMap<>());
        getResultHandler = new GetResultHandler(results);
        queryResultHandler = new QueryResultHandler(results);
        pool = Executors.newWorkStealingPool(Res.CONST_CPU_CORES_TOTAL);
    }

    @Override
    public void addResult(Result result) {
        results.remove(result);
        results.add(result);
    }

    @Override
    public void handleQuery(String query, boolean sync) {
        if(sync) {
            handle(query, sync);
        }else {
            pool.execute(new Runnable() {
                @Override
                public void run() {
                    handle(query, sync);
                }
            });
        }
    }

    @Override
    public void clearSummary(ScanType scanType) {
        getResultHandler.clearSummary(scanType);
        queryResultHandler.clearSummary(scanType);
    }

    @Override
    public void stop() {
        pool.shutdownNow();
    }

    private void handle(String query, boolean sync) {
        Query q = new Query(query);
        ScanType scanType = q.getScanType();

        if(query.endsWith(Res.CMD_SUMMARY)) {
            Map<String, Map<String, Integer>> result;

            if(sync) {
                result = getSummary(scanType);
            }else {
                result = querySummary(scanType);
            }

            if(result == null) {
                return;
            }

            for(Map.Entry<String, Map<String, Integer>> e : result.entrySet()) {
                JsonObject<String, Integer> json = new JsonObject<>(e.getValue());
                Log.i(String.format(Res.FORMAT_RESULT, e.getKey(), json));
            }
        }else {
            Map<String, Integer> result;

            if(sync) {
                result = getResult(q);
            }else {
                result = queryResult(q);
            }

            if(result == null) {
                return;
            }

            if(result.isEmpty()) {
                Log.e(String.format(Res.FORMAT_ERROR, Res.ERROR_CORPUS_NOT_FOUND, q.getPath()));
            }else {
                JsonObject<String, Integer> json = new JsonObject<>(result);
                Log.i(json.toString());
            }
        }
    }

    private Map<String, Integer> getResult(Query query) {
        return getResultHandler.handleResult(query);
    }

    private Map<String, Integer> queryResult(Query query) {
        return queryResultHandler.handleResult(query);
    }

    private Map<String, Map<String, Integer>> getSummary(ScanType scanType) {
        return getResultHandler.handleSummary(scanType);
    }

    private Map<String, Map<String, Integer>> querySummary(ScanType scanType) {
        return queryResultHandler.handleSummary(scanType);
    }
}
