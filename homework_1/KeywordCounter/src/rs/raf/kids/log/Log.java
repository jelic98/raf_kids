package rs.raf.kids.log;

import rs.raf.kids.core.Res;

public class Log {

    private static final Logger[] loggers = new Logger[] {
            new ConsoleLogger(),
            new FileLogger()
    };

    public static void e(String message) {
        log(Res.TITLE_ERROR, message, true);
    }

    public static void i(String message) {
        log(Res.TITLE_INFO, message, true);
    }

    public static void d(String message) {
        log(Res.TITLE_DEBUG, message, true);
    }

    public static void input(String message) {
        log(message, Res.FORMAT_INPUT, false);
    }

    private static void log(String tag, String message, boolean breakLine) {
        for(Logger logger : loggers) {
            logger.log(String.format(Res.FORMAT_LOG, tag, message), breakLine);
        }
    }
}
