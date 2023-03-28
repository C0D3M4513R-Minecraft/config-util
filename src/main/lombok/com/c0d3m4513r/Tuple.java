package com.c0d3m4513r;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;
import org.checkerframework.dataflow.qual.Pure;
import org.checkerframework.dataflow.qual.SideEffectFree;

/**
 * A simple tuple class
 * @param <V1> the type of the left value
 * @param <V2> the type of the right value
 */
@Value
@Getter(onMethod_ = {@Pure})
@AllArgsConstructor(onConstructor_ = {@SideEffectFree, @SuppressWarnings("purity.not.sideeffectfree.assign.field")})
public class Tuple<V1, V2> {
    V1 left;
    V2 right;
}
