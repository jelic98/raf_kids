package app;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Config {

    public static int SERVENT_COUNT;
    public static int CHORD_SIZE;
    public static Servent BOOTSTRAP_SERVER;
    public static Servent LOCAL_SERVENT;
    public static List<Servent> SERVENTS;
    public static List<Servent> BOOTSTRAP_SERVENTS;
    public static ChordState CHORD;

    public static Properties load(String path) {
        Properties properties = new Properties();

        try {
            properties.load(new FileInputStream(new File(path)));
        } catch (IOException e) {
            App.error("Cannot open properties file");
            ServentSingle.stop();
        }

        SERVENT_COUNT = Integer.parseInt(properties.getProperty("servent_count"));
        CHORD_SIZE = Integer.parseInt(properties.getProperty("chord_size"));

        String host = properties.getProperty("bootstrap.host");
        int port = Integer.parseInt(properties.getProperty("bootstrap.port"));

        BOOTSTRAP_SERVER = new Servent(host, port);

        return properties;
    }

    public static void load(String path, int servent) {
        Properties properties = load(path);

        CHORD = new ChordState();
        SERVENTS = new ArrayList<>();

        for (int i = 1; i <= SERVENT_COUNT; i++) {
            String host = properties.getProperty("servent" + i + ".host");
            int port = Integer.parseInt(properties.getProperty("servent" + i + ".port"));

            SERVENTS.add(new Servent(host, port));
        }

        if (servent > 0) {
            LOCAL_SERVENT = SERVENTS.get(servent - 1);
        }
    }
}
