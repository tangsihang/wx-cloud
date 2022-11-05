package com.tencent.wxcloudrun.common.utils;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author tangsh
 * @date 2022/11/05
 */
public class DataUtils {

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> set = ConcurrentHashMap.newKeySet();
        return t -> set.add(keyExtractor.apply(t));
    }
}
