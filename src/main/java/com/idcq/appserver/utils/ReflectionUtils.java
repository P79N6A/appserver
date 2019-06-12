package com.idcq.appserver.utils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;

import com.idcq.appserver.dto.user.UserDto;
  
/** 
 *  
 * 反射的 Utils 函数集合 
 * 提供访问私有变量, 获取泛型类型 Class, 提取集合中元素属性等 Utils 函数 
 * 
 */  
public class ReflectionUtils {  
    /** 
     * 将反射时的 "检查异常" 转换为 "运行时异常" 
     * @return 
     */  
    public static IllegalArgumentException convertToUncheckedException(Exception ex){  
        if(ex instanceof IllegalAccessException || ex instanceof IllegalArgumentException  
                || ex instanceof NoSuchMethodException){  
            throw new IllegalArgumentException("反射异常", ex);  
        }else{  
            throw new IllegalArgumentException(ex);  
        }  
    }  
      
    /** 
     * 转换字符串类型到 toType 类型, 或 toType 转为字符串 
     * @param value:  待转换的字符串 
     * @param toType: 提供类型信息的 Class, 可以是基本数据类型的包装类或指定格式日期型 
     * @return 
     */  
    public static Object convertValue(Object value, Class<?> toType){  
        try {  
           /* DateConverter dc = new DateConverter();  
              
            dc.setUseLocaleFormat(true);  
            dc.setPatterns(new String[]{"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss"});  
  
            ConvertUtils.register(dc, Date.class); */ 
              
            return ConvertUtils.convert(value, toType);  
        } catch (Exception e) {  
            e.printStackTrace();  
            throw convertToUncheckedException(e);  
        }  
    }  
  
    /** 
     * 提取集合中的对象的属性(通过 getter 方法), 组成 List 
     * @param collection: 来源集合 
     * @param propertyName: 要提取的属性名 
     * @return 
     */  
    @SuppressWarnings("unchecked")  
    public static List fetchElementPropertyToList(Collection collection, String propertyName){  
        List list = new ArrayList();  
          
        try {  
            for(Object obj: collection){  
                list.add(PropertyUtils.getProperty(obj, propertyName));  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
            convertToUncheckedException(e);  
        }  
          
        return list;  
    }  
      
    /** 
     * 提取集合中的对象属性(通过 getter 函数), 组合成由分隔符分隔的字符串 
     * @param collection: 来源集合 
     * @param propertyName: 要提取的属性名 
     * @param seperator: 分隔符 
     * @return 
     */  
    @SuppressWarnings("unchecked")  
    public static String fetchElementPropertyToString(Collection collection, String propertyName,   
            String seperator){  
        List list = fetchElementPropertyToList(collection, propertyName);  
        return StringUtils.join(list, seperator);  
    }  
      
    /** 
     * 通过反射, 获得定义 Class 时声明的父类的泛型参数的类型 
     * 如: public EmployeeDao extends BaseDao<Employee, String> 
     * @param clazz 
     * @param index 
     * @return 
     */  
    @SuppressWarnings("unchecked")  
    public static Class getSuperClassGenricType(Class clazz, int index){  
        Type genType = clazz.getGenericSuperclass();  
          
        if(!(genType instanceof ParameterizedType)){  
            return Object.class;  
        }  
          
        Type [] params = ((ParameterizedType)genType).getActualTypeArguments();  
          
        if(index >= params.length || index < 0){  
            return Object.class;  
        }  
          
        if(!(params[index] instanceof Class)){  
            return Object.class;  
        }  
          
        return (Class) params[index];  
    }  
      
    /** 
     * 通过反射, 获得 Class 定义中声明的父类的泛型参数类型 
     * 如: public EmployeeDao extends BaseDao<Employee, String> 
     * @param <T> 
     * @param clazz 
     * @return 
     */  
    @SuppressWarnings("unchecked")  
    public static<T> Class<T> getSuperGenericType(Class clazz){  
        return getSuperClassGenricType(clazz, 0);  
    }  
      
    /** 
     * 循环向上转型, 获取对象的 DeclaredMethod 
     * @param object 
     * @param methodName 
     * @param parameterTypes 
     * @return 
     */  
    public static Method getDeclaredMethod(Object object, String methodName, Class<?>[] parameterTypes){  
          
        for(Class<?> superClass = object.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()){  
            try {  
                //superClass.getMethod(methodName, parameterTypes);  
                return superClass.getDeclaredMethod(methodName, parameterTypes);  
            } catch (NoSuchMethodException e) {  
                //Method 不在当前类定义, 继续向上转型  
            }  
            //..  
        }  
          
        return null;  
    }  
      
    /** 
     * 使 filed 变为可访问 
     * @param field 
     */  
    public static void makeAccessible(Field field){  
        if(!Modifier.isPublic(field.getModifiers())){  
            field.setAccessible(true);  
        }  
    }  
      
    /** 
     * 循环向上转型, 获取对象的 DeclaredField 
     * @param object 
     * @param filedName 
     * @return 
     */  
    public static Field getDeclaredField(Object object, String filedName){  
          
        for(Class<?> superClass = object.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()){  
            try {  
                return superClass.getDeclaredField(filedName);  
            } catch (NoSuchFieldException e) {  
                //Field 不在当前类定义, 继续向上转型  
            }  
        }  
        return null;  
    }  
      
    /** 
     * 直接调用对象方法, 而忽略修饰符(private, protected) 
     * @param object 
     * @param methodName 
     * @param parameterTypes 
     * @param parameters 
     * @return 
     * @throws InvocationTargetException  
     * @throws IllegalArgumentException  
     */  
    public static Object invokeMethod(Object object, String methodName, Class<?> [] parameterTypes,  
            Object [] parameters) throws InvocationTargetException{  
          
        Method method = getDeclaredMethod(object, methodName, parameterTypes);  
          
        if(method == null){  
            throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + object + "]");  
        }  
          
        method.setAccessible(true);  
          
        try {  
            return method.invoke(object, parameters);  
        } catch(IllegalAccessException e) {  
  
        }   
          
        return null;  
    }  
      
