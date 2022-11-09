package net.jlxxw.robot.filter.core.vo.base;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * page result
 * @author chunyang.leng
 * @date 2022-03-24 3:27 PM
 */
@Schema(description = "page result")
public class PageRequestResult {
    /**
     * data total
     */
    @Schema(description = " data total ")
    private Long total;

    /**
     * current page number
     */
    @Schema(description = "current page number")
    private Long currentPageNum;

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Long getCurrentPageNum() {
        return currentPageNum;
    }

    public void setCurrentPageNum(Long currentPageNum) {
        this.currentPageNum = currentPageNum;
    }
}
