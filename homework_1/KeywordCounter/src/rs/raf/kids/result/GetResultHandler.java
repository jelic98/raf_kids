package rs.raf.kids.result;

import rs.raf.kids.job.ScanType;
import java.util.Map;
import java.util.Set;

class GetResultHandler extends AbstractResultHandler {

    GetResultHandler(Set<Result> results) {
        super(results);
    }

    @Override
    protected boolean handleResult(Result result, Query query, Map<String, Integer> counts) {
        if(result.isDone() && shouldCombineResults(result, query.getScanType(), query.getPath())) {
            combineResults(counts, result);
        }

        return false;
    }

    @Override
    protected boolean handleSummary(Result result, ScanType scanType, Map<String, Map<String, Integer>> summary) {
        if(result.isDone() && shouldSummarizeResults(result, scanType)) {
            summarizeResults(summary, result);
        }

        return false;
    }
}
