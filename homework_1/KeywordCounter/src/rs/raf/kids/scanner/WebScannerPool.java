package rs.raf.kids.scanner;

import rs.raf.kids.result.ResultRetriever;
import rs.raf.kids.scraper.Scraper;
import rs.raf.kids.scraper.WebScraper;

public class WebScannerPool extends AbstractScanner {

    public WebScannerPool(ResultRetriever resultRetriever) {
        super(resultRetriever);
    }

    @Override
    protected Scraper getScraper() {
        return new WebScraper();
    }
}
