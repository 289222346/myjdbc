package com.myjdbc.core.idgenerator;

import com.myjdbc.core.annotations.IDAutoGenerator;
import com.myjdbc.core.config.properties.IdGeneratorConfig;

import java.io.Serializable;


/**
 * ID自动生成工具
 *
 * @author 陈文
 */
public class IdGeneratorUtil {

    /**
     * 雪花生成器-默认机器编码0
     */
    private static SnowFlakeGenerator snowFlakeGenerator = new SnowFlakeGenerator(IdGeneratorConfig.DATACENTERID, IdGeneratorConfig.MACHINEID);

    /**
     * 初始化雪花ID生成器
     *
     * @param datacenterId
     * @param machineId
     */
    public static void initSnowFlakeGenerator(int datacenterId, int machineId) {
        snowFlakeGenerator = new SnowFlakeGenerator(datacenterId, machineId);
    }

    /**
     * 通过注解来生成ID
     *
     * @param idAutoGenerator
     * @return
     */
    public static Serializable generateID(IDAutoGenerator idAutoGenerator) {
        if (idAutoGenerator != null) {
            //生成String类型,默认UUID
            if (String.class.equals(idAutoGenerator.value())) {
                return UUIDHexGenerator.getUUID();
            }

            //生成Integer类型,默认snowflake(雪花算法)
            if (Integer.class.equals(idAutoGenerator.value())) {
                return snowFlakeGenerator.nextId();
            }
        }
        return null;
    }
}
