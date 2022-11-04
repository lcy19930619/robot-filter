package net.jlxxw.robot.filter.core.vo;

import java.util.List;
import java.util.Map;

/**
 * count by client id
 * @author chunyang.leng
 * @date 2022-11-04 3:39 PM
 */
public class RobotClientIdVO {
    private String filterName;

    /**
     * key rule name
     * value client info
     */
    private Map<String,List<ClientIdCountVO>> data;

    public String getFilterName() {
        return filterName;
    }

    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }

    public Map<String, List<ClientIdCountVO>> getData() {
        return data;
    }

    public void setData(Map<String, List<ClientIdCountVO>> data) {
        this.data = data;
    }
}
