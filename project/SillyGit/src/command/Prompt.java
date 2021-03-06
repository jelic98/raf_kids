package command;

import java.util.LinkedHashSet;
import java.util.Set;

public class Prompt {

    private final Set<Option> options;

    public Prompt() {
        options = new LinkedHashSet<>();
    }

    public Prompt add(Option option) {
        options.add(option);

        return this;
    }

    public boolean select(String name) {
        for (Option option : options) {
            if (option.getName().equals(name)) {
                option.getHandler().handle();
                return true;
            }
        }

        return false;
    }

    @Override
    public String toString() {
        return "Options: " + options;
    }

    public interface Handler {

        void handle();
    }

    public static class Option {

        private final String name;
        private final Handler handler;

        public Option(String name, Handler handler) {
            this.name = name;
            this.handler = handler;
        }

        public String getName() {
            return name;
        }

        public Handler getHandler() {
            return handler;
        }

        @Override
        public String toString() {
            return getName();
        }
    }
}
