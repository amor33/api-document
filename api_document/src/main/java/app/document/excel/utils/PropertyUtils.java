package app.document.excel.utils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.NullValueInNestedPathException;
import org.springframework.beans.PropertyAccessorFactory;

public class PropertyUtils {
	private static Logger log = LoggerFactory.getLogger(PropertyUtils.class);

	/**
     * <p>Copy property values from the "origin" bean to the "destination" bean
     * for all cases where the property names are the same (even though the
     * actual getter and setter methods might have been customized via
     * <code>BeanInfo</code> classes).</p>
     *
     * <p>For more details see <code>PropertyUtilsBean</code>.</p>
     *
     * @param dest Destination bean whose properties are modified
     * @param orig Origin bean whose properties are retrieved
     *
     * @see PropertyUtilsBean#copyProperties
     */
	public static void copyProperties(Object dest, Object orig) {
		try {
			org.apache.commons.beanutils.PropertyUtils.copyProperties(dest, orig);
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T getProperty(Object bean, String name) {
		if ("this".equals(name)) {
			return (T) bean;
		}
		if (bean instanceof Map) {
			@SuppressWarnings("rawtypes")
			Map map = (Map) bean;
			return (T) map.get(name);
		}
		BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(bean);
		bw.setAutoGrowNestedPaths(true);
		try {
			return (T) bw.getPropertyValue(name);
		} catch (NullValueInNestedPathException e) {
			if (getPropertyType(e.getBeanClass(), e.getPropertyName()).isEnum()) {
				log.debug("枚举类型属性 {} 的值为 null，所以属性 {} 的值直接返回 null", e.getPropertyName(), name);
				return null;
			} else {
				throw e;
			}
		}
	}

	public static void setProperty(Object bean, String name, Object value) {
		if ("this".equals(name)) {
			return;
		}
		BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(bean);
		bw.setAutoGrowNestedPaths(true);
		bw.setPropertyValue(name, value);
	}

	/**
	 * 与Class.getDeclaredFields方法类似，获取给定类型的声明属性，但是不同的是，
	 * 此方法还会包含所有祖先类的声明属性
	 * 
	 * @param clz
	 * @return
	 */
	public static Field[] getAllDeclaredFields(Class<?> clz) {
		Field[] fields = new Field[0];
		for(Class<?> curClz = clz; curClz != Object.class; curClz = curClz.getSuperclass()) {
			fields = ArrayUtils.addAll(fields, curClz.getDeclaredFields());
		}
		return fields;
	}

	public static boolean isReadable(Object bean, String name) {
		return org.apache.commons.beanutils.PropertyUtils.isReadable(bean, name);
	}


	public static Method getReadMethod(PropertyDescriptor descriptor) {
		return org.apache.commons.beanutils.PropertyUtils.getReadMethod(descriptor);
	}

	/**
	 * 获取属性的类型，如果是集合类型，则返回集合类型的内部类型
	 * 
	 * @param clz
	 * @param propName 属性名称，支持点分隔的级联属性
	 * @return
	 */
	public static Class<?> getPropertyType(Class<?> clz, String propName) {
		return getPropertyTypeOfRawOrNot(clz, propName, false);
	}

	/**
	 * 获取属性的原始类型，如果是集合类型，则返回集合类型
	 * 
	 * @param clz
	 * @param propName 属性名称，支持点分割的级联属性
	 * @return
	 */
	public static Class<?> getPropertyRawType(Class<?> clz, String propName) {
		return getPropertyTypeOfRawOrNot(clz, propName, true);
	}

	private static Class<?> getPropertyTypeOfRawOrNot(Class<?> clz, String propName, boolean raw) {
		if (propName.contains(".")) {
			String first = propName.substring(0, propName.indexOf("."));
			String remain = propName.substring(propName.indexOf(".") + 1);
			return getPropertyType(getPropertyType0(clz, first, raw), remain);
		} else {
			return getPropertyType0(clz, propName, raw);
		}		
	}

	private static Class<?> getPropertyType0(Class<?> clz, String propName, boolean raw) {
		for (PropertyDescriptor pd : org.apache.commons.beanutils.PropertyUtils.getPropertyDescriptors(clz)) {
			if (pd.getName().equals(propName)) {
				if ((!raw) && Collection.class.isAssignableFrom(pd.getPropertyType())) {
					Type rType = pd.getReadMethod().getGenericReturnType();
					if (rType instanceof ParameterizedType) {
						ParameterizedType pType = (ParameterizedType) rType;
						return (Class<?>) pType.getActualTypeArguments()[0];
					}
				}
				return pd.getPropertyType();
			}
		}
		return null;
	}

}
