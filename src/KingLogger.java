import java.io.IOException;
import java.util.Formatter;
import java.util.logging.*;

// How To use our cute Logger:
// In the place where you want to put some logging in the code, just write -
//
// KingLogger.logEvent/logError(Level.WARNING,"this is a warning logger");
//
// the logging levels are (from the highest to lowest): SEVERE, WARNING, INFO, CONFIG, FINE, FINER, FINEST.

class KingLogger {
    private static Logger Eventlogger;
    private static Handler EventHandler;
    private static SimpleFormatter fm1;

    private static Logger Errorlogger;
    private static Handler ErrorHandler;
    private static SimpleFormatter fm2;

    private static Logger getLogger(String name){
        if(name.equals("Event")) {   //Event
            if (KingLogger.Eventlogger == null) {
                try {
                    Eventlogger = Logger.getLogger(name);
                    EventHandler = new FileHandler("eventLog.txt");
                    fm1 = new SimpleFormatter();
                    EventHandler.setFormatter(fm1);
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
                    ErrorHandler = new FileHandler("errorLog.txt");
                    fm2 = new SimpleFormatter();
                    ErrorHandler.setFormatter(fm2);
                    Errorlogger.addHandler(ErrorHandler);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return Errorlogger;
        }
    }

    public static void logEvent(Level level, String msg){
        getLogger("Event").log(level, msg);
    }

    public static void logError(Level level, String msg){
        getLogger("Error").log(level, msg);
    }
}
