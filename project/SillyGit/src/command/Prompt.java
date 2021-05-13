package command;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public class Prompt {

    private Set<Option> options;

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

    public static class Option {

        private String name;
        private Handler handler;

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

    public interface Handler {

        void handle();
    }
}
