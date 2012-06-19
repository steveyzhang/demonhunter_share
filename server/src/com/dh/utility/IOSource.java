package com.dh.utility;

import java.io.*;

import java.net.*;

import com.dh.InvalidIOSourceException;

public class IOSource {
    private String source;

    /**
     * return InputStream
     *
     */

    public IOSource(String source) {
        this.source = source;
    }

    /**
     * return InputStream
     */

    public InputStream getInputStream() throws InvalidIOSourceException {
        // try file first..
        try {
            File file = new File(source);
            if (file.exists()) {
                return (new FileInputStream(file));
            }
            
            //if there is not existed in local file system, try the URL secondly
            URL url = new URL(source);
            InputStream iStream = url.openStream();
            return (iStream);
        } catch (IOException e) {
            throw new InvalidIOSourceException("Can't open the stream to " + source);
        }
    }
    
    public OutputStream getOutputStream() throws InvalidIOSourceException {
        // try file first..
        try {
            File file = new File(source);
            if (file.exists()) {
                return (new FileOutputStream(file));
            }
            return null;
            
        } catch (IOException e) {
            throw new InvalidIOSourceException("Can't open the stream to " + source);
        }
    }
    
}
