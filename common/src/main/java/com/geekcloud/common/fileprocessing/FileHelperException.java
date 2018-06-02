package com.geekcloud.common.fileprocessing;

public class FileHelperException extends Exception {
    protected FileHelperException () {}
    protected FileHelperException (String message) {
        super(message);
    }
    protected FileHelperException (Exception e) {
        super (e);
    }
}
