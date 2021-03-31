package rs.raf.kids.scanner;

import rs.raf.kids.scraper.Scraper;
import rs.raf.kids.scraper.WebScraper;

public class WebScannerPool extends AbstractScanner {

    @Override
    protected Scraper getScraper() {
        return new WebScraper();
    }
}
