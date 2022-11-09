package net.jlxxw.robot.filter.core.black;

import java.util.HashSet;
import java.util.Set;
import net.jlxxw.robot.filter.config.properties.filter.RuleProperties;
import net.jlxxw.robot.filter.core.lru.Time;

/**
 * @author chunyang.leng
 * @date 2022-11-09 4:31 PM
 */
public class ClientInfo implements Time {
    long createTime = System.currentTimeMillis();

    /**
     * other Identities
     * for example ip or client id
     */
    public Set<String> otherIdentities = new HashSet<>();

    private RuleProperties rule;
    /**
     * gai this creates time milliseconds
     *
     * @return
     */
    @Override
    public long getCreateTime() {
        return createTime;
    }
    public void addIdentities(String identity) {
        otherIdentities.add(identity);
    }

    public Set<String>  getOtherIdentities(){
        return otherIdentities;
    }

    public int countOtherIdentities(){
        return otherIdentities.size();
    }

    public RuleProperties getRule() {
        return rule;
    }

    public void setRule(RuleProperties rule) {
        this.rule = rule;
    }
}
