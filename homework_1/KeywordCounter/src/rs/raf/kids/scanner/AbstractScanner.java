package rs.raf.kids.scanner;

import rs.raf.kids.core.Property;
import rs.raf.kids.core.Res;
import rs.raf.kids.job.Job;
import rs.raf.kids.result.Result;
import rs.raf.kids.result.ResultRetriever;
import rs.raf.kids.scraper.Scraper;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

abstract class AbstractScanner implements PathScanner {

    private final ResultRetriever resultRetriever;
    private final Scraper scraper;
    private final String[] keywords;
    private final ExecutorService pool;

    protected AbstractScanner(ResultRetriever resultRetriever) {
        this.resultRetriever = resultRetriever;

        scraper = getScraper();
        keywords = Property.KEYWORDS.get().split(",");
        pool = Executors.newWorkStealingPool(Res.CONST_CPU_CORES_TOTAL);
    }

    @Override
    public Result publishResult(Job job) {
        Result result = new Result(job);
        resultRetriever.addResult(result);

        return result;
    }

    @Override
    public void stop() {
        pool.shutdownNow();
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
            Result result = publishResult(job);

            String content = scraper.getContent(job.getPath());
            Map<String, Integer> counts = countWords(content, keywords);

            result.combine(counts);
        }
    }

    private Map<String, Integer> countWords(String text, String[] words) {
        Map<String, Integer> counts = new HashMap<>();

        for(String word : words) {
            int count = countWord(text, word);
            counts.put(word, count);
        }

        return counts;
    }

    private int countWord(String text, String word) {
        String sep = String.format(Res.FORMAT_KEYWORD, word);
        String[] counts = text.split(sep);

        return counts.length - 1;
    }

    protected abstract Scraper getScraper();
}
