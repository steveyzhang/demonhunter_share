package com.dh.utility;


import java.io.*;
import java.util.*;

public class DHLogger {
	//Flag
    public static final String WARNNING = "WARNNING";
    public static final String ERROR = "ERROR";
    public static final String INFO = "INFO";
    
    //Preset Package Name
    public static final String PKG_POSTER = "Poster";
    
	private  boolean alreadyInit = false;
    String project_name = "";
    
    private static DHLogger logger = null;
    
    //log file definition
    private  LogWriterConfig logConfig = null;
    private  PrintWriter 	logWriter = null;
    private  String 	 	logName = "/Users/steve/WorkShop/demonhunter_server/log.txt";

    private StringBuffer mPreStr = new StringBuffer(1024*10);
    
    // flag for turn on/off log writing.
    private  boolean logActive = true;

    // flag for turn on/off timestamp
    private  boolean timeStampActive = false;

    // flag for turn on/off thread name activation. default is false..
    // if it's turn on message format would convert to millisecond time: thread name: message
    private  boolean threadInfoActive = false;

    // flag for turn on synchronized messaging.
    private  boolean syncMsgActive = false;

    // flag for turn on/off System.out.println
    private  boolean outputToSystem = true;

    private DHLogger( ) {
        init();
    }

    static public void log( String flag, String pkgName , String message )
    {
    	DHLogger logIns = getInstance();
    	logIns.out(flag, pkgName, message);
    }
    
    static public DHLogger getInstance()
    {
    	if( logger == null)
    	{
    		logger = new DHLogger();
    	}
    	
    	return logger;
    }
    
    public void init() {
        if (!alreadyInit) {
            // get log file name
            try {
                File logFile = new File(logName);
                if (logFile.exists()) {

                    File f = new File(logName);

                    int i = 0;
                    while (f.exists()) {

                        i++;
                        f = new File(logName + "." + i);
                    }

                    if (!logFile.renameTo(new File(logName + "." + i))) {

                        System.out.println("Log file already exists, could not rename to " + logName + "." + i);
                    }

                    logFile = new File(logName);

                }
                logWriter = new PrintWriter(new BufferedWriter(new FileWriter(logFile)));
                alreadyInit = true;
            } catch (IOException e) {
                System.out.println("Log file not found: " + logName);
            } catch (Exception npe) {
                npe.printStackTrace();
                System.out.println("ERROR: Seems like you try to invoke this LogManager outside Create Bean system.");
            }
        }
    }

    public void cleanUp() {
    }

    protected void writeLog(String logmsg) {
        if (logActive) {
            String logMsg;

            if (threadInfoActive) {
                logMsg = (new Date()).getTime() + ": " + Thread.currentThread() + ": " + logmsg;
            } else if (timeStampActive) {
                logMsg = new Date() + ": " + logmsg;
            } else {
                logMsg = logmsg;
            }

            if (outputToSystem)
                System.out.println(logmsg);

            logWriter.println(logmsg);
            logWriter.flush();

        }
    }

    public void out(String flag, String pkgName, String message) {
    	mPreStr.replace(0, mPreStr.length(), String.format("[%s]@%s: %s", flag , pkgName , message));
        writeLog( mPreStr.toString() );
    }
}


