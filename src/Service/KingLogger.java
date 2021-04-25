package Service;

import java.io.IOException;
import java.util.Date;
import java.util.logging.*;

// How To use our cute Logger:
// In the place where you want to put some logging in the code, just write -
//
// Service.KingLogger.logEvent/logError("this is a log");
//
// the logging levels are (from the highest to lowest): SEVERE, WARNING, INFO, CONFIG, FINE, FINER, FINEST.

public class KingLogger {
    private static Logger Eventlogger;
    private static Handler EventHandler;
    //private static SimpleFormatter fm1;

    private static Logger Errorlogger;
    private static Handler ErrorHandler;
    //private static SimpleFormatter fm2;

    private static Logger getLogger(String name){
        if(name.equals("Event")) {   //Event
            if (KingLogger.Eventlogger == null) {
                try {
                    Eventlogger = Logger.getLogger(name);
                    EventHandler = new FileHandler("eventLog.txt", true);
                    //fm1 = new SimpleFormatter();

                    EventHandler.setFormatter(new SimpleFormatter() {
                        private static final String format = "[%1$tF] [%1$tT] [%2$-4s] %3$s %n";

                        @Override
                        public synchronized String format(LogRecord lr) {
                            return String.format(format,
                                    new Date(lr.getMillis()),
                                    lr.getLevel().getLocalizedName(),
                                    lr.getMessage()
                            );
                        }
                    });


                    Eventlogger.addHandler(EventHandler);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return Eventlogger;
        }
        else{  //Error
            if (KingLogger.Errorlogger == null) {
                try {
                    Errorlogger = Logger.getLogger(name);
                    ErrorHandler = new FileHandler("errorLog.txt", true);
                    //fm2 = new SimpleFormatter();


                    ErrorHandler.setFormatter(new SimpleFormatter() {
                        private static final String format = "[%1$tF] [%1$tT] [%2$-7s] %3$s %n";

                        @Override
                        public synchronized String format(LogRecord lr) {
                            return String.format(format,
                                    new Date(lr.getMillis()),
                                    lr.getLevel().getLocalizedName(),
                                    lr.getMessage()
                            );
                        }
                    });


                    Errorlogger.addHandler(ErrorHandler);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return Errorlogger;
        }
    }

    public static void logEvent(String msg){
        getLogger("Event").log(Level.INFO, msg + "\n");
    }

    public static void logError(String msg){
        getLogger("Error").log(Level.WARNING, msg + "\n");

    }

    public static void main(String [] args){
        KingLogger.logEvent("this is log event");
        KingLogger.logError("this is log error");
    }

}
