package rs.raf.kids.core;

import rs.raf.kids.log.Log;
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
        commander.startThreads();

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

            int totalParams = tokens.length - 1;

            switch(command) {
                case Res.CMD_ADD_DIRECTORY:
                    commander.addDirectory(param, totalParams);
                    break;
                case Res.CMD_ADD_WEB:
                    commander.addWeb(param, totalParams);
                    break;
                case Res.CMD_GET_RESULT_SYNC:
                    commander.getResultSync(param, totalParams);
                    break;
                case Res.CMD_GET_RESULT_ASYNC:
                    commander.getResultAsync(param, totalParams);
                    break;
                case Res.CMD_CLEAR_FILE_SUMMARY:
                    commander.clearSummaryFile(totalParams);
                    break;
                case Res.CMD_CLEAR_WEB_SUMMARY:
                    commander.clearSummaryWeb(totalParams);
                    break;
                case Res.CMD_STOP:
                    commander.stopThreads();
                    s.close();
                    return;
                default:
                    Log.e(Res.ERROR_PARSE_COMMAND);
                    break;
            }
        }
    }
}
