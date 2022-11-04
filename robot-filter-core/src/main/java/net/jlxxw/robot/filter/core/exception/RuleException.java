package net.jlxxw.robot.filter.core.exception;

import net.jlxxw.robot.filter.config.properties.RuleProperties;

/**
 * @author chunyang.leng
 * @date 2022-11-03 10:01 AM
 */
public class RuleException extends RuntimeException{

    private final RuleProperties ruleProperties;
    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public RuleException(String message,RuleProperties ruleProperties) {
        super(message);
        this.ruleProperties = ruleProperties;
    }

    public RuleProperties getRuleProperties() {
        return ruleProperties;
    }
}
