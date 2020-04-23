package com.myjdbc.core.util;

import com.myjdbc.core.annotations.IDAutoGenerator;
import com.myjdbc.core.idgenerator.UUIDHexGenerator;

import java.io.Serializable;

public class IdGeneratorUtil {

    public static Serializable generateID(IDAutoGenerator idAutoGenerator) {
        if (idAutoGenerator != null) {
            //有ID生成器
            if (String.class.equals(idAutoGenerator.value())) {
                return UUIDHexGenerator.getUUID();
            }
        }
        return null;
    }
}
