package net.jlxxw.robot.filter.data.share.redis.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import net.jlxxw.robot.filter.config.properties.data.redis.RobotFilterRedisProperties;
import net.jlxxw.robot.filter.config.properties.filter.FilterProperties;
import net.jlxxw.robot.filter.config.properties.filter.RuleProperties;
import net.jlxxw.robot.filter.config.properties.filter.servlet.RobotFilterServletFilterProperties;
import net.jlxxw.robot.filter.core.data.DataCore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.util.CollectionUtils;

/**
 * @author chunyang.leng
 * @date 2022-12-05 12:20 PM
 */
public class RobotFilterRedisClient implements DataCore {
    private static final Map<String, RuleProperties> RULE_PROPERTIES_MAP = new HashMap<>();

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private RobotFilterServletFilterProperties robotFilterServletFilterProperties;
    @Autowired
    private RobotFilterRedisProperties redisProperties;

    /**
     * init data store
     */
    @Override
    public void init() {
        List<FilterProperties> filters = robotFilterServletFilterProperties.getFilters();
        if (CollectionUtils.isEmpty(filters)) {
            return;
        }
        for (FilterProperties filter : filters) {
            if (filter.isEnabled()) {
                String filterName = filter.getName();
                List<RuleProperties> rules = filter.getRules();
                rules.forEach(rule -> {
                    String name = rule.getName();
                    String key = getRedisKey(filterName, name);
                    RULE_PROPERTIES_MAP.put(key, rule);
                });

            }
        }
    }

    /**
     * ip pass inc
     *
     * @param filterName filter name
     * @param ruleName   name of the rule
     * @param ip         client ip
     */
    @Override
    public void incIp(String filterName, String ruleName, String ip) {
        String key = getRedisKey(filterName, ruleName);

        RuleProperties properties = RULE_PROPERTIES_MAP.get(key);
        if (Objects.isNull(properties)) {
            // should not exist
            return;
        }

        long startInterval = properties.getInterval();

        ZSetOperations<String, String> operations = redisTemplate.opsForZSet();

        long currentTimeMillis = System.currentTimeMillis();
        String value = UUID.randomUUID().toString();
        // remove start interval
        operations.removeRange(key + ":ip:" + ip, 0, currentTimeMillis - startInterval);

        operations.add(key + ":ip:" + ip, value, currentTimeMillis);

        // todo expire
        // redisTemplate.expire
    }

    /**
     * client id pass inc
     *
     * @param filterName filter name
     * @param ruleName   name of the rule
     * @param clientId   client id
     */
    @Override
    public void incClientId(String filterName, String ruleName, String clientId) {
        String key = getRedisKey(filterName, ruleName);

        RuleProperties properties = RULE_PROPERTIES_MAP.get(key);

        if (Objects.isNull(properties)) {
            // should not exist
            return;
        }

        long startInterval = properties.getInterval();

        ZSetOperations<String, String> operations = redisTemplate.opsForZSet();

        long currentTimeMillis = System.currentTimeMillis();

        // remove start interval
        operations.removeRange(key + ":clientId:" + clientId, 0, currentTimeMillis - startInterval);

        String value = UUID.randomUUID().toString();

        operations.add(key + ":clientId:" + clientId, value, currentTimeMillis);

        // todo expire
        // redisTemplate.expire
    }

    /**
     * allow pass ?
     *
     * @param filterName filter name
     * @param ruleName   rule name
     * @param ip         client ip
     * @return true is pass
     */
    @Override
    public boolean allowIp(String filterName, String ruleName, String ip) {
        String key = getRedisKey(filterName, ruleName);

        RuleProperties properties = RULE_PROPERTIES_MAP.get(key);
        if (Objects.isNull(properties)) {
            // should not exist
            return true;
        }

        long startInterval = properties.getInterval();

        ZSetOperations<String, String> operations = redisTemplate.opsForZSet();

        long currentTimeMillis = System.currentTimeMillis();

        long start = currentTimeMillis - startInterval;
        Long count = operations.count(key + ":ip", start, currentTimeMillis);
        return count != null && count < properties.getMaxAllow();
    }

    /**
     * allow pass ?
     *
     * @param filterName filter name
     * @param ruleName   rule name
     * @param clientId   client id
     * @return true is pass
     */
    @Override
    public boolean allowClientId(String filterName, String ruleName, String clientId) {
        String key = getRedisKey(filterName, ruleName);

        RuleProperties properties = RULE_PROPERTIES_MAP.get(key);
        if (Objects.isNull(properties)) {
            // should not exist
            return true;
        }

        long startInterval = properties.getInterval();

        ZSetOperations<String, String> operations = redisTemplate.opsForZSet();

        long currentTimeMillis = System.currentTimeMillis();

        long start = currentTimeMillis - startInterval;
        Long count = operations.count(key + ":ClientId", start, currentTimeMillis);
        return count != null && count < properties.getMaxAllow();
    }

    /**
     * add to temp list
     *
     * @param ip
     * @param ruleProperties
     */
    @Override public void addIpToTempBlackList(String ip, RuleProperties ruleProperties) {
        long time = ruleProperties.getBlacklistedTime();
    }

    /**
     * add to temp list
     *
     * @param ip
     * @param ruleProperties
     */
    @Override public void addClientIdToTempBlackList(String ip, RuleProperties ruleProperties) {
        long time = ruleProperties.getBlacklistedTime();


    }

    /**
     * check ip
     *
     * @param ip
     * @return
     */
    @Override public boolean checkIpInTempBlackList(String ip) {
        return false;
    }

    /**
     * check client id
     *
     * @param clientId
     * @return
     */
    @Override public boolean checkClientIdInTempBlackList(String clientId) {
        return false;
    }

    /**
     * create one  prefix
     *
     * @param fileName fileName
     * @param ruleName ruleName
     * @return redis key prefix
     */
    private String getRedisKey(String fileName, String ruleName) {
        String prefix = redisProperties.getPrefix();
        return prefix + "filter:" + fileName + ":rule:" + ruleName;
    }

}
