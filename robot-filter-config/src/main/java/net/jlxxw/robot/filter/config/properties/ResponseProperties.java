package net.jlxxw.robot.filter.config.properties;

/**
 * response properties bean
 * @author chunyang.leng
 * @date 2022-11-03 11:46 AM
 */
public class ResponseProperties {
    /**
     * return reject message
     */
    private boolean returnRejectMessage = false;

    public boolean isReturnRejectMessage() {
        return returnRejectMessage;
    }

    public void setReturnRejectMessage(boolean returnRejectMessage) {
        this.returnRejectMessage = returnRejectMessage;
    }
}
