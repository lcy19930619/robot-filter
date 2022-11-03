package net.jlxxw.robot.filter.data.share.netty.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import net.jlxxw.robot.filter.data.share.netty.protocol.protobuf.RequestProtocol;

/**
 * @author chunyang.leng
 * @date 2021-07-07 12:50 下午
 */
public class ClientEncoder extends MessageToByteEncoder<RequestProtocol> {

    /**
     * Encode a message into a {@link ByteBuf}. This method will be called for each written message that can be handled
     * by this encoder.
     *
     * @param ctx the {@link ChannelHandlerContext} which this {@link MessageToByteEncoder} belongs to
     * @param msg the message to encode
     * @param out the {@link ByteBuf} into which the encoded message will be written
     * @throws Exception is thrown if an error occurs
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, RequestProtocol msg, ByteBuf out) throws Exception {
        byte[] body = msg.toByteArray();
        //读取消息的长度
        int dataLength = body.length;
        //先将消息长度写入，也就是消息头
        out.writeInt(dataLength);
        //消息体中包含我们要发送的数据
        out.writeBytes(body);
    }
}
