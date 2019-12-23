package com.myjdbc.jdbc.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * 写的太久，忘记了，应该是对于Oracle数据库的吧。
 */
public class BeanUtilE {

	public static Object objE(String type, Object obj) {
		int index = type.lastIndexOf('.');
		if (index != -1) {
			type = type.substring(index + 1);
		}
		try {
			Method objEB = BeanUtilE.class.getMethod(type + "B", new Class[] { Object.class });
			return objEB.invoke(BeanUtilE.class, obj);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static int intB(Object obj) {
		return Integer.parseInt(obj.toString());
	}

	public static double doubleB(Object obj) {
		return Double.parseDouble(obj.toString());
	}

	public static float floatB(Object obj) {
		return Float.parseFloat(obj.toString());
	}

	public static String StringB(Object obj) {
		return (String) obj.toString();
	}

}
