package org.zgl.pool;

import java.nio.channels.ServerSocketChannel;

/**
 * @作者： big
 * @创建时间： 2018/6/14
 * @文件描述：boss线程
 */
public interface Boss {
    void registerAcceptChannelTask(ServerSocketChannel var1);
}
