package app;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ServentMultiple {

    private static final String TEST_DIR = "res/test";
    private static final String OUT_DIR = "out/production/SillyGit";

    public static void main(String[] args) {
        List<Process> servents = new ArrayList<>();

        Config.load(TEST_DIR + "/servent_list.properties");

        App.print("Starting multiple servents - Type \"stop\" to exit");

        for (int i = 0; i < Config.SERVENT_COUNT; i++) {
            try {
                ProcessBuilder builder = new ProcessBuilder("java", "-cp", OUT_DIR, "app.ServentSingle",
                        TEST_DIR + "/servent_list.properties", String.valueOf(i));

                builder.redirectOutput(new File(TEST_DIR + "/output/servent" + i + "_out.txt"));
                builder.redirectError(new File(TEST_DIR + "/error/servent" + i + "_err.txt"));
                builder.redirectInput(new File(TEST_DIR + "/input/servent" + i + "_in.txt"));

                servents.add(builder.start());
            } catch (IOException e) {
                App.error("Error while starting servents");
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
