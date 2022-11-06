package net.jlxxw.robot.filter.core.limit;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import net.jlxxw.robot.filter.core.lru.Time;

/**
 * @author chunyang.leng
 * @date 2022-03-10 11:15 AM
 */
public class SimpleCountUtils implements Time {

    private long createTime = System.currentTimeMillis();
    /**
     * ip or client id
     */
    private Set<String> infoSet = new HashSet<String>();
    /**
     * time windows
     * unit:milliseconds
     */
    private final long interval;
    /**
     * current time
     */
    private long timeStamp = System.currentTimeMillis();
    /**
     * init pass is 0
     */
    private final AtomicInteger reqCount = new AtomicInteger(0);

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
     * if info set not found info
     * then
     * inc and get count
     *
     * @param info info
     * @return
     */
    public int incrementAndGet(String info) {
        long now = System.currentTimeMillis();
        if (now < timeStamp + interval) {
            if (infoSet.contains(info)){
                return reqCount.get();
            }
            // in current interval
            return reqCount.incrementAndGet();
        } else {
            timeStamp = now;
            // reset 1
            reqCount.set(1);
            infoSet.clear();
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

    /**
     * count size
     * @return
     */
    public int countInfoSize(){
        return infoSet.size();
    }

    /**
     * gai this creates time milliseconds
     *
     * @return
     */
    @Override
    public long getCreateTime() {
        return createTime;
    }
}
