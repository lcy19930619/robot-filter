package net.jlxxw.robot.filter.core.vo;

import java.util.List;

/**
 * count by ip count
 * @author chunyang.leng
 * @date 2022-11-04 3:41 PM
 */
public class RobotIpVO {
    private String filterName;

    private String ruleName;

    private List<IpCountVO> data;

    public String getFilterName() {
        return filterName;
    }

    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public List<IpCountVO> getData() {
        return data;
    }

    public void setData(List<IpCountVO> data) {
        this.data = data;
    }
}
