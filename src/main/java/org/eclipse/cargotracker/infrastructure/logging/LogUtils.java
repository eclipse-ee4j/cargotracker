package org.eclipse.cargotracker.infrastructure.logging;

import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class for enhanced logging throughout the application.
 */
public class LogUtils {

    /**
     * Log a message at FINE level if the logger is enabled for the FINE level.
     * This avoids constructing the message if the level is not enabled.
     * 
     * @param logger The logger to use
     * @param messageSupplier A supplier function that produces the log message
     */
    public static void fine(Logger logger, Supplier<String> messageSupplier) {
        if (logger.isLoggable(Level.FINE)) {
            logger.fine(messageSupplier.get());
        }
    }

    /**
     * Log a message at FINER level if the logger is enabled for the FINER level.
     * 
     * @param logger The logger to use
     * @param messageSupplier A supplier function that produces the log message
     */
    public static void finer(Logger logger, Supplier<String> messageSupplier) {
        if (logger.isLoggable(Level.FINER)) {
            logger.finer(messageSupplier.get());
        }
    }

    /**
     * Log a message at FINEST level if the logger is enabled for the FINEST level.
     * 
     * @param logger The logger to use
     * @param messageSupplier A supplier function that produces the log message
     */
    public static void finest(Logger logger, Supplier<String> messageSupplier) {
        if (logger.isLoggable(Level.FINEST)) {
            logger.finest(messageSupplier.get());
        }
    }

    /**
     * Log entry to a method at FINER level.
     * 
     * @param logger The logger to use
     * @param className The class name
     * @param methodName The method name
     * @param params Optional parameters to log
     */
    public static void entering(Logger logger, String className, String methodName, Object... params) {
        if (logger.isLoggable(Level.FINER)) {
            logger.entering(className, methodName, params);
        }
    }

    /**
     * Log exit from a method at FINER level.
     * 
     * @param logger The logger to use
     * @param className The class name
     * @param methodName The method name
     * @param result Optional result to log
     */
    public static void exiting(Logger logger, String className, String methodName, Object result) {
        if (logger.isLoggable(Level.FINER)) {
            logger.exiting(className, methodName, result);
        }
    }

    /**
     * Log exit from a method at FINER level.
     * 
     * @param logger The logger to use
     * @param className The class name
     * @param methodName The method name
     */
    public static void exiting(Logger logger, String className, String methodName) {
        if (logger.isLoggable(Level.FINER)) {
            logger.exiting(className, methodName);
        }
    }
}
