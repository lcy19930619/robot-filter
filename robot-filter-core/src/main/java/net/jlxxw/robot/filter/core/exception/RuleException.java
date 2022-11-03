package net.jlxxw.robot.filter.core.exception;

/**
 * @author chunyang.leng
 * @date 2022-11-03 10:01 AM
 */
public class RuleException extends RuntimeException{
    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public RuleException(String message) {
        super(message);
    }
}
