package net.jlxxw.robot.filter.core.limit;

/**
 * @author chunyang.leng
 * @date 2022-03-10 11:15 AM
 */
public class SimpleLimit {
    /**
     * 时间窗口内最大请求数
     */
    public int limit;
    /**
     * 时间窗口ms
     */
    public long interval = 1000 * 60;
    /**
     * 当前时间
     */
    public long timeStamp = System.currentTimeMillis();
    /**
     * 初始化计数器
     */
    public int reqCount = 0;

    public SimpleLimit(int limit) {
        this.limit = limit;
    }

    public boolean limit() {
        long now = System.currentTimeMillis();
        if (now < timeStamp + interval) {
            // 在时间窗口内
            reqCount++;
            // 判断当前时间窗口内是否超过最大请求控制数
            return reqCount <= limit;
        } else {
            timeStamp = now;
            // 超时后重置
            reqCount = 1;
            return true;
        }
    }
}
