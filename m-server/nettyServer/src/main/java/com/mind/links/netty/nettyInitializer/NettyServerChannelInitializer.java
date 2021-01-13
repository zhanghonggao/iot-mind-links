package com.mind.links.netty.nettyInitializer;

import com.mind.links.netty.nettyHandler.*;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


/**
 * Netty编码解码器
 *
 * @author qiding
 */
@ApiModel("编码解码器")
@Component
@RequiredArgsConstructor
public class NettyServerChannelInitializer extends ChannelInitializer<SocketChannel> {

    @ApiModelProperty("通道主动处理程序(单例)")
    private final ChannelActiveHandler channelActiveHandler;

    @ApiModelProperty("频道无效的处理程序(单例)")
    private final ChannelInactiveHandler channelInactiveHandler;

    @ApiModelProperty("IO处理程序(单例)")
    private final NettyServerHandler nettyServerHandler;

    @ApiModelProperty("异常处理程序(单例)")
    private final ExceptionHandler exceptionHandler;

    @Override
    protected void initChannel(SocketChannel channel) {
        final NettyInitializerProperties nip = new NettyInitializerProperties();
        channel.pipeline()
                .addLast(channelActiveHandler.getClass().getSimpleName(), channelActiveHandler)
                .addLast(channelInactiveHandler.getClass().getSimpleName(), channelInactiveHandler)
                .addLast(nip.socketChooseHandler.getClass().getSimpleName(), nip.socketChooseHandler)
                .addLast(nip.idleStateHandler.getClass().getSimpleName(), nip.idleStateHandler)
                .addLast(nettyServerHandler.getClass().getSimpleName(), nettyServerHandler)
                .addLast(exceptionHandler.getClass().getSimpleName(), exceptionHandler);
    }

    /**
     * 为了最大程度地减少对象的创建
     */
    @ApiModel("通用编码解码器配置")
    public static class NettyInitializerProperties {

        @ApiModelProperty("心跳机制(多例)")
        volatile IdleStateHandler idleStateHandler = new IdleStateHandler(60, 0, 0);

        @ApiModelProperty("协议选择处理程序(多例)")
        volatile SocketChooseHandler socketChooseHandler = new SocketChooseHandler();

    }
}

