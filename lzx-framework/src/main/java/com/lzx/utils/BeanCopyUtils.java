package com.lzx.utils;

import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lzx
 */
public class BeanCopyUtils {
    /**
     * 设置无参构造为private，以便只使用静态方法
     */
    private BeanCopyUtils(){

    }
    /**
     *
     * @param source 被copy的对象
     * @param clazz copy目标的class
     * @return
     * @param <V>
     */
    public static <S, V> V copyBean(S source, Class<V> clazz) {
        V result = null;
        try {
            result = clazz.newInstance();
            //实现copy对象
            BeanUtils.copyProperties(source, result);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     *
     * @param sourceList 被copy的对象列表
     * @param clazz copy目标的class
     * @return
     * @param <S>
     * @param <V>
     */
    public static <S, V> List<V> copyBeanList(List<S> sourceList, Class<V> clazz){
        return sourceList.stream()
                .map(s -> copyBean(s, clazz))
                .collect(Collectors.toList());
    }
}
