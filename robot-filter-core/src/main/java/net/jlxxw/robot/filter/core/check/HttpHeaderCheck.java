package net.jlxxw.robot.filter.core.check;

import java.util.Set;
import net.jlxxw.robot.filter.config.properties.filter.RuleProperties;
import net.jlxxw.robot.filter.core.exception.RuleException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * @author chunyang.leng
 * @date 2022-11-03 11:35 AM
 */
@Component
public class HttpHeaderCheck {

    private final RuleProperties ruleProperties = new RuleProperties();

    {
        ruleProperties.setContentType("application/json");
        ruleProperties.setHttpResponseCode(400);
    }

    /**
     * check referer
     *
     * @param referer   http referer
     * @param whitelist whitelist
     * @throws RuleException not in whitelist
     */
    public void checkReferer(String referer, Set<String> whitelist) throws RuleException {
        if (StringUtils.isBlank(referer)) {
            throw new RuleException("no referer", ruleProperties);
        }
        if (CollectionUtils.isEmpty(whitelist)) {
            throw new RuleException("unsafe referer:" + referer, ruleProperties);
        }
        for (String white : whitelist) {
            if (referer.contains(white)) {
                return;
            }
        }
        throw new RuleException("unsafe referer:" + referer, ruleProperties);
    }

    /**
     * check origin
     *
     * @param origin    http origin
     * @param whitelist whitelist
     * @throws RuleException not in whitelist
     */
    public void checkOrigin(String origin, Set<String> whitelist) throws RuleException {
        if (StringUtils.isBlank(origin)) {
            throw new RuleException("no origin", ruleProperties);
        }
        if (CollectionUtils.isEmpty(whitelist)) {
            throw new RuleException("unsafe origin:" + origin, ruleProperties);
        }
        for (String white : whitelist) {
            if (origin.contains(white)) {
                return;
            }
        }
        throw new RuleException("unsafe referer:" + origin, ruleProperties);
    }

}
