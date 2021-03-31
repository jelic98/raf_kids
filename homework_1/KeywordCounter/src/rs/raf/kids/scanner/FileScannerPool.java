package rs.raf.kids.scanner;

import rs.raf.kids.scraper.Scraper;
import rs.raf.kids.scraper.FileScraper;

public class FileScannerPool extends AbstractScanner {

    @Override
    protected Scraper getScraper() {
        return new FileScraper();
    }
}
