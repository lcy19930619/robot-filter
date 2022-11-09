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
    private final long expiryTime;

    /**
     * initialized
     *
     * @param size       lru size, -1 no max size
     * @param expiryTime expiry Time ,unit: ms, -1 no expiry
     */
    public TimeOutLru(int size, long expiryTime, int initialCapacity) {
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
        return (expiryTime > 0 && time + expiryTime > currentTimeMillis) || (size > 0 && size() > size);
    }

}
