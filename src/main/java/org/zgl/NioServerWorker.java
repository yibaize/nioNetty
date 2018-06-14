package org.zgl;

import org.zgl.pool.NioSelectorRunnablePool;
import org.zgl.pool.Worker;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Executor;

/**
 * @作者： big
 * @创建时间： 2018/6/14
 * @文件描述：
 */
public class NioServerWorker extends AbstractNioSelector implements Worker {
    public NioServerWorker(Executor executor, String threadName, NioSelectorRunnablePool selectorRunnablePool) {
        super(executor, threadName, selectorRunnablePool);
    }

    protected void process(Selector selector) throws IOException {
        Set<SelectionKey> selectedKeys = selector.selectedKeys();
        if (!selectedKeys.isEmpty()) {
            Iterator ite = this.selector.selectedKeys().iterator();

            while(ite.hasNext()) {
                SelectionKey key = (SelectionKey)ite.next();
                ite.remove();
                SocketChannel channel = (SocketChannel)key.channel();
                int ret = 0;
                boolean failure = true;
                ByteBuffer buffer = ByteBuffer.allocate(1024);

                try {
                    ret = channel.read(buffer);
                    failure = false;
                } catch (Exception var10) {
                    ;
                }

                if (ret > 0 && !failure) {
                    System.out.println("收到数据:" + new String(buffer.array()));
                    ByteBuffer outBuffer = ByteBuffer.wrap("收到\n".getBytes());
                    channel.write(outBuffer);
                } else {
                    key.cancel();
                    System.out.println("客户端断开连接");
                }
            }

        }
    }

    public void registerNewChannelTask(final SocketChannel channel) {
        final Selector selector = this.selector;
        this.registerTask(new Runnable() {
            public void run() {
                try {
                    channel.register(selector, 1);
                } catch (ClosedChannelException var2) {
                    var2.printStackTrace();
                }

            }
        });
    }

    protected int select(Selector selector) throws IOException {
        return selector.select(500L);
    }
}
