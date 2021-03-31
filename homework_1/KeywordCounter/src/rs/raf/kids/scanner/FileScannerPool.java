package rs.raf.kids.scanner;

import rs.raf.kids.result.ResultRetriever;
import rs.raf.kids.scraper.Scraper;
import rs.raf.kids.scraper.FileScraper;

public class FileScannerPool extends AbstractScanner {

    public FileScannerPool(ResultRetriever resultRetriever) {
        super(resultRetriever);
    }

    @Override
    protected Scraper getScraper() {
        return new FileScraper();
    }
}
