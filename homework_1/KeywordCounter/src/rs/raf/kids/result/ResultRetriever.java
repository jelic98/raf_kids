package rs.raf.kids.result;

import rs.raf.kids.job.ScanType;

public interface ResultRetriever {

    void addResult(Result result);
    void handleQuery(String query, boolean sync);
    void clearSummary(ScanType scanType);
    void stop();
}
