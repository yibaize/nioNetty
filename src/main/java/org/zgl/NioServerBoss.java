package org.zgl;

import org.zgl.pool.Boss;
import org.zgl.pool.NioSelectorRunnablePool;
import org.zgl.pool.Worker;

import java.io.IOException;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Executor;

/**
 * @作者： big
 * @创建时间： 2018/6/14
 * @文件描述：
 */
public class NioServerBoss  extends AbstractNioSelector implements Boss {
    public NioServerBoss(Executor executor, String threadName, NioSelectorRunnablePool selectorRunnablePool) {
        super(executor, threadName, selectorRunnablePool);
    }

    protected void process(Selector selector) throws IOException {
        Set<SelectionKey> selectedKeys = selector.selectedKeys();
        if (!selectedKeys.isEmpty()) {
            Iterator i = selectedKeys.iterator();

            while(i.hasNext()) {
                SelectionKey key = (SelectionKey)i.next();
                i.remove();
                ServerSocketChannel server = (ServerSocketChannel)key.channel();
                SocketChannel channel = server.accept();
                channel.configureBlocking(false);
                Worker nextworker = this.getSelectorRunnablePool().nextWorker();
                nextworker.registerNewChannelTask(channel);
                System.out.println("新客户端链接");
            }

        }
    }

    public void registerAcceptChannelTask(final ServerSocketChannel serverChannel) {
        final Selector selector = this.selector;
        this.registerTask(new Runnable() {
            public void run() {
                try {
                    serverChannel.register(selector, 16);
                } catch (ClosedChannelException var2) {
                    var2.printStackTrace();
                }

            }
        });
    }

    protected int select(Selector selector) throws IOException {
        return selector.select();
    }
}
