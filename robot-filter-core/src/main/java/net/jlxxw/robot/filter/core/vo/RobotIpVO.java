package net.jlxxw.robot.filter.core.vo;

import java.util.List;
import java.util.Map;

/**
 * count by ip count
 * @author chunyang.leng
 * @date 2022-11-04 3:41 PM
 */
public class RobotIpVO {
    private String filterName;

    /**
     * key rule name
     * value client info
     */
    private Map<String,List<IpCountVO>> data;

    public String getFilterName() {
        return filterName;
    }

    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }

    public Map<String, List<IpCountVO>> getData() {
        return data;
    }

    public void setData(Map<String, List<IpCountVO>> data) {
        this.data = data;
    }
}
