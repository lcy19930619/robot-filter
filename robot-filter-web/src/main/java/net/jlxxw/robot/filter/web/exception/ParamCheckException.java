package net.jlxxw.robot.filter.web.exception;

/**
 * @author chunyang.leng
 * @date 2022-11-09 5:59 PM
 */
public class ParamCheckException extends RuntimeException {

    /**
     * Constructs a new runtime exception with {@code null} as its
     * detail message.  The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     */
    public ParamCheckException() {
        super();
    }

    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public ParamCheckException(String message) {
        super(message);

    }
}
