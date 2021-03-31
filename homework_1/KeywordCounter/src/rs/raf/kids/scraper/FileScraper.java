package rs.raf.kids.scraper;

import rs.raf.kids.core.Res;
import rs.raf.kids.log.Log;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileScraper implements Scraper {

    @Override
    public String getContent(String path) {
        String file = null;

        try {
            file = Files.readString(Paths.get(path));
        }catch(IOException e) {
            Log.e(Res.ERROR_SCAN_FILE);
        }

        return file != null ? file : "";
    }
}
