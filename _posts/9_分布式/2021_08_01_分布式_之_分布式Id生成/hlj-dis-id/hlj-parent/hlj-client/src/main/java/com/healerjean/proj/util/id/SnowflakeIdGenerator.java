package com.healerjean.proj.util.id;

import com.healerjean.proj.common.ResponseBean;
import com.healerjean.proj.common.exception.PublicBasicException;
import com.healerjean.proj.util.NetUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ThreadLocalRandom;

@Slf4j
public class SnowflakeIdGenerator {

    /**
     * zkAddress
     */
    private String zkAddress;
    /**
     * port
     */
    private String port;
    /**
     * 最后的时间戳
     */
    private long lastTimestamp = -1L;
    /**
     * 最后的时间戳
     */
    private final long twepoch;
    /**
     * 当前机器的IP地址
     */
    private final String ip;
    /**
     * 当前机器的工作ID
     */
    private long workerId;
    /**
     * workerIdBits
     */
    private final long workerIdBits = 10L;
    /**
     * maxWorkerId
     */
    private  final long maxWorkerId = ~(-1L << workerIdBits);
    /**
     * sequence
     */
    private long sequence = 0L;
    /**
     * sequenceBits
     */
    private final long sequenceBits = 12L;
    /**
     * workerIdShift
     */
    private final long workerIdShift = sequenceBits;
    /**
     * sequenceMask
     */
    private final long sequenceMask = ~(-1L << sequenceBits);
    /**
     * timestampLeftShift
     */
    private final long timestampLeftShift = sequenceBits + workerIdBits;
    /**
     * random
     */
    private ThreadLocalRandom random;


    /**
     * SnowflakeIdGenerater
     * @param zkAddress zkAddress
     * @param port port
     */
    public SnowflakeIdGenerator(String zkAddress, String port) {
        this.zkAddress = zkAddress;
        this.port = port;
        this.twepoch = 1288834974657L;
        if (now() < twepoch) {
            log.error("Snowflake设置twepoch异常");
            throw new PublicBasicException(500, "Snowflake设置twepoch异常");
        }
        random = ThreadLocalRandom.current();
        ip = NetUtils.getIp();
        SnowflakeZookeeperHolder holder = new SnowflakeZookeeperHolder(ip, String.valueOf(port), zkAddress);
        holder.init();
        workerId = holder.getWorkerID();
        if (workerId < 0 || workerId > maxWorkerId) {
            log.error("workerID 值范围在 0 - 1023");
            throw new PublicBasicException(500, "workerID 值范围在 0 - 1023");
        }
        log.info("twepoch:{} ,ip:{} ,zkAddress:{} port:{}", twepoch, ip, zkAddress, port);
    }


    /**
     * 产生递增不重复19位长度的数字
     */
    public ResponseBean get() {
        long timestamp = now();
        if (timestamp < lastTimestamp) {
            long offset = lastTimestamp - timestamp;
            if (offset <= 5) {
                try {
                    wait(offset << 1);
                    timestamp = now();
                    if (timestamp < lastTimestamp) {
                        return ResponseBean.buildFailure("时间对比很失败");
                    }
                } catch (InterruptedException e) {
                    log.error("wait interrupted");
                    return ResponseBean.buildFailure("时间不太对");
                }
            } else {
                return ResponseBean.buildFailure("时间对比失败");
            }
        }
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0) {
                //seq 为0的时候表示是下一毫秒时间开始对seq做随机
                sequence = random.nextInt(100);
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            //如果是新的ms开始
            sequence = random.nextInt(100);
        }
        lastTimestamp = timestamp;
        long id = ((timestamp - twepoch) << timestampLeftShift) | (workerId << workerIdShift) | sequence;
        return ResponseBean.buildFailure(String.valueOf(id));
    }

    protected long now() {
        return System.currentTimeMillis();
    }

    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = now();
        while (timestamp <= lastTimestamp) {
            timestamp = now();
        }
        return timestamp;
    }

}
