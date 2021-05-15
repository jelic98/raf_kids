package app;

import file.Files;
import servent.Network;
import servent.Servent;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;

public class Config {

    public static String STORAGE_PATH;
    public static String WORKSPACE_PATH;
    public static int K;
    public static int SERVENT_COUNT;
    public static int TEAM_LIMIT;
    public static long FAILURE_SOFT;
    public static long FAILURE_HARD;
    public static long REPLICATE_INTERVAL;
    public static Servent BOOTSTRAP;
    public static Servent LOCAL;
    public static Network NETWORK;
    public static Files WORKSPACE;
    public static Files STORAGE;
    public static final Random RANDOM = new Random(1);

    public static Properties load(String path) {
        Properties properties = new Properties();

        try {
            properties.load(new FileInputStream(new File(path)));
        } catch (IOException e) {
            App.error("Cannot open properties file");
            System.exit(0);
        }

        STORAGE_PATH = properties.getProperty("storage_path");
        WORKSPACE_PATH = properties.getProperty("workspace_path");
        K = Integer.parseInt(properties.getProperty("k"));
        SERVENT_COUNT = Integer.parseInt(properties.getProperty("servent_count"));
        TEAM_LIMIT = Integer.parseInt(properties.getProperty("team_limit"));
        FAILURE_SOFT = Long.parseLong(properties.getProperty("failure_soft"));
        FAILURE_HARD = Long.parseLong(properties.getProperty("failure_hard"));
        REPLICATE_INTERVAL = Long.parseLong(properties.getProperty("replicate_interval"));

        String bootstrapHost = properties.getProperty("bootstrap.host");
        int bootstrapPort = Integer.parseInt(properties.getProperty("bootstrap.port"));

        BOOTSTRAP = new Servent(bootstrapHost, bootstrapPort, null, 0);

        return properties;
    }

    public static void load(String path, int servent) {
        Properties properties = load(path);

        if (servent == 0) {
            LOCAL = BOOTSTRAP;
        } else {
            String serventHost = properties.getProperty("servent" + servent + ".host");
            int serventPort = Integer.parseInt(properties.getProperty("servent" + servent + ".port"));
            String serventTeam = properties.getProperty("servent" + servent + ".team");

            LOCAL = new Servent(serventHost, serventPort, serventTeam, servent);
        }

        NETWORK = new Network();
        WORKSPACE = new Files();
        STORAGE = new Files();
    }
}
