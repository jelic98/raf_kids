package rs.raf.kids.crawler;

import rs.raf.kids.core.Property;
import rs.raf.kids.core.Res;
import rs.raf.kids.job.Job;
import rs.raf.kids.job.JobQueue;
import rs.raf.kids.log.Log;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

class DirectoryCrawler implements PathCrawler {

    private static class Metadata {

        private final String lastModified;

        public Metadata(String lastModified) {
            this.lastModified = lastModified;
        }

        public String getLastModified() {
            return lastModified;
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

    private JobQueue jobQueue;
    private Map<String, Metadata> metadata;

    public DirectoryCrawler(JobQueue jobQueue) {
        this.jobQueue = jobQueue;

        metadata = new HashMap<>();
    }

    @Override
    public void addPath(String path, Job.ScanType scanType) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                scanDirectory(path);
            }
        }).start();
    }

    private void scanDirectory(String path) {
        File root = new File(path);

        if(!root.exists()) {
            Log.e(Res.ERROR_ADD_DIRECTORY);
            return;
        }

        for(File dir : extractCorpora(root, new Stack<>())) {
            if(dir.getName().startsWith(Property.FILE_CORPUS_PREFIX.get())) {
                addJobs(dir);
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

            try {
                jobQueue.enqueue(new Job(path, Job.ScanType.FILE));
            }catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
