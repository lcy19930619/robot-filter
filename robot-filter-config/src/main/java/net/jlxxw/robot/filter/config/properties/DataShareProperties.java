package net.jlxxw.robot.filter.config.properties;

/**
 * <b>no support refresh<b/>
 * @author chunyang.leng
 * @date 2022-11-03 5:58 PM
 */
public class DataShareProperties {

    /**
     * data share model
     * redis or netty sync
     * default is netty
     */
    private Model model = Model.NETTY;

    private NettyProperties netty;

    public NettyProperties getNetty() {
        return netty;
    }

    public void setNetty(NettyProperties netty) {
        this.netty = netty;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    enum Model{
        NETTY,
        REDIS
    }
}
