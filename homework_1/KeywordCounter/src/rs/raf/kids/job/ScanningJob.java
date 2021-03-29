package rs.raf.kids.job;

import rs.raf.kids.scan.ScanType;

import java.util.Map;
import java.util.concurrent.Future;

public interface ScanningJob {

    ScanType getType();

    String getQuery();

    Future<Map<String, Integer>> initiate();
}
