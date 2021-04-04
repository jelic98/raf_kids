package rs.raf.kids.crawler;

import rs.raf.kids.core.Property;
import rs.raf.kids.core.Res;
import rs.raf.kids.job.JobQueue;
import rs.raf.kids.job.ScanType;
import rs.raf.kids.log.Log;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

class DirectoryCrawler extends AbstractCrawler {

    private static class Metadata {

        private final String lastModified;

        public Metadata(String lastModified) {
            this.lastModified = lastModified;
        }

        @Override
        public boolean equals(Object obj) {
            if(obj instanceof Metadata) {
                Metadata m = (Metadata) obj;
                return m.lastModified.equals(lastModified);
            }

            return false;
        }
    }

    private Map<String, Metadata> metadata;
    private final long DIR_SLEEP_MAX;

    DirectoryCrawler(JobQueue jobQueue) {
        super(jobQueue);

        metadata = new HashMap<>();
        DIR_SLEEP_MAX = Long.parseLong(Property.DIR_CRAWLER_SLEEP_TIME.get());
    }

    @Override
    protected void crawl(String path) {
        while(!Thread.currentThread().isInterrupted()) {
            File root = new File(path);

            if(!root.exists()) {
                Log.e(String.format(Res.FORMAT_ERROR, Res.ERROR_ADD_DIRECTORY, path));
                return;
            }

            for(File dir : extractCorpora(root, new Stack<>())) {
                if(dir.getName().startsWith(Property.FILE_CORPUS_PREFIX.get())) {
                    addJobs(dir);
                }
            }

            try {
                Thread.sleep(DIR_SLEEP_MAX);
            }catch(InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private Stack<File> extractCorpora(File root, Stack<File> dirs) {
        for(File file : root.listFiles()) {
            if(file.isDirectory()) {
                dirs.push(file);
                extractCorpora(file, dirs);
            }
        }

        return dirs;
    }

    private void addJobs(File dir) {
        DateFormat df = new SimpleDateFormat(Res.FORMAT_DATE);

        for(File file : dir.listFiles()) {
            String path = file.getAbsolutePath();
            String lastModified = df.format(file.lastModified());
            Metadata metadata = new Metadata(lastModified);

            if(metadata.equals(this.metadata.get(path))) {
                continue;
            }

            this.metadata.put(path, metadata);

            addJob(path, ScanType.FILE);
        }
    }
}
