package rs.raf.kids.core;

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import rs.raf.kids.log.Log;
import rs.raf.kids.thread.Commander;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class App {

    public void loadProperties() {
        Scanner s = null;

        try {
            s = new Scanner(new File(Res.PATH_PROPERTIES));

            while(s.hasNextLine()) {
                String line = s.nextLine().trim();

                if(line.startsWith("#") || line.length() == 0) {
                    continue;
                }

                String[] keyValue = line.split("=");

                String key = keyValue[0].trim().toUpperCase();
                String value = keyValue[1].trim();

                Property property = Property.valueOf(key);
                property.set(value);
            }

            for(Property p : Property.values()) {
                Log.i(p.toString());
            }
        }catch(IOException e) {
            Log.e(Res.ERROR_LOAD_PROPERTIES);
        }finally{
            if(s != null) {
                s.close();
            }
        }
    }

    public void parseCommands() {
        Commander commander = new Commander();

        Scanner s = new Scanner(System.in);

        while(true) {
            Log.input(Res.INPUT_COMMAND);

            String line = s.nextLine().trim();
            String[] tokens = line.split(" ");

            String command = tokens[0];
            String param = null;

            if(tokens.length == 2) {
                param = tokens[1];
            }

            switch(command) {
                case "ad":
                    commander.addDirectory(param);
                    break;
                case "aw":
                    commander.addWeb(param);
                    break;
                case "get":
                    commander.getResultSync(param);
                    break;
                case "query":
                    commander.getResultAsync(param);
                    break;
                case "cfs":
                    commander.clearSummaryFile();
                    break;
                case "cws":
                    commander.clearSummaryWeb();
                    break;
                case "stop":
                    s.close();
                    return;
                default:
                    Log.e(Res.ERROR_PARSE_COMMAND);
                    break;
            }
        }
    }
}
