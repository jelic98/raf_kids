package rs.raf.kids.crawler;

import rs.raf.kids.core.Property;
import rs.raf.kids.core.Res;
import rs.raf.kids.job.JobQueue;
import rs.raf.kids.job.ScanType;
import rs.raf.kids.log.Log;
import rs.raf.kids.scraper.WebScraper;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class WebCrawler extends AbstractCrawler {

    private List<Stack<String>> hops;
    private Set<String> scannedPaths;
    private Thread thread;
    private final long URL_REFRESH_TIMEOUT;

    WebCrawler(JobQueue jobQueue) {
        super(jobQueue);

        hops = new LinkedList<>();
        scannedPaths = new HashSet<>();
        URL_REFRESH_TIMEOUT = Long.parseLong(Property.URL_REFRESH_TIME.get());

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(URL_REFRESH_TIMEOUT);
                    scannedPaths.clear();
                }catch(InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    @Override
    protected void crawl(String path) {
        boolean invalidPath = false;

        Stack<String> base = new Stack<>();
        base.push(path);
        hops.add(base);

        WebScraper scraper = new WebScraper();

        for(int i = 0; i < getHopCount(); i++) {
            Stack<String> hop = new Stack<>();

            for(String parentUrl : hops.get(i)) {
                String page = scraper.getContent(parentUrl);

                if(page.isEmpty()) {
                    invalidPath = true;
                    break;
                }

                Pattern p = Pattern.compile(Res.FORMAT_URL);
                Matcher m = p.matcher(page);

                while(m.find()) {
                    String url = m.group();
                    hop.push(url);
                    addJob(url, ScanType.WEB);
                }
            }

            if(invalidPath) {
                break;
            }

            hops.add(hop);
        }

        if(!invalidPath) {
            addJob(path, ScanType.WEB);
        }
    }

    @Override
    protected void addJob(String path, ScanType scanType) {
        if(!scannedPaths.contains(path)) {
            scannedPaths.add(path);

            super.addJob(path, scanType);
        }
    }

    private int getHopCount() {
        int hopCount = 0;

        try {
            hopCount = Integer.parseInt(Property.HOP_COUNT.get());
        }catch(NumberFormatException e) {
            Log.e(Res.ERROR_HOP_COUNT);
        }

        return hopCount;
    }
}
