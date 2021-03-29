package rs.raf.kids;

import rs.raf.kids.core.App;

public class Main {

    public static void main(String[] args) {
        App app = new App();
        app.loadProperties();
        app.startThreads();
        app.parseCommands();
    }
}
