package com.wqb.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;

public class Log4jLogger {

    private final Logger logger;

    private Log4jLogger(Class<?> clazz) {
        logger = LoggerFactory.getLogger(clazz);
    }

    public static Log4jLogger getLogger(Class<?> clazz) {
        return new Log4jLogger(clazz);
    }

    public void debug(Object message) {
        logger.debug(String.valueOf(message));
    }

    public void debug(Object message, Throwable t) {
        logger.debug(String.valueOf(message), t);
    }

    public void debug(String pattern, Object... arguments) {
        logger.debug(pattern, arguments);
    }

    public void debug(String pattern, Throwable t, Object... arguments) {
        logger.info(format(pattern, arguments), t);
    }

    public void info(Object message) {
        logger.info(String.valueOf(message));
    }

    public void info(Object message, Throwable t) {
        logger.info(String.valueOf(message), t);
    }

    public void info(String pattern, Object... arguments) {
        logger.info(pattern, arguments);
    }

    public void info(String pattern, Throwable t, Object... arguments) {
        logger.info(format(pattern, arguments), t);
    }


    public void error(Object message) {
        logger.error(String.valueOf(message));
    }

    public void error(Object message, Throwable t) {
        logger.error(String.valueOf(message), t);
    }

    public void error(String pattern, Object... arguments) {
        logger.error(pattern, arguments);
    }

    public void error(String pattern, Throwable t, Object... arguments) {
        logger.error(format(pattern, arguments), t);
    }

    private static String format(String pattern, Object... arguments) {
        return MessageFormat.format(pattern, arguments);
    }
}
