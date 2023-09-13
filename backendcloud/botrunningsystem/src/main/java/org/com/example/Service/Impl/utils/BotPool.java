package org.com.example.Service.Impl.utils;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class BotPool extends Thread {
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition(); // 定义条件变量
    private final Queue<Bot> bots = new LinkedList<>(); // 定义消息队列

    public void addBot(Integer userId, String botCode, String input) {
        lock.lock();
        try {
            bots.add(new Bot(userId, botCode, input));
            condition.signalAll(); // 唤醒所有的等待线程
        } finally {
            lock.unlock();
        }
    }

    private void consume(Bot bot) {
        Consumer consumer = new Consumer();
        consumer.startTimeout(2000, bot);
    }

    @Override
    public void run() { // 消费者线程
        while (true) {
            lock.lock();
            if (bots.isEmpty()) {
                try {
                    condition.await();
                } catch (InterruptedException e) {
                    lock.unlock();
                    e.printStackTrace();
                    break;
                }
            } else {
                Bot bot = bots.remove();
                lock.unlock(); // 当没有读写冲突后及时进行解锁，因为下面的代码执行时间很长
                consume(bot); // 对当前线程进行消耗
            }
        }
    }
}
