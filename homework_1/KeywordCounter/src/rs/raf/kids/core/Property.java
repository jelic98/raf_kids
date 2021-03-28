package rs.raf.kids.core;

public enum Property {
    KEYWORDS,
    FILE_CORPUS_PREFIX,
    DIR_CRAWLER_SLEEP_TIME,
    FILE_SCANNING_SIZE_LIMIT,
    HOP_COUNT,
    URL_REFRESH_TIME;

    private String value;

    public String get() {
        return value;
    }

    public void set(String value) {
        if(this.value == null && value != null) {
            this.value = value;
        }
    }

    @Override
    public String toString() {
        return super.toString() + " = " + get();
    }
}
