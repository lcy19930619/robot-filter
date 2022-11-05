package net.jlxxw.robot.filter.config.properties.ui;

import java.util.Collections;
import java.util.List;

/**
 * @author chunyang.leng
 * @date 2022-11-05 10:57 PM
 */
public class UiProperties {

    private String loginName;

    private String password;

    /**
     *
     * ip range
     */
    private List<String> whiteIplist = Collections.singletonList("127.0.0.1");

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getWhiteIplist() {
        return whiteIplist;
    }

    public void setWhiteIplist(List<String> whiteIplist) {
        this.whiteIplist = whiteIplist;
    }
}
