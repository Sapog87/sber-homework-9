package org.sber;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Streams<T> {
    private final Supplier<Collection<? extends T>> supplier;

    public Streams(Supplier<Collection<? extends T>> supplier) {
        this.supplier = supplier;
    }

    /**
     * @param src коллекция, из которой будут браться значения
     * @return <code>Streams</code>
     */
    public static <T> Streams<T> of(Collection<? extends T> src) {
        Objects.requireNonNull(src);
        return new Streams<>(() -> src);
    }

    /**
     * @param pred предикат, который будет использоваться для фильтрации
     * @return <code>Streams</code>
     */
    public Streams<T> filter(Predicate<? super T> pred) {
        Objects.requireNonNull(pred);
        return new Streams<>(() -> {
            Collection<? extends T> src = supplier.get();
            Collection<T> dst = new ArrayList<>();
            for (T t : src) {
                if (pred.test(t)) {
                    dst.add(t);
                }
            }
            return dst;
        });
    }

    /**
     * @param func функция, которая будет применена для трансформации значений
     * @return <code>Streams</code>
     */
    public <R> Streams<R> transform(Function<? super T, ? extends R> func) {
        Objects.requireNonNull(func);
        return new Streams<>(() -> {
            Collection<? extends T> src = supplier.get();
            Collection<R> dst = new ArrayList<>(src.size());
            for (T t : src) {
                dst.add(func.apply(t));
            }
            return dst;
        });
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
        Collection<? extends T> src = supplier.get();
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
