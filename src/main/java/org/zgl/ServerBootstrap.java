package org.zgl;

import org.zgl.pool.Boss;
import org.zgl.pool.NioSelectorRunnablePool;

import java.net.SocketAddress;
import java.nio.channels.ServerSocketChannel;

/**
 * @作者： big
 * @创建时间： 2018/6/14
 * @文件描述：
 */
public class ServerBootstrap {
    private NioSelectorRunnablePool selectorRunnablePool;

    public ServerBootstrap(NioSelectorRunnablePool selectorRunnablePool) {
        this.selectorRunnablePool = selectorRunnablePool;
    }

    public void bind(SocketAddress localAddress) {
        try {
            ServerSocketChannel serverChannel = ServerSocketChannel.open();
            serverChannel.configureBlocking(false);
            serverChannel.socket().bind(localAddress);
            Boss nextBoss = this.selectorRunnablePool.nextBoss();
            nextBoss.registerAcceptChannelTask(serverChannel);
        } catch (Exception var4) {
            var4.printStackTrace();
        }

    }
}
