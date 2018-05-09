package com.example.asus.lantalk.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

/**
 * Created by asus on 18-5-9.
 */

public class CrazyitMap<K, V> {

    public Map<K, V> map = (Map<K, V>) Collections.synchronizedMap(new HashMap<>());

    public synchronized void removeByValue(Object value) {
        for (Object key : map.keySet()) {
            if (map.get(key) == value) {
                map.remove(key);
                break;
            }
        }
    }


    public synchronized Set<V> valueSet() {
        Set<V> result = new HashSet<>();
        List<String> strings = new ArrayList<>();
        strings.addAll((ArrayList) map.keySet());
        for (String s : strings) {
            result.add(map.get(s));
        }
        return result;
    }

    public synchronized K getKeyByValue(V val) {
        for (K key : map.keySet()) {
            if (map.get(key) == val || map.get(key).equals(val)) {
                return key;
            }
        }
        return null;
    }


    public synchronized V put(K key, V value) {
        for (V val : valueSet()) {
            if (val.equals(value) && val.hashCode() == value.hashCode()) {
                throw new RuntimeException("不允许有重复的value");
            }
        }

        return map.put(key, value);
    }
}
