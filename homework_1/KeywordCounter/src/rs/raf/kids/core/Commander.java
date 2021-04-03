package rs.raf.kids.core;

import rs.raf.kids.job.*;
import rs.raf.kids.log.Log;
import rs.raf.kids.crawler.CrawlerDispatcher;
import rs.raf.kids.result.ResultRetriever;
import rs.raf.kids.result.ResultRetrieverPool;
import rs.raf.kids.result.Query;
import java.util.Map;

class Commander {

    private class JsonObject<K, V> {

        private Map<K, V> map;

        public JsonObject(Map<K, V> map) {
            this.map = map;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("{");

            int i = 0;

            for(Map.Entry<K, V> e : map.entrySet()) {
                if(i++ > 0) {
                    sb.append(", ");
                }

                sb.append(e.getKey());
                sb.append("=");
                sb.append(e.getValue());
            }

            sb.append("}");

            return sb.toString();
        }
    }

    private JobQueue jobQueue;
    private JobDispatcher jobDispatcher;
    private CrawlerDispatcher crawlerDispatcher;
    private ResultRetriever resultRetriever;

    Commander() {
        jobQueue = new ScanningJobQueue();
        resultRetriever = new ResultRetrieverPool();
        crawlerDispatcher = new CrawlerDispatcher(jobQueue);
        jobDispatcher = new JobDispatcher(jobQueue, resultRetriever);
    }

    void startThreads() {
        Log.i(Res.INFO_START_THREADS);

        jobDispatcher.start();
    }

    void stopThreads() {
        Log.i(Res.INFO_STOP_THREADS);

        crawlerDispatcher.stop();

        try {
            jobQueue.enqueue(new Job());
        }catch(InterruptedException e) {
            e.printStackTrace();
        }
    }

    void addDirectory(String path) {
        Log.i(Res.INFO_ADD_DIRECTORY);

        crawlerDispatcher.addPath(path, ScanType.FILE);
    }

    void addWeb(String domain) {
        Log.i(Res.INFO_ADD_WEB);

        crawlerDispatcher.addPath(domain, ScanType.WEB);
    }

    void getResultSync(String query) {
        Log.i(Res.INFO_GET_RESULT_SYNC);

        Query q = new Query(query);

        if(query.endsWith(Res.CMD_SUMMARY)) {
            Map<String, Map<String, Integer>> result = resultRetriever.getSummary(ScanType.FILE);

            for(Map.Entry<String, Map<String, Integer>> e : result.entrySet()) {
                String parent = e.getKey();
                JsonObject<String, Integer> json = new JsonObject<>(e.getValue());

                Log.i(String.format(Res.FORMAT_RESULT, parent, json));
            }
        }else {
            Map<String, Integer> result = resultRetriever.getResult(q);
            JsonObject<String, Integer> json = new JsonObject<>(result);

            if(result.isEmpty()) {
                Log.e(String.format(Res.FORMAT_ERROR, Res.ERROR_CORPUS_NOT_FOUND, q.getPath()));
            }else {
                Log.i(json.toString());
            }
        }
    }

    void getResultAsync(String query) {
        Log.i(Res.INFO_GET_RESULT_ASYNC);

        Query q = new Query(query);

        if(query.endsWith(Res.CMD_SUMMARY)) {
            resultRetriever.querySummary(ScanType.FILE);
        }else {
            resultRetriever.queryResult(q);
        }

        // TODO If corpus does not exist report error like in getResultSync method
    }

    void clearSummaryFile() {
        Log.i(Res.INFO_CLEAR_SUMMARY_FILE);

        resultRetriever.clearSummary(ScanType.FILE);
    }

    void clearSummaryWeb() {
        Log.i(Res.INFO_CLEAR_SUMMARY_WEB);

        resultRetriever.clearSummary(ScanType.WEB);
    }
}
