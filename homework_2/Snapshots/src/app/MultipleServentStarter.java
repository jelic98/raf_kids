package app;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * This class implements the logic for starting multiple servent instances.
 * <p>
 * To use it, invoke startServentTest with a directory name as parameter.
 * This directory should include:
 * <ul>
 * <li>A <code>servent_list.properties</code> file (explained in {@link AppConfig} class</li>
 * <li>A directory called <code>output</code> </li>
 * <li>A directory called <code>error</code> </li>
 * <li>A directory called <code>input</code> with text files called
 * <code> servent0_in.txt </code>, <code>servent1_in.txt</code>, ... and so on for each servent.
 * These files should contain the commands for each servent, as they would be entered in console.</li>
 * </ul>
 *
 * @author bmilojkovic
 */
public class MultipleServentStarter {

    private static final String TEST_NAME = "res/snapshot";
    private static final String OUT_DIR = "out/production/Snapshots";

    /**
     * The parameter for this function should be the name of a directory that
     * contains a servent_list.properties file which will describe our distributed system.
     */
    private static void startServentTest(String testName) {
        List<Process> serventProcesses = new ArrayList<>();

        AppConfig.readConfig(testName + "/servent_list.properties");

        AppConfig.timestampedStandardPrint("Starting multiple servent runner. "
                + "If servents do not finish on their own, type \"stop\" to finish them");

        for (int i = 0; i < AppConfig.SERVENT_COUNT; i++) {
            try {
                ProcessBuilder builder = new ProcessBuilder("java", "-cp", OUT_DIR, "app.ServentMain",
                        testName + "/servent_list.properties", String.valueOf(i));

                builder.redirectOutput(new File(testName + "/output/servent" + i + "_out.txt"));
                builder.redirectError(new File(testName + "/error/servent" + i + "_err.txt"));
                builder.redirectInput(new File(testName + "/input/servent" + i + "_in.txt"));

                serventProcesses.add(builder.start());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        new Thread(new ServentCLI(serventProcesses)).start();

        for (Process process : serventProcesses) {
            try {
                process.waitFor();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        AppConfig.timestampedStandardPrint("All servent processes finished. Type \"stop\" to exit.");
    }

    public static void main(String[] args) {
        startServentTest(TEST_NAME);
    }

    /**
     * We will wait for user stop in a separate thread.
     * The main thread is waiting for processes to end naturally.
     */
    private static class ServentCLI implements Runnable {

        private final List<Process> serventProcesses;

        public ServentCLI(List<Process> serventProcesses) {
            this.serventProcesses = serventProcesses;
        }

        @Override
        public void run() {
            Scanner sc = new Scanner(System.in);

            while (true) {
                String line = sc.nextLine();

                if (line.equals("stop")) {
                    for (Process process : serventProcesses) {
                        process.destroy();
                    }
                    break;
                }
            }

            sc.close();
        }
    }
}
