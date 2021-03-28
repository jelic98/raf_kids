package rs.raf.kids.thread;

import rs.raf.kids.core.Res;
import rs.raf.kids.log.Log;

public class Commander {

    public void addDirectory(String path) {
        Log.i(Res.INFO_ADD_DIRECTORY);
    }

    public void addWeb(String domain) {
        Log.i(Res.INFO_ADD_WEB);
    }

    public void getResultSync(String query) {
        Log.i(Res.INFO_GET_RESULT_SYNC);
    }

    public void getResultAsync(String query) {
        Log.i(Res.INFO_GET_RESULT_ASYNC);
    }

    public void clearSummaryFile() {
        Log.i(Res.INFO_CLEAR_SUMMARY_FILE);
    }

    public void clearSummaryWeb() {
        Log.i(Res.INFO_CLEAR_SUMMARY_WEB);
    }
}
