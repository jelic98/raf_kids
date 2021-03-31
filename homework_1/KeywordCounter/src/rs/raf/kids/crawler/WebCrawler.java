package rs.raf.kids.crawler;

import rs.raf.kids.core.Property;
import rs.raf.kids.core.Res;
import rs.raf.kids.job.Job;
import rs.raf.kids.job.JobQueue;
import rs.raf.kids.log.Log;
import rs.raf.kids.scraper.WebScraper;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class WebCrawler extends AbstractCrawler {

    private List<Stack<String>> hops;

    WebCrawler(JobQueue jobQueue) {
        super(jobQueue);

        hops = new LinkedList<>();
    }

    @Override
    protected void crawl(String path) {
        Stack<String> base = new Stack<>();
        base.push(path);
        hops.add(base);

        addJob(path, Job.ScanType.WEB);

        WebScraper scraper = new WebScraper();

        for(int i = 0; i < getHopCount(); i++) {
            Stack<String> hop = new Stack<>();

            for(String parentUrl : hops.get(i)) {
                String page = scraper.getContent(parentUrl);

                Pattern p = Pattern.compile(Res.FORMAT_URL);
                Matcher m = p.matcher(page);

                while(m.find()) {
                    String url = m.group();
                    hop.push(url);
                    addJob(url, Job.ScanType.WEB);
                }
            }

            hops.add(hop);
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
