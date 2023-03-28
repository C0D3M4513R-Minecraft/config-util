package com.c0d3m4513r;

import com.c0d3m4513r.logger.Logging;
import com.c0d3m4513r.logger.Slf4jLogger;
import org.apache.log4j.BasicConfigurator;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;

public final class LoggerUtils {
    public static void initLogger(@NonNull final String testName) {
        System.out.println("Initializing logger for test: " + testName);
        BasicConfigurator.configure();
        Logging.setConfigLogger(new Slf4jLogger<>(org.slf4j.LoggerFactory.getLogger(testName), Logger.class));
    }
}
