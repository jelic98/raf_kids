package app;

import command.CommandParser;
import message.HailAskMessage;
import message.MessageHandler;
import message.MessageListener;

public class ServentSingle {

    private static MessageListener listener;
    private static CommandParser parser;

    public static void main(String[] args) {
        int servent = Integer.parseInt(args[1]);

        Config.load(args[0], servent);

        Servent bootstrap = new Servent(Config.BOOTSTRAP_HOST, Config.BOOTSTRAP_PORT);

        if(servent > 0) {
            App.print("Starting servent " + Config.LOCAL_SERVENT);
        }else {
            Config.LOCAL_SERVENT = bootstrap;

            App.print("Starting bootstrap server " + Config.LOCAL_SERVENT);
        }

        new Thread(new MessageHandler()).start();

        listener = new MessageListener();
        new Thread(listener).start();

        if(servent > 0) {
            parser = new CommandParser();
            new Thread(parser).start();

            App.send(new HailAskMessage(bootstrap, Config.LOCAL_SERVENT.getAddress()));
        }
    }

    public static void stop() {
        App.print("Stopping");

        listener.stop();
        parser.stop();
    }
}
