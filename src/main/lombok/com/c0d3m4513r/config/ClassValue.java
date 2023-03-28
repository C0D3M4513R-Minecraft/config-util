package com.c0d3m4513r.config;

import lombok.*;
import org.checkerframework.dataflow.qual.Pure;

/**
 * A class that stores a value, and a class.
 * The class and the value do not need to be the same type, to allow for more flexibility.
 * @param <V> Type of the value
 * @param <T> Type of the class
 */
@Value
@NonNull
@Getter(onMethod_ = {@Pure})
public class ClassValue<V,T> {
    @org.checkerframework.checker.nullness.qual.NonNull
    V value;
    @org.checkerframework.checker.nullness.qual.NonNull
    Class<T> clazz;
}
