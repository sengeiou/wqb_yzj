package com.wqb.commons.toolkit;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @author Shoven
 * @since 2019-04-01 14:14
 */
public class MyCollectors {

    /**
     * 与 Collectors#groupingBy(Function, Collector) 方法返回Map<K, List<T>>不同，本方法返回Map<K, T>
     * 对一个集合分组收集后， 假如分组的键是唯一的，那么分组后的每个集合都只有一个元素，取出这个元素
     *
     * @see Collectors#groupingBy(Function, Collector)
     *
     * @param classifier
     * @param <T> 输入元素类型
     * @param <K> 分组键类型
     * @return
     */
    public static <T, K>
    Collector<T, ?, Map<K, T>> singleResultGroupingBy(Function<? super T, ? extends K> classifier) {
        Collector<T, List<T>, T> collector = new Collector<T, List<T>, T>() {
            @Override
            public Supplier<List<T>> supplier() {
                return ArrayList::new;
            }

            @Override
            public BiConsumer<List<T>, T> accumulator() {
                return List::add;
            }

            @Override
            public BinaryOperator<List<T>> combiner() {
                return (left, right) -> {
                    left.addAll(right);
                    return left;
                };
            }

            @Override
            public Function<List<T>, T> finisher() {
                return list -> {
                    if (list.size() == 1) {
                        return list.get(0);
                    }
                    throw new IllegalArgumentException("分组后的集合数量超过一个");
                };
            }

            @Override
            public Set<Characteristics> characteristics() {
                return Collections.emptySet();
            }
        };

        return  Collectors.groupingBy(classifier, collector);
    }
}
