package com.lifengdi.util;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * @author 李锋镝
 * @date Create at 15:43 2018/8/10
 * @modified by
 */
public class ObjectUtils {

    /**
     * 对象转map
     * @param bean
     * @param <T>
     * @return
     */
    public static <T> Map<String, Object> objectToMap(T bean) {
        Map<String, Object> map = Maps.newHashMap();
        if (bean != null) {
            BeanMap beanMap = BeanMap.create(bean);
            for (Object key : beanMap.keySet()) {
                map.put(key.toString(), beanMap.get(key));
            }
        }
        return map;
    }

    /**
     * 对象转MultiValueMap
     * @param bean
     * @param <T>
     * @return
     */
    public static <T> MultiValueMap<String, String> objectToMultiValueMap(T bean) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        if (bean != null) {
            BeanMap beanMap = BeanMap.create(bean);
            for (Object key : beanMap.keySet()) {
                List list = map.getOrDefault(key, new ArrayList<>());
                Object value = beanMap.get(key);
                if (Objects.nonNull(value)) {
                    list.add(value.toString());
                }
                if (!CollectionUtils.isEmpty(list)) {
                    map.put(key.toString(), list);
                }
            }
        }
        return map;
    }

    /**
     * map转对象
     * @param map
     * @param bean
     * @param <T>
     * @return
     */
    public static <T> T mapToObject(Map<String, Object> map,T bean) {
        BeanMap beanMap = BeanMap.create(bean);
        beanMap.putAll(map);
        return bean;
    }

    /**
     * 利用反射将map转换为对象
     * @param map
     * @param beanClass
     * @return
     * @throws Exception
     */
    public static Object mapToObj(Map<String, Object> map, Class<?> beanClass) throws Exception {
        if (map == null)
            return null;

        Object obj = beanClass.newInstance();

        Field[] fields = getAllFields(obj);
        for (Field field : fields) {
            int mod = field.getModifiers();
            if(Modifier.isStatic(mod) || Modifier.isFinal(mod)){
                continue;
            }

            field.setAccessible(true);

            String type = field.getGenericType().getTypeName();
            Object param = map.get(field.getName());
            if(StringUtils.equals(type,"int")||StringUtils.equals(type,"java.lang.Integer")){
                String v = (String)param;
                if(StringUtils.isNotEmpty(v)){
                    field.set(obj,Integer.parseInt(v));
                }else {
                    continue;
                }
            }else{
                field.set(obj,param);
            }

        }

        return obj;
    }


    private static Field[] getAllFields(Object object){
        Class<?> clazz = object.getClass();
        List<Field> fieldList = new ArrayList<>();
        while (clazz != null) {
            fieldList.addAll(new ArrayList<>(Arrays.asList(clazz.getDeclaredFields())));
            clazz = clazz.getSuperclass();
        }
        Field[] fields = new Field[fieldList.size()];
        fieldList.toArray(fields);
        return fields;
    }

}

