package cn.hikyson.android.godeye.sample;

import java.util.concurrent.CountDownLatch;

/**
 * Created by kysonchao on 2018/1/12.
 */
public class DeadLockMaker {

    public static void makeBlock(final Loggable loggable) {
        final Object lock1 = new Object();
        final Object lock2 = new Object();
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (lock1) {
                    loggable.log(Thread.currentThread().getName() + " got lock1");
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    synchronized (lock2) {
                        loggable.log(Thread.currentThread().getName() + " got lock2");
                    }
                }
                loggable.log(Thread.currentThread().getName() + " end");
            }
        });
        thread.setName("IAMDEADLOCKTHREAD1");
        thread.start();
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (lock2) {
                    loggable.log(Thread.currentThread().getName() + " got lock2");
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    synchronized (lock1) {
                        loggable.log(Thread.currentThread().getName() + " got lock2");
                    }
                }
                loggable.log(Thread.currentThread().getName() + " end");
            }
        });
        thread2.setName("IAMDEADLOCKTHREAD2");
        thread2.start();
    }

    public static void makeNormal() {
        for (int i = 0; i < 5; i++) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(30000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.setName("android-god-eye-" + i);
            thread.start();
        }
    }

    public static void makeWait(final Loggable loggable) {
        final CountDownLatch lock1 = new CountDownLatch(1);
        final CountDownLatch lock2 = new CountDownLatch(1);
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    lock2.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                lock1.countDown();
            }
        });
        thread.setName("IAMDEADLOCKTHREAD1");
        thread.start();
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    lock1.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                lock2.countDown();
            }
        });
        thread2.setName("IAMDEADLOCKTHREAD2");
        thread2.start();
    }
}