    /** 
     * 直接设置对象属性值, 忽略 private/protected 修饰符, 也不经过 setter 
     * @param object 
     * @param fieldName 
     * @param value 
     */  
    public static void setFieldValue(Object object, String fieldName, Object value){  
        Field field = getDeclaredField(object, fieldName);  
          
        if (field == null)  
            throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");  
          
        makeAccessible(field);  
          
        try {  
            field.set(object, value);  
        } catch (IllegalAccessException e) {  
  
        }  
    }  
      
    /** 
     * 直接读取对象的属性值, 忽略 private/protected 修饰符, 也不经过 getter 
     * @param object 
     * @param fieldName 
     * @return 
     */  
    public static Object getFieldValue(Object object, String fieldName){  
        Field field = getDeclaredField(object, fieldName);  
          
        if (field == null)  
            throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");  
          
        makeAccessible(field);  
          
        Object result = null;  
          
        try {  
            result = field.get(object);  
        } catch (IllegalAccessException e) {  
  
        }  
          
        return result;  
    }  
    
    /**
     * 检测属性值是否存在
     * @param entryClass
     * @param fieldName
     * @return
     */
    public static boolean checkFieldIsExist(Class entityClass,String fieldName)
    {
    	try {
			entityClass.getDeclaredField(fieldName);
			return true;
		} catch (Exception e) {
			return false;
		}
    }
    
    /**
     * 设置值如果值不为空
     * @param obj
     * @param fieldName
     * @param value
     */
    public static void setValueNotEmpty(Object obj,String fieldName,String value)
    {
    	if(StringUtils.isNotEmpty(value))
    	{	
	    	if(checkFieldIsExist(obj.getClass(), fieldName))
	    	{
	    		Object convertValue=convertValue(value,getDeclaredField(obj, fieldName).getType());
	    		setFieldValue(obj, fieldName, convertValue);
	    	}
    	}
    }
    
   
    /**
     * 将一个 Map 对象转化为一个 JavaBean
     * @param type 要转化的类型
     * @param map 包含属性值的 map
     * @return 转化出来的 JavaBean 对象
     * @throws IntrospectionException
     *             如果分析类属性失败
     * @throws IllegalAccessException
     *             如果实例化 JavaBean 失败
     * @throws InstantiationException
     *             如果实例化 JavaBean 失败
     * @throws InvocationTargetException
     *             如果调用属性的 setter 方法失败
     */
    public static Object convertMap(Class type, Map map)
            throws IntrospectionException, IllegalAccessException,
            InstantiationException, InvocationTargetException {
        BeanInfo beanInfo = Introspector.getBeanInfo(type); // 获取类属性
        Object obj = type.newInstance(); // 创建 JavaBean 对象

        // 给 JavaBean 对象的属性赋值
        PropertyDescriptor[] propertyDescriptors =  beanInfo.getPropertyDescriptors();
        for (int i = 0; i< propertyDescriptors.length; i++) {
            PropertyDescriptor descriptor = propertyDescriptors[i];
            String propertyName = descriptor.getName();
            if (map.containsKey(propertyName)) {
                // 下面一句可以 try 起来，这样当一个属性赋值失败的时候就不会影响其他属性赋值。
                Object value = map.get(propertyName);
                Object[] args = new Object[1];
                args[0] = value;

                descriptor.getWriteMethod().invoke(obj, args);
            }
        }
        return obj;
    }
    
    /**
     * 将一个 JavaBean 对象转化为一个  Map
     * @param bean 要转化的JavaBean 对象
     * @return 转化出来的  Map 对象
     * @throws IntrospectionException 如果分析类属性失败
     * @throws IllegalAccessException 如果实例化 JavaBean 失败
     * @throws InvocationTargetException 如果调用属性的 setter 方法失败
     */
    public static Map convertBean(Object bean)
            throws IntrospectionException, IllegalAccessException, InvocationTargetException {
        Class type = bean.getClass();
        Map returnMap = new HashMap();
        BeanInfo beanInfo = Introspector.getBeanInfo(type);

        PropertyDescriptor[] propertyDescriptors =  beanInfo.getPropertyDescriptors();
        for (int i = 0; i< propertyDescriptors.length; i++) {
            PropertyDescriptor descriptor = propertyDescriptors[i];
            String propertyName = descriptor.getName();
            if (!propertyName.equals("class")) {
                Method readMethod = descriptor.getReadMethod();
                Object result = readMethod.invoke(bean, new Object[0]);
                if (result != null) {
                    returnMap.put(propertyName, result);
                } 
            }
        }
        return returnMap;
    }
    
    
    public static void main(String[] args) throws IllegalAccessException, InvocationTargetException, IntrospectionException, InstantiationException {
		UserDto user = new UserDto();
		user.setAddress("aaa");
		user.setUserId(111L);
		user.setMobile("12345679");
		Map map = convertBean(user);
		System.out.println(map.toString());
		UserDto u = (UserDto) convertMap(UserDto.class, map);
		System.out.println(u.toString());
		
	}
}  
