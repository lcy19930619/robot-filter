package net.jlxxw.robot.filter.core.lru;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author chunyang.leng
 * @date 2022-11-04 6:28 PM
 */
public class TimeOutLru<K, V extends Time> extends LinkedHashMap<K, V> {

    /**
     * lru size
     */
    int size;
    /**
     * expiry
     */
    private final int expiryTime;

    /**
     * initialized
     *
     * @param size       lru size
     * @param expiryTime expiry Time ,unit: ms
     */
    public TimeOutLru(int size, int expiryTime, int initialCapacity) {
        super(initialCapacity, 0.75f, true);
        this.expiryTime = expiryTime;
        this.size = size;
    }

    /**
     * 如果map里面的元素个数大于了缓存最大容量，则删除链表的顶端元素
     */
    @Override
    public boolean removeEldestEntry(Map.Entry<K, V> entry) {
        Time value = entry.getValue();
        long time = value.getCreateTime();
        long currentTimeMillis = System.currentTimeMillis();
        return time + expiryTime > currentTimeMillis || size() > size;
    }

}
