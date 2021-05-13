package app;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ServentMultiple {

    private static final String TEST_DIR = "res/test";
    private static final String OUT_DIR = "out/production/SillyGit";

    public static void main(String[] args) {
        List<Process> servents = new ArrayList<>();

        Config.load(TEST_DIR + "/app.properties");

        for (int i = 0; i <= Config.SERVENT_COUNT; i++) {
            try {
                String dir = "servent" + i;

                if (i == 0) {
                    dir = "bootstrap";
                }

                ProcessBuilder builder = new ProcessBuilder("java", "-cp", OUT_DIR, "app.ServentSingle",
                        TEST_DIR + "/app.properties", String.valueOf(i));

                builder.redirectOutput(new File(TEST_DIR + "/output/" + dir + "_out.txt"));
                builder.redirectError(new File(TEST_DIR + "/error/" + dir + "_err.txt"));

                if (dir.contains("servent")) {
                    builder.redirectInput(new File(TEST_DIR + "/input/" + dir + "_in.txt"));
                }

                servents.add(builder.start());

                if (i > 0) {
                    App.print(String.format("Started servent %d/%d", i, Config.SERVENT_COUNT));

                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            } catch (IOException e) {
                App.error(String.format("Error while starting servents (%s)", e.getMessage()));
            }
        }

        for (Process process : servents) {
            try {
                process.waitFor();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        App.print("All servents stopped");
    }
}
