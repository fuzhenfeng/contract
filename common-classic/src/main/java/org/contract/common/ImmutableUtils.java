package org.contract.common;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import java.util.Collection;
import java.util.Map;

public class ImmutableUtils {
    public static <E> ImmutableList<E> list(Collection<? extends E> elements) {
        return ImmutableList.copyOf(elements);
    }
    public static <K, V> ImmutableMap<K, V> map(Map<? extends K, ? extends V> map) {
        return ImmutableMap.copyOf(map);
    }
    public static <E> ImmutableSet<E> set(Collection<? extends E> elements) {
        return ImmutableSet.copyOf(elements);
    }
}
