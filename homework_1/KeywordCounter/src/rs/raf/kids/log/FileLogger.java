package rs.raf.kids.log;

import rs.raf.kids.core.Res;

import java.io.*;

class FileLogger implements Logger {

    @Override
    public void log(String message, boolean breakLine) {
        PrintWriter writer = null;

        try {
            writer = new PrintWriter(new FileWriter(Res.PATH_LOG, true));
            writer.print(message);

            if(breakLine) {
                writer.println();
            }
        }catch(IOException e) {
            e.printStackTrace();
        }finally {
            if(writer != null) {
                writer.close();
            }
        }
    }
}
