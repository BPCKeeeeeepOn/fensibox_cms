package com.fensibox.cms.common;

import com.fensibox.cms.utils.CommonUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * 分布式下主键id生成规则算法，基于twitter的snowflake算法实现
 *
 */
public class GeneratorID{

    public static final long EPOCH = LocalDateTime.of(2015,1,1,0,0,0).
            atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

    private static final long SEQUENCE_BITS = 12L;

    private static final long WORKER_ID_BITS = 10L;

    private static final long SEQUENCE_MASK = (1 << SEQUENCE_BITS) - 1;

    private static final long WORKER_ID_LEFT_SHIFT_BITS = SEQUENCE_BITS;

    private static final long TIMESTAMP_LEFT_SHIFT_BITS = WORKER_ID_LEFT_SHIFT_BITS + WORKER_ID_BITS;

    private static final long WORKER_ID_MAX_VALUE = 1L << WORKER_ID_BITS;

    private long workerId;

    private long sequence;

    private long lastTime;

    private GeneratorID(long workerId) {
        if(workerId < 0L || workerId >= WORKER_ID_MAX_VALUE) {
            throw new IllegalArgumentException();
        }
        this.workerId = workerId;
    }

    /**
     * Generate key.
     *
     * @return key type is @{@link Long}.
     */
    public synchronized long generateKey() {
        long currentMillis = getCurrentMillis();
        if (currentMillis < lastTime) {
            throw new RuntimeException(String.format(
                    "Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTime - currentMillis));
        }
        if (lastTime == currentMillis) {
            if (0L == (sequence = ++sequence & SEQUENCE_MASK)) {
                currentMillis = waitUntilNextTime(currentMillis);
            }
        } else {
            sequence = 0;
        }
        lastTime = currentMillis;
        return ((currentMillis - EPOCH) << TIMESTAMP_LEFT_SHIFT_BITS) | (workerId << WORKER_ID_LEFT_SHIFT_BITS) | sequence;
    }

    private long waitUntilNextTime(final long lastTime) {
        long time = getCurrentMillis();
        while (time <= lastTime) {
            time = getCurrentMillis();
        }
        return time;
    }

    private long getCurrentMillis() {
        return System.currentTimeMillis();
    }

    /**
     * 使用单列模式提供一个唯一实例
     *
     * @author yancy
     *
     */
    private static class idWork {
        private static final GeneratorID INSTANCE = new GeneratorID(CommonUtils.generateWorkerId());
    }

    /**
     * 对外提供获取Id的方法
     *
     * @return
     */
    public static Long getId() {
        return idWork.INSTANCE.generateKey();
    }
}
