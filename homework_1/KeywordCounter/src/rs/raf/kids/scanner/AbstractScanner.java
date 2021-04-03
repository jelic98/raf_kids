package rs.raf.kids.scanner;

import rs.raf.kids.core.Property;
import rs.raf.kids.core.Res;
import rs.raf.kids.job.Job;
import rs.raf.kids.result.ResultRetriever;
import rs.raf.kids.scraper.Scraper;
import rs.raf.kids.util.WordCounter;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

abstract class AbstractScanner implements PathScanner {

    private ResultRetriever resultRetriever;
    private Scraper scraper;
    private WordCounter counter;
    private String[] keywords;
    private ExecutorService pool;

    protected AbstractScanner(ResultRetriever resultRetriever) {
        this.resultRetriever = resultRetriever;

        scraper = getScraper();
        counter = new WordCounter();
        keywords = Property.KEYWORDS.get().split(",");
        pool = Executors.newWorkStealingPool(Res.CONST_CPU_CORES_OTAL);
    }

    @Override
    public void publishResult(Job job, Map<String, Integer> counts) {
        resultRetriever.addResult(job, counts);
    }

    @Override
    public void stop() {
        pool.shutdown();
    }

    protected void scanJobBuffer(Job job) {
        Queue<Job> jobBuffer = new LinkedList<>(Collections.singletonList(job));
        scanJobBuffer(jobBuffer);
    }

    protected void scanJobBuffer(Queue<Job> jobBuffer) {
        pool.execute(new Runnable() {
            @Override
            public void run() {
                scan(jobBuffer);
            }
        });
    }

    private void scan(Queue<Job> jobBuffer) {
        while(!jobBuffer.isEmpty()) {
            Job job = jobBuffer.remove();
            String path = job.getPath();
            String content = scraper.getContent(path);

            Map<String, Integer> counts = new HashMap<>();

            for(String keyword : keywords) {
                int count = counter.count(content, keyword);
                counts.put(keyword, count);
            }

            publishResult(job, counts);
        }
    }

    protected abstract Scraper getScraper();
}
