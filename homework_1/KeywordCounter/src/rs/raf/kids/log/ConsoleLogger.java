package rs.raf.kids.log;

import java.io.PrintStream;

class ConsoleLogger implements Logger {

    @Override
    public void log(String message, boolean breakLine, boolean error) {
        PrintStream ps = error ? System.err : System.out;

        ps.print(message);

        if(breakLine) {
            ps.println();
        }
    }
}
