package app;

import file.FileManager;
import servent.System;
import servent.Servent;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

public class Config {

    public static String STORAGE_PATH;
    public static String WORKSPACE_PATH;
    public static int SERVENT_COUNT;
    public static int CHORD_SIZE;
    public static Servent BOOTSTRAP;
    public static Servent LOCAL;
    public static List<Servent> ACTIVE_SERVENTS;
    public static System SYSTEM;
    public static FileManager FILES;
    public static Random RANDOM = new Random(1);

    public static Properties load(String path) {
        Properties properties = new Properties();

        try {
            properties.load(new FileInputStream(new File(path)));
        } catch (IOException e) {
            App.error("Cannot open properties file");
            java.lang.System.exit(0);
        }

        STORAGE_PATH = properties.getProperty("storage_path");
        WORKSPACE_PATH = properties.getProperty("workspace_path");
        SERVENT_COUNT = Integer.parseInt(properties.getProperty("servent_count"));
        CHORD_SIZE = Integer.parseInt(properties.getProperty("chord_size"));

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

        ACTIVE_SERVENTS = new ArrayList<>();
        SYSTEM = new System();
        FILES = new FileManager();
    }
}
