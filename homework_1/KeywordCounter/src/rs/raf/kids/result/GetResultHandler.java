package rs.raf.kids.result;

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
}
