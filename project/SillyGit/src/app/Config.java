package app;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Config {

    public static final int BOOTSTRAP_PORT = 999;
    public static final List<Servent> BOOTSTRAP_SERVENTS = new ArrayList<>();

    public static ChordState CHORD;
    public static List<Servent> SERVENTS;
    public static Servent LOCAL_SERVENT;
    public static int SERVENT_COUNT;

    public static Properties load(String path) {
        Properties properties = new Properties();

        try {
            properties.load(new FileInputStream(new File(path)));
        } catch (IOException e) {
            App.error("Cannot open properties file");
            System.exit(0);
        }

        SERVENT_COUNT = Integer.parseInt(properties.getProperty("servent_count"));

        return properties;
    }

    public static void load(String path, int servent) {
        Properties properties = load(path);

        CHORD = new ChordState();

        SERVENTS = new ArrayList<>();
        List<List<Integer>> globalNeighbors = new ArrayList<>();

        for (int i = 0; i < SERVENT_COUNT; i++) {
            int serventPort = Integer.parseInt(properties.getProperty("servent" + i + ".port"));
            String neighbors = properties.getProperty("servent" + i + ".neighbors");

            List<Integer> localNeighbors = new ArrayList<>();

            for (String neighbor : neighbors.split(",")) {
                localNeighbors.add(Integer.parseInt(neighbor));
            }

            globalNeighbors.add(localNeighbors);

            SERVENTS.add(new Servent(i, "localhost", serventPort));
        }

        for (int i = 0; i < SERVENT_COUNT; i++) {
            List<Servent> neighbors = SERVENTS.get(i).getNeighbors();

            for (Integer neighbor : globalNeighbors.get(i)) {
                neighbors.add(SERVENTS.get(neighbor));
            }
        }

        LOCAL_SERVENT = SERVENTS.get(servent);

        ServentState.initializeVectorClock();
    }
}
