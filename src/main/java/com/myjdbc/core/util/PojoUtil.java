package com.myjdbc.core.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class PojoUtil {

	private static ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * 将Json转换成Bo，只会替换Json中存在的属性
	 *
	 * @param bo
	 * @param json
	 * @param      <T>
	 * @return
	 */
	public static <T> T jsonToBo(T bo, String json) {
		try {
			bo = copy(bo, (T) objectMapper.readValue(json, bo.getClass()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bo;
	}

	/**
	 * 复制Bo，如果值为null或不存在则不复制
	 *
	 * @param bo
	 * @param newBo
	 * @param       <T>
	 * @return
	 */
	public static <T> T copy(T bo, T newBo) {
		if (null == bo || newBo == null) {
			return null;
		}
		Class cls = bo.getClass();
		Class newcCls = newBo.getClass();
		Field[] fields = cls.getDeclaredFields();
		for (Field field : fields) {
			// 获取要设置的属性的set方法名称
			String getField = "get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
			String setField = "set" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
			try {
				Method getMethod = newcCls.getMethod(getField);
				Object value = getMethod.invoke(newBo);
				if (value != null) {
					Method setMethod = cls.getMethod(setField, field.getType());
					setMethod.invoke(bo, value);
				}
			} catch (Exception e) {
				// e.printStackTrace();
			}
		}
		return bo;
	}

	/**
	 * 将bo转换成Json
	 *
	 * @param bo
	 * @param    <T>
	 * @return
	 */
	public static <T> String boTojson(T bo) {
		String str = "";
		try {
			str = objectMapper.writeValueAsString(bo);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return str;
	}

}
