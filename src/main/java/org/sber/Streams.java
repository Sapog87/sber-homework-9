package org.sber;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

public class Streams<T> {
    private final Collection<? extends T> src;

    private Streams(Collection<? extends T> src) {
        this.src = src;
    }

    /**
     * @param src коллекция, из которой будут браться значения
     * @return <code>Streams</code>
     */
    public static <T> Streams<T> of(Collection<? extends T> src) {
        Objects.requireNonNull(src);
        return new Streams<>(src);
    }

    /**
     * @param pred предикат, который будет использоваться для фильтрации
     * @return <code>Streams</code>
     */
    public Streams<T> filter(Predicate<? super T> pred) {
        Objects.requireNonNull(pred);
        Collection<T> dst = new ArrayList<>();
        for (T t : src) {
            if (pred.test(t)) {
                dst.add(t);
            }
        }
        return new Streams<>(dst);
    }

    /**
     * @param func функция, которая будет применена для трансформации значений
     * @return <code>Streams</code>
     */
    public <R> Streams<R> transform(Function<? super T, ? extends R> func) {
        Objects.requireNonNull(func);
        Collection<R> dst = new ArrayList<>(src.size());
        for (T t : src) {
            dst.add(func.apply(t));
        }
        return new Streams<>(dst);
    }

    /**
     * @param keyMapper   функция для трансформации значения из Streams в ключ для Map
     * @param valueMapper функция для трансформации значения из Streams в значение для Map
     * @return <code>Map</code>
     */
    public <K, V> Map<K, V> toMap(Function<? super T, ? extends K> keyMapper,
                                  Function<? super T, ? extends V> valueMapper) {
        Objects.requireNonNull(keyMapper);
        Objects.requireNonNull(valueMapper);
        Map<K, V> map = new HashMap<>();
        for (T t : src) {
            K key = keyMapper.apply(t);
            if (map.containsKey(key)) {
                throw new IllegalArgumentException("Duplicate key: %s".formatted(keyMapper.apply(t)));
            }
            map.put(key, valueMapper.apply(t));
        }
        return map;
    }
}
