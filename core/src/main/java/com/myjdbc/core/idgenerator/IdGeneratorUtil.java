package com.myjdbc.core.idgenerator;

import com.myjdbc.api.annotations.IDAutoGenerator;
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
    private static final SnowFlakeGenerator snowFlakeGenerator = new SnowFlakeGenerator(IdGeneratorConfig.DATACENTERID, IdGeneratorConfig.MACHINEID);

    /**
     * 通过注解来生成ID
     *
     * @param idAutoGenerator
     * @return
     */
    public static Serializable generateID(IDAutoGenerator idAutoGenerator) {
        if (idAutoGenerator != null) {
            Class cls;
            if (idAutoGenerator.type() != IDAutoGenerator.Type.DEFAULT) {
                cls = idAutoGenerator.type().getCls();
            } else {
                cls = idAutoGenerator.value();
            }

            //生成String类型,默认UUID
            if (String.class.equals(cls)) {
                return UUIDHexGenerator.getUUID();
            }

            //生成Integer类型,默认snowflake(雪花算法)
            if (Integer.class.equals(cls)) {
                return snowFlakeGenerator.nextId();
            }
        }
        return null;
    }
}
