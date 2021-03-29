package rs.raf.kids.core;

import rs.raf.kids.log.Log;
import rs.raf.kids.crawler.CrawlerDispatcher;
import rs.raf.kids.scan.ScanType;

class Commander {

    private CrawlerDispatcher crawlerDispatcher;

    Commander() {
        crawlerDispatcher = new CrawlerDispatcher();
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
    }

    void getResultAsync(String query) {
        Log.i(Res.INFO_GET_RESULT_ASYNC);
    }

    void clearSummaryFile() {
        Log.i(Res.INFO_CLEAR_SUMMARY_FILE);
    }

    void clearSummaryWeb() {
        Log.i(Res.INFO_CLEAR_SUMMARY_WEB);
    }
}
