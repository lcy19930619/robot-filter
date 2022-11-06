package net.jlxxw.robot.filter.core.lru;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author chunyang.leng
 * @date 2022-11-04 6:28 PM
 */
public class LimitLru<K, V> extends LinkedHashMap<K, V> {
    /**
     * limit length
     */
    private final int limit;

    /**
     * initialized
     * @param limit length
     */
    public LimitLru(int limit) {
        super(16, 0.75f, true);
        this.limit = limit;
    }

    /**
     * init
     * @param limit length
     * @param initialCapacity capacity
     */
    public LimitLru(int limit,int initialCapacity) {
        super(16, 0.75f, true);
        this.limit = limit;
    }

    /**
     * 如果map里面的元素个数大于了缓存最大容量，则删除链表的顶端元素
     */
    @Override
    public boolean removeEldestEntry(Map.Entry<K, V> entry) {
        return size() > limit;
    }

}
