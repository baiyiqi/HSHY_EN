package Log;

import Property.Property;

import java.io.File;
import java.io.IOException;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * Created by yiqibai on 12/23/15.
 */
public class Logs {

    private Logger KWBloger;
    private Property Logproperties;



    public Logs(String projectname, Property logproperty){
        KWBloger = Logger.getLogger(String.format( "[  %s  ]", projectname));
        Logproperties = logproperty;

        logsetup();
    }



    /**
     * configure the log property
     */
    private void logsetup(){
        //create log file
        String pattern = Logproperties
                .getProperty("java.util.logging.FileHandler.pattern");
        String logFilePath = pattern.substring(0, pattern.lastIndexOf("/"));
        File file = new File(logFilePath);
        if (!file.exists()) {
            file.mkdirs();
        }

        //load configuration
        LogManager logManager = LogManager.getLogManager();
        try {
            logManager.readConfiguration(Thread.currentThread().getContextClassLoader().getResourceAsStream(Logproperties.getFile()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    public Logger getKWBloger(){
        return KWBloger;
    }



    public void info(String msg){
        KWBloger.info(msg);
    }



    public void server(String msg){
        KWBloger.severe(msg);
    }

}
