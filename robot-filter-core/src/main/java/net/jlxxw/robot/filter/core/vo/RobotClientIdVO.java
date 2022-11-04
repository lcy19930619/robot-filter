package net.jlxxw.robot.filter.core.vo;

import java.util.List;

/**
 * count by client id
 * @author chunyang.leng
 * @date 2022-11-04 3:39 PM
 */
public class RobotClientIdVO {
    private String filterName;

    private String ruleName;

    private List<ClientIdCountVO> data;

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

    public List<ClientIdCountVO> getData() {
        return data;
    }

    public void setData(List<ClientIdCountVO> data) {
        this.data = data;
    }
}
