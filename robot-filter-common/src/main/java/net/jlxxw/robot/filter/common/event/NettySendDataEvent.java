package net.jlxxw.robot.filter.common.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author chunyang.leng
 * @date 2022-11-03 6:40 PM
 */
public class NettySendDataEvent extends ApplicationEvent {
    private final Object data;
    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public NettySendDataEvent(Object source) {
        super(source);
        this.data = source;
    }

    public Object getData() {
        return data;
    }
}
