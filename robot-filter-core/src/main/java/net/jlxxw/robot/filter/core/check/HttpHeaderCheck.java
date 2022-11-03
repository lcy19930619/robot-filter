package net.jlxxw.robot.filter.core.check;

import java.util.Set;
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

    /**
     * check referer
     * @param referer http referer
     * @param whitelist whitelist
     * @throws RuleException not in whitelist
     */
    public void checkReferer(String referer, Set<String> whitelist) throws RuleException {
        if (StringUtils.isBlank(referer)) {
            throw new RuleException("no referer");
        }
        if (CollectionUtils.isEmpty(whitelist)){
            throw new RuleException("unsafe referer:"+ referer);
        }
        for (String white : whitelist) {
            if (white.contains(referer)){
                return;
            }
        }
        throw new RuleException("unsafe referer:" + referer);
    }

    /**
     * check origin
     * @param origin http origin
     * @param whitelist whitelist
     * @throws RuleException not in whitelist
     */
    public void checkOrigin(String origin, Set<String> whitelist) throws RuleException {
        if (StringUtils.isBlank(origin)) {
            throw new RuleException("no origin");
        }
        if (CollectionUtils.isEmpty(whitelist)){
            throw new RuleException("unsafe origin:"+ origin);
        }
        for (String white : whitelist) {
            if (white.contains(origin)){
                return;
            }
        }
        throw new RuleException("unsafe referer:" + origin);
    }

}
