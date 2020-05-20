package com.myjdbc.core.idgenerator;

import com.myjdbc.core.util.more.MacAddressAPI;

/**
 * 雪花ID生成器
 * 本类由 陈文 于 2020-05-20 09:36 引用开源代码整理而成
 * 参考地址：https://github.com/souyunku/SnowFlake
 * <p>
 * <p>
 * 描述: Twitter的分布式自增ID雪花算法snowflake (Java版)
 *
 * @author yanpenglei
 * @create 2018-03-13 12:37
 **/
public class SnowFlakeGenerator {

    /**
     * 起始的时间戳
     */
    private final static long START_STMP = 1589881011555L;

    /**
     * 序列号占用的位数
     */
    private final static long SEQUENCE_BIT = 12;
    /**
     * 机器标识占用的位数
     */
    private final static long MACHINE_BIT = 5;
    /**
     * 数据中心占用的位数
     */
    private final static long DATACENTER_BIT = 5;

    /**
     * 机器标识-最大值
     */
    private final static long MAX_DATACENTER_NUM = -1L ^ (-1L << DATACENTER_BIT);
    /**
     * 数据中心-最大值
     */
    private final static long MAX_MACHINE_NUM = -1L ^ (-1L << MACHINE_BIT);
    /**
     * 时间戳-最大值
     */
    private final static long MAX_SEQUENCE = -1L ^ (-1L << SEQUENCE_BIT);


    /**
     * 机器标识-向左位移
     */
    private final static long MACHINE_LEFT = SEQUENCE_BIT;
    /**
     * 数据中心-向左位移
     */
    private final static long DATACENTER_LEFT = SEQUENCE_BIT + MACHINE_BIT;
    /**
     * 时间戳-向左位移
     */
    private final static long TIMESTMP_LEFT = DATACENTER_LEFT + DATACENTER_BIT;

    /**
     * 数据中心
     */
    private long datacenterId;
    /**
     * 机器标识
     */
    private long machineId;
    /**
     * 序列号
     */
    private long sequence = 0L;

    /**
     * 上一次时间戳
     */
    private long lastStmp = -1L;

    public SnowFlakeGenerator(int datacenterId, int machineId) {
        if (datacenterId > MAX_DATACENTER_NUM || datacenterId < 0) {
            throw new IllegalArgumentException("datacenterId can't be greater than MAX_DATACENTER_NUM or less than 0");
        }
        if (machineId > MAX_MACHINE_NUM || machineId < 0) {
            throw new IllegalArgumentException("machineId can't be greater than MAX_MACHINE_NUM or less than 0");
        }
        this.datacenterId = datacenterId;
        this.machineId = machineId;
    }

    /**
     * 产生下一个ID
     *
     * @return
     */
    public synchronized long nextId() {
        long currStmp = getNewstmp();
        if (currStmp < lastStmp) {
            throw new RuntimeException("Clock moved backwards.  Refusing to generate id");
        }

        if (currStmp == lastStmp) {
            //相同毫秒内，序列号自增
            sequence = (sequence + 1) & MAX_SEQUENCE;
            //同一毫秒的序列数已经达到最大
            if (sequence == 0L) {
                currStmp = getNextMill();
            }
        } else {
            //不同毫秒内，序列号置为0
            sequence = 0L;
        }

        lastStmp = currStmp;

        //时间戳部分|数据中心部分|机器标识部分|序列号部分
        return (currStmp - START_STMP) << TIMESTMP_LEFT
                | datacenterId << DATACENTER_LEFT
                | machineId << MACHINE_LEFT
                | sequence;
    }

    private long getNextMill() {
        long mill = getNewstmp();
        while (mill <= lastStmp) {
            mill = getNewstmp();
        }
        return mill;
    }

    private long getNewstmp() {
        return System.currentTimeMillis();
    }

    public static void main(String[] args) {

        int x = BytesHelper.toInt(MacAddressAPI.getMACAddress().getBytes());
        System.out.println(x);

//        SnowFlakeGenerator snowFlake = new SnowFlakeGenerator(2, 3);
//        long start = System.currentTimeMillis();
//        for (int i = 0; i < 1000000; i++) {
//            System.out.println(snowFlake.nextId());
//        }
//        System.out.println(System.currentTimeMillis() - start);
    }
}
