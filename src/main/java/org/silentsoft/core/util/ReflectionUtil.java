package org.silentsoft.core.util;

import java.lang.reflect.Method;

/**
 * @author silentsoft
 */
public class ReflectionUtil {

	/**
	 * Returns {@code Method} object that reflects the specified declared method of the {@code clazz}.
	 * this method delegates to {@link Class#getDeclaredMethod(String, Class...)}.
	 * 
	 * @param clazz
	 * @param name
	 * @param parameterTypes
	 * @return
	 * @throws Exception
	 */
	public static Method getMethod(Class<?> clazz, String name, Class<?>... parameterTypes) throws Exception {
		Method method = clazz.getDeclaredMethod(name, parameterTypes);
		method.setAccessible(true);
		
		return method;
	}
	
	/**
	 * Invokes the underlying method represented by this {@code Method} object, on the specified object with the specified parameters.
	 * this method delegates to {@link Method#invoke(Object, Object...)}.
     * 
	 * @param object
	 * @param method
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public static Object invoke(Object object, Method method, Object... parameters) throws Exception {
		return method.invoke(object, parameters);
	}
	
}
