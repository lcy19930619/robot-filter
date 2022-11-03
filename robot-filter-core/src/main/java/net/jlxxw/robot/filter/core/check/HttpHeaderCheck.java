package net.jlxxw.robot.filter.core.check;

import java.util.Set;
import net.jlxxw.robot.filter.core.exception.RuleException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
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
     * @param requestUrl http request url
     * @param ip client IP address
     * @throws RuleException not in whitelist
     */
    public void checkReferer(String requestUrl,String ip,String referer, Set<String> whitelist) throws RuleException {
        if (StringUtils.isBlank(referer)) {
            throw new RuleException("no referer");
        }
    }
}
