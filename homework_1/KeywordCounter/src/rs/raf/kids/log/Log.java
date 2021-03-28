package rs.raf.kids.log;

public class Log {

    private static final Logger[] loggers = new Logger[] {
            new ConsoleLogger(),
            new FileLogger()
    };

    public static void e(String message) {
        log("ERROR", message, true);
    }

    public static void i(String message) {
        log("INFO", message, true);
    }

    public static void d(String message) {
        log("DEBUG", message, true);
    }

    public static void input(String message) {
        log(message, ">>> ", false);
    }

    private static void log(String tag, String message, boolean breakLine) {
        for(Logger logger : loggers) {
            logger.log(String.format("[%s] %s", tag, message), breakLine);
        }
    }
}
