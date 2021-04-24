package app;

import servent.snapshot.CausalBroadcastShared;
import servent.snapshot.SnapshotType;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class AppConfig {

    public static List<Servent> SERVENTS;
    public static Servent LOCAL_SERVENT;
    public static int SERVENT_COUNT;
    public static SnapshotType SNAPSHOT_TYPE;

    public static Properties readConfig(String configPath) {
        Properties properties = new Properties();

        try {
            properties.load(new FileInputStream(new File(configPath)));
        } catch (IOException e) {
            error("Cannot open properties file");
            System.exit(0);
        }

        SERVENT_COUNT = Integer.parseInt(properties.getProperty("servent_count"));
        SNAPSHOT_TYPE = SnapshotType.valueOf(properties.getProperty("snapshot").toUpperCase());

        return properties;
    }

    public static void readConfig(String configPath, int localServent) {
        Properties properties = readConfig(configPath);

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

        LOCAL_SERVENT = SERVENTS.get(localServent);

        CausalBroadcastShared.initializeVectorClock();
    }

    public static void print(String message) {
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        Date now = new Date();

        System.out.println(timeFormat.format(now) + " - " + message);
    }

    public static void error(String message) {
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        Date now = new Date();

        System.err.println(timeFormat.format(now) + " - " + message);
    }
}
