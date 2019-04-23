package com.wqb.supports.util;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * @author Shoven
 * @date 2018-11-01 13:43
 */
public class KeyWorker {
    /**
     * 起始的时间戳
     */
    private final static long START_STAMP = 1508143349995L;
    /**
     * 机器标识位数
     */
    private final static long WORKER_ID_BITS = 5L;
    /**
     * 数据中心标识位数
     */
    private final static long DATACENTER_ID_BITS = 5L;
    /**
     * 毫秒内自增位数
     */
    private final static long SEQUENCE_BITS = 12L;
    /**
     * 机器ID偏左移12位
     */
    private final static long WORKER_ID_SHIFT = SEQUENCE_BITS;
    /**
     * 数据中心ID左移17位
     */
    private final static long DATACENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;
    /**
     * 时间毫秒左移22位
     */
    private final static long TIMESTAMP_LEFT_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + DATACENTER_ID_BITS;
    /**
     * sequence掩码，确保sequnce不会超出上限
     */
    private final static long SEQUENCE_MASK = ~(-1L << SEQUENCE_BITS);
    /**
     * 上次时间戳
     */
    private static long lastTimestamp = -1L;
    /**
     * 序列
     */
    private long sequence = 0L;
    /**
     * 服务器ID
     */
    private long workerId;
    /**
     * 进程编码
     */
    private long processId;

    private static KeyWorker keyWorker = new KeyWorker();

    private KeyWorker() {
        this.workerId = getMachineNum() & ~(-1L << WORKER_ID_BITS);
        this.processId = getProcessNum() & ~(-1L << DATACENTER_ID_BITS);
    }

    public static long nextId() {
        return keyWorker.getNextId();
    }

    public synchronized long getNextId() {
        //获取时间戳
        long timestamp = timeGen();
        //如果时间戳小于上次时间戳则报错
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(
                    String.format("Clock moved backwards. Refusing to generate id for %d milliseconds",
                            lastTimestamp - timestamp));
        }
        //如果时间戳与上次时间戳相同
        if (lastTimestamp == timestamp) {
            // 当前毫秒内，则+1，与sequenceMask确保sequence不会超出上限
            sequence = (sequence + 1) & SEQUENCE_MASK;
            if (sequence == 0) {
                // 当前毫秒内计数满了，则等待下一秒
                timestamp = tilNextMillis();
            }
        } else {
            sequence = 0;
        }
        lastTimestamp = timestamp;
        // ID偏移组合生成最终的ID，并返回ID
        return ((timestamp - START_STAMP) << TIMESTAMP_LEFT_SHIFT)
                | (processId << DATACENTER_ID_SHIFT)
                | (workerId << WORKER_ID_SHIFT)
                | sequence;
    }

    /**
     * 再次获取时间戳直到获取的时间戳与现有的不同
     *
     * @return 下一个时间戳
     */
    private long tilNextMillis() {
        long timestamp = this.timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = this.timeGen();
        }
        return timestamp;
    }

    private long timeGen() {
        return System.currentTimeMillis();
    }

    /**
     * 获取进程号编号
     *
     * @return 进程号
     */
    private long getProcessNum() {
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        return Long.parseLong(runtimeMXBean.getName().split("@")[0]);
    }

    /**
     * 获取机器号编号
     *
     * @return 机器号
     */
    private long getMachineNum() {

        StringBuilder sb = new StringBuilder();
        Enumeration<NetworkInterface> networkInterfaces;
        try {
            networkInterfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
        while (networkInterfaces.hasMoreElements()) {
            NetworkInterface ni = networkInterfaces.nextElement();
            sb.append(ni.toString());
        }
        return (long)sb.toString().hashCode();
    }
}
