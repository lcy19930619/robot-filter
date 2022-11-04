package net.jlxxw.robot.filter.core.limit;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author chunyang.leng
 * @date 2022-03-10 11:15 AM
 */
public class SimpleCountUtils {
    /**
     * time windows
     * unit:milliseconds
     */
    public long interval;
    /**
     * current time
     */
    public long timeStamp = System.currentTimeMillis();
    /**
     * init pass is 0
     */
    public AtomicInteger reqCount = new AtomicInteger(0);

    public SimpleCountUtils(long interval) {
        this.interval = interval;
    }

    /**
     * inc and get count
     *
     * @return
     */
    public int incrementAndGet() {
        long now = System.currentTimeMillis();
        if (now < timeStamp + interval) {
            // in current interval
            return reqCount.incrementAndGet();
        } else {
            timeStamp = now;
            // reset 1
            reqCount.set(1);
            return 1;
        }
    }

    /**
     * get current passed requests
     *
     * @return current passed requests
     */
    public int currentPass() {
        return reqCount.get();
    }
}
