package rs.raf.kids.core;

public class Res {

    public static final String PATH_PROPERTIES = "./res/app.properties";
    public static final String PATH_LOG = "./out/log.txt";

    public static final String ERROR_LOAD_PROPERTIES = "Cannot load properties";
    public static final String ERROR_PARSE_COMMAND = "Cannot parse command";
    public static final String ERROR_ADD_DIRECTORY = "Cannot add directory";
    public static final String ERROR_ADD_WEB = "Cannot add web page";
    public static final String ERROR_HOP_COUNT = "Hop count property is not a number";

    public static final String INPUT_COMMAND = "Enter command";

    public static final String INFO_START_THREADS = "Starting threads";
    public static final String INFO_STOP_THREADS = "Stopping threads";
    public static final String INFO_ADD_DIRECTORY = "Adding directory";
    public static final String INFO_ADD_WEB = "Add web page";
    public static final String INFO_GET_RESULT_SYNC = "Getting result synchronously";
    public static final String INFO_GET_RESULT_ASYNC = "Getting result asynchronously";
    public static final String INFO_CLEAR_SUMMARY_FILE = "Clearing file summary";
    public static final String INFO_CLEAR_SUMMARY_WEB = "Clearing web summary";

    public static final String TITLE_ERROR = "ERROR";
    public static final String TITLE_INFO = "INFO";
    public static final String TITLE_DEBUG = "DEBUG";

    public static final String FORMAT_DATE = "MM/dd/yyyy HH:mm:ss";
    public static final String FORMAT_LOG = "[%s] %s";
    public static final String FORMAT_INPUT = ">>> ";
    public static final String FORMAT_URL = "(?<=href=\")https?://[^\"]*(?=\")";

    public static final String CMD_ADD_DIRECTORY = "ad";
    public static final String CMD_ADD_WEB = "aw";
    public static final String CMD_GET_RESULT_SYNC = "get";
    public static final String CMD_GET_RESULT_ASYNC = "query";
    public static final String CMD_CLEAR_FILE_SUMMARY = "cfs";
    public static final String CMD_CLEAR_WEB_SUMMARY = "cws";
    public static final String CMD_STOP = "stop";

    public static final int CONST_JOB_QUEUE_SIZE = 10;
}
