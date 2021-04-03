package rs.raf.kids.util;

import rs.raf.kids.job.Job;
import java.util.regex.Pattern;

public class Query {

    private Job.ScanType scanType;
    private String path;

    public Query(String query) {
        String[] tokens = query.split(Pattern.quote("|"));

        this.scanType = Job.ScanType.valueOf(tokens[0].trim().toUpperCase());
        this.path = tokens[1].trim();
    }

    public Job.ScanType getScanType() {
        return scanType;
    }

    public String getPath() {
        return path;
    }
}
