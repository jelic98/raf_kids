package rs.raf.kids.result;

import rs.raf.kids.job.ScanType;
import java.util.regex.Pattern;

public class Query {

    private ScanType scanType;
    private String path;

    public Query(String query) {
        String[] tokens = query.split(Pattern.quote("|"));

        this.scanType = ScanType.valueOf(tokens[0].trim().toUpperCase());
        this.path = tokens[1].trim();
    }

    public ScanType getScanType() {
        return scanType;
    }

    public String getPath() {
        return path;
    }
}
