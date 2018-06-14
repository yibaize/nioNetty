package org.zgl.pool;

import java.nio.channels.SocketChannel;

/**
 * @作者： big
 * @创建时间： 2018/6/14
 * @文件描述：worker线程
 */
public interface Worker {
    void registerNewChannelTask(SocketChannel var1);
}
