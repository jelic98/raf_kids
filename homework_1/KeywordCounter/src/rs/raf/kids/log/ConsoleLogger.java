package rs.raf.kids.log;

class ConsoleLogger implements Logger {

    @Override
    public void log(String message, boolean breakLine) {
        System.out.print(message);

        if(breakLine) {
            System.out.println();
        }
    }
}
