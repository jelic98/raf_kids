package file;

import java.io.File;
import java.util.LinkedList;
import java.util.Queue;

public class FileHandler {

    public void forEach(File path, Handler<String> handler) {
        if (path.isFile()) {
            handler.handle(path.getPath());
        } else {
            Queue<File> dirs = new LinkedList<>();
            dirs.add(path);

            while (!dirs.isEmpty()) {
                File dir = dirs.poll();

                for (File file : dir.listFiles()) {
                    if (file.isDirectory()) {
                        dirs.add(file);
                    } else {
                        handler.handle(file.getPath());
                    }
                }
            }
        }
    }

    public interface Handler<T> {

        void handle(T path);
    }
}
