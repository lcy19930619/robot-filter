package net.jlxxw.robot.filter.common.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author chunyang.leng
 * @date 2022-11-10 1:19 PM
 */
public class SystemStartEvent extends ApplicationEvent {

    private final long time;

    public SystemStartEvent(long time) {
        super(time);
        this.time = time;
    }

    public long getTime() {
        return time;
    }
}
