package ru.khaziev.TelegramBotModule.util;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractCache<K,V> {

    public Map<K, SoftReference<V>> cache = new ConcurrentHashMap<>();

    public V get(K key) throws IOException {

        SoftReference<V> refValue  = cache.get(key);

        V value = (refValue == null) ? null : refValue.get();

        if (value == null) {
            value = load(key);
        }

        return value;
    }

    protected abstract V load(K key) throws IOException;

    public void put(K key, V value) {
        cache.put(key,new SoftReference<>(value));
    }

}
