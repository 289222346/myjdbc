package com.myjdbc.core.util.config.properties;

import com.myjdbc.core.util.config.properties.enums.PropertiesFile;
import com.myjdbc.core.util.config.properties.enums.PropertiesIdGenerator;

/**
 * ID生成器配置
 *
 * @author 陈文
 */
public class IdGeneratorConfig {

    /**
     * 雪花-数据中心编码
     */
    public static final int DATACENTERID;

    /**
     * 雪花-机器编码
     */
    public static final int MACHINEID;

    static {
        //属性工具
        PropertiesConfigUtil util = new PropertiesConfigUtil(PropertiesFile.ID_GENERATOR);
        DATACENTERID = Integer.parseInt(util.readProperty(PropertiesIdGenerator.DATA_CENTER_ID.getCode()) + "");
        MACHINEID = Integer.parseInt(util.readProperty(PropertiesIdGenerator.MACHINE_ID.getCode()) + "");
    }

}
