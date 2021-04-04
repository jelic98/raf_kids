package rs.raf.kids.result;

import rs.raf.kids.core.Res;
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

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Query) {
            Query query = (Query) obj;

            boolean scanTypeOk = query.scanType.equals(scanType);
            boolean pathOk = query.path.equals(path);

            return scanTypeOk && pathOk;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Res.CONST_HASH_PRIME * scanType.hashCode() + path.hashCode();
    }
}
