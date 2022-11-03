package net.jlxxw.robot.filter.config.rule;

/**
 * every one robot filter rule
 * @author chunyang.leng
 * @date 2022-11-03 10:01 AM
 */
public class Rule {

    /**
     * robot allow pass max qps
     */
    int maxQps = 50;

    /**
     * allow to add blacklisted
     */
    boolean allowAddBlacklisted = true;

    /**
     * time out remove blacklisted
     * unit:second
     */
    long blacklistedTime = 60;

    /**
     * allow remove
     */
    boolean allowRemoveBlacklisted = true;
}
