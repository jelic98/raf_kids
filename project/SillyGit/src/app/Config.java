package app;

import file.Files;
import servent.System;
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
    public static Servent BOOTSTRAP;
    public static Servent LOCAL;
    public static System SYSTEM;
    public static Files WORKSPACE;
    public static Files STORAGE;
    public static Random RANDOM = new Random(1);

    public static Properties load(String path) {
        Properties properties = new Properties();

        try {
            properties.load(new FileInputStream(new File(path)));
        } catch (IOException e) {
            App.error("Cannot open properties file");
            java.lang.System.exit(1);
        }

        STORAGE_PATH = properties.getProperty("storage_path");
        WORKSPACE_PATH = properties.getProperty("workspace_path");
        K = Integer.parseInt(properties.getProperty("k"));
        SERVENT_COUNT = Integer.parseInt(properties.getProperty("servent_count"));

        String bootstrapHost = properties.getProperty("bootstrap.host");
        int bootstrapPort = Integer.parseInt(properties.getProperty("bootstrap.port"));

        BOOTSTRAP = new Servent(bootstrapHost, bootstrapPort);

        return properties;
    }

    public static void load(String path, int servent) {
        Properties properties = load(path);

        if (servent == 0) {
            LOCAL = BOOTSTRAP;
        } else {
            String serventHost = properties.getProperty("servent" + servent + ".host");
            int serventPort = Integer.parseInt(properties.getProperty("servent" + servent + ".port"));

            LOCAL = new Servent(serventHost, serventPort);
        }

        SYSTEM = new System();
        WORKSPACE = new Files();
        STORAGE = new Files();
    }
}
