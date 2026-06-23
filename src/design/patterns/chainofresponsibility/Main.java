package design.patterns.chainofresponsibility;

public class Main {
    public static void main(String[] args) {
        LogManager log = new InfoLogManager(new DebugLogManager(new ErrorLogManager(null)));


        log.processLog(LogLevel.ERROR, "Error processing request");
        log.processLog(LogLevel.DEBUG, "DB Transaction started");
        log.processLog(LogLevel.INFO, "Traffic is spiking");
    }
}
