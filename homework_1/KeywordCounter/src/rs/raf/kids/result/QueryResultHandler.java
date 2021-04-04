package rs.raf.kids.result;

import rs.raf.kids.core.Res;
import rs.raf.kids.job.ScanType;
import rs.raf.kids.log.Log;
import java.util.Map;
import java.util.Set;

class QueryResultHandler extends AbstractResultHandler {

    QueryResultHandler(Set<Result> results) {
        super(results);
    }

    @Override
    protected boolean handleResult(Result result, Query query, Map<String, Integer> counts) {
        if(shouldCombineResults(result, query.getScanType(), query.getPath())) {
            if(result.isDone()) {
                combineResults(counts, result);
            }else {
                Log.e(String.format(Res.FORMAT_ERROR, Res.ERROR_CORPUS_NOT_FINISHED, query.getPath()));
                return true;
            }
        }

        return false;
    }

    @Override
    protected boolean handleSummary(Result result, ScanType scanType, Map<String, Map<String, Integer>> summary) {
        if(shouldSummarizeResults(result, scanType)) {
            if(result.isDone()) {
                summarizeResults(summary, result);
            }else {
                Log.e(String.format(Res.FORMAT_ERROR, Res.ERROR_CORPUS_NOT_FINISHED, scanType));
                return true;
            }
        }

        return false;
    }
}
