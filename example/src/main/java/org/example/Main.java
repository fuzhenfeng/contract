package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {

    private final static Logger log = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        try {
            new Boot(args);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
