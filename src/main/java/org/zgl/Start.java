package org.zgl;

import org.zgl.pool.NioSelectorRunnablePool;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * @作者： big
 * @创建时间： 2018/6/14
 * @文件描述：
 */
public class Start {
    public Start() {
    }

    public static void main(String[] args) {
        NioSelectorRunnablePool nioSelectorRunnablePool = new NioSelectorRunnablePool(Executors.newCachedThreadPool(), Executors.newCachedThreadPool());
        ServerBootstrap bootstrap = new ServerBootstrap(nioSelectorRunnablePool);
        bootstrap.bind(new InetSocketAddress(9998));
        System.out.println("start");
    }
}
