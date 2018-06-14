package org.zgl.pool;

import org.zgl.NioServerBoss;
import org.zgl.NioServerWorker;

import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @作者： big
 * @创建时间： 2018/6/14
 * @文件描述：
 */
public class NioSelectorRunnablePool {
    private final AtomicInteger bossIndex = new AtomicInteger();
    private Boss[] bosses;
    private final AtomicInteger workerIndex = new AtomicInteger();
    private Worker[] workeres;

    public NioSelectorRunnablePool(Executor boss, Executor worker) {
        this.initBoss(boss, 1);
        this.initWorker(worker, Runtime.getRuntime().availableProcessors() * 2);
    }

    private void initBoss(Executor boss, int count) {
        this.bosses = new NioServerBoss[count];

        for(int i = 0; i < this.bosses.length; ++i) {
            this.bosses[i] = new NioServerBoss(boss, "boss thread " + (i + 1), this);
        }

    }

    private void initWorker(Executor worker, int count) {
        this.workeres = new NioServerWorker[count];

        for(int i = 0; i < this.workeres.length; ++i) {
            this.workeres[i] = new NioServerWorker(worker, "worker thread " + (i + 1), this);
        }

    }

    public Worker nextWorker() {
        return this.workeres[Math.abs(this.workerIndex.getAndIncrement() % this.workeres.length)];
    }

    public Boss nextBoss() {
        return this.bosses[Math.abs(this.bossIndex.getAndIncrement() % this.bosses.length)];
    }
}
