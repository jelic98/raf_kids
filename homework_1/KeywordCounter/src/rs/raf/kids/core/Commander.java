package rs.raf.kids.core;

import rs.raf.kids.job.*;
import rs.raf.kids.log.Log;
import rs.raf.kids.crawler.CrawlerDispatcher;
import rs.raf.kids.result.ResultRetriever;
import rs.raf.kids.result.ResultRetrieverPool;
import rs.raf.kids.result.Query;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

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
    private BlockingQueue<String> queries;
    private Thread thread;

    Commander() {
        jobQueue = new ScanningJobQueue();
        resultRetriever = new ResultRetrieverPool();
        crawlerDispatcher = new CrawlerDispatcher(jobQueue);
        jobDispatcher = new JobDispatcher(jobQueue, resultRetriever);
        queries = new ArrayBlockingQueue<>(Res.CONST_QUERY_QUEUE_SIZE);

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        handleQuery(queries.take());
                    }catch(InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
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

        handleQuery(query);
    }

    void getResultAsync(String query) {
        Log.i(Res.INFO_GET_RESULT_ASYNC);

        try {
            queries.put(query);
        }catch(InterruptedException e) {
            e.printStackTrace();
        }
    }

    void clearSummaryFile() {
        Log.i(Res.INFO_CLEAR_SUMMARY_FILE);

        resultRetriever.clearSummary(ScanType.FILE);
    }

    void clearSummaryWeb() {
        Log.i(Res.INFO_CLEAR_SUMMARY_WEB);

        resultRetriever.clearSummary(ScanType.WEB);
    }

    private void handleQuery(String query) {
        Query q = new Query(query);
        ScanType scanType = q.getScanType();

        if(query.endsWith(Res.CMD_SUMMARY)) {
            Map<String, Map<String, Integer>> result = null;

            if(scanType == ScanType.FILE) {
                result = resultRetriever.getSummary(scanType);
            }else if(scanType == ScanType.WEB) {
                result = resultRetriever.querySummary(scanType);
            }

            if(result == null) {
                return;
            }

            for(Map.Entry<String, Map<String, Integer>> e : result.entrySet()) {
                JsonObject<String, Integer> json = new JsonObject<>(e.getValue());
                Log.i(String.format(Res.FORMAT_RESULT, e.getKey(), json));
            }
        }else {
            Map<String, Integer> result = null;

            if(scanType == ScanType.FILE) {
                result = resultRetriever.getResult(q);
            }else if(scanType == ScanType.WEB) {
                result = resultRetriever.queryResult(q);
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
}
