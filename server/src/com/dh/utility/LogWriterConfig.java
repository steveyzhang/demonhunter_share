package com.dh.utility;

import java.io.*;


public class LogWriterConfig {

    private PrintWriter logWriter;
    private DHLogger logger;

    public LogWriterConfig() {
    }

    public void setPrintWriter(PrintWriter p) {
        logWriter = p;
    }

    public PrintWriter getPrintWriter() {
        return (logWriter);
    }

    public void setLogManager(DHLogger manager) {
        this.logger = manager;
    }

    public DHLogger getLogManager() {
        return (logger);
    }
}