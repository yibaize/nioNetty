package org.zgl;

import org.zgl.pool.NioSelectorRunnablePool;

import java.io.IOException;
import java.nio.channels.Selector;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @作者： big
 * @创建时间： 2018/6/14
 * @文件描述：
 */
public abstract class AbstractNioSelector implements Runnable {
    private final Executor executor;
    protected Selector selector;
    protected final AtomicBoolean wakenUp = new AtomicBoolean();
    private final Queue<Runnable> taskQueue = new ConcurrentLinkedQueue();
    private String threadName;
    protected NioSelectorRunnablePool selectorRunnablePool;

    AbstractNioSelector(Executor executor, String threadName, NioSelectorRunnablePool selectorRunnablePool) {
        this.executor = executor;
        this.threadName = threadName;
        this.selectorRunnablePool = selectorRunnablePool;
        this.openSelector();
    }

    private void openSelector() {
        try {
            this.selector = Selector.open();
        } catch (IOException var2) {
            throw new RuntimeException("Failed to create a selector.");
        }

        this.executor.execute(this);
    }

    public void run() {
        Thread.currentThread().setName(this.threadName);

        while(true) {
            while(true) {
                try {
                    this.wakenUp.set(false);
                    this.select(this.selector);
                    this.processTaskQueue();
                    this.process(this.selector);
                } catch (Exception var2) {
                    ;
                }
            }
        }
    }

    protected final void registerTask(Runnable task) {
        this.taskQueue.add(task);
        Selector selector = this.selector;
        if (selector != null) {
            if (this.wakenUp.compareAndSet(false, true)) {
                selector.wakeup();
            }
        } else {
            this.taskQueue.remove(task);
        }

    }

    private void processTaskQueue() {
        while(true) {
            Runnable task = (Runnable)this.taskQueue.poll();
            if (task == null) {
                return;
            }

            task.run();
        }
    }

    public NioSelectorRunnablePool getSelectorRunnablePool() {
        return this.selectorRunnablePool;
    }

    protected abstract int select(Selector var1) throws IOException;

    protected abstract void process(Selector var1) throws IOException;
}
