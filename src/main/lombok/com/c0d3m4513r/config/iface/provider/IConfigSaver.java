package com.c0d3m4513r.config.iface.provider;

import lombok.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.common.returnsreceiver.qual.This;
import org.checkerframework.dataflow.qual.Deterministic;
import org.checkerframework.dataflow.qual.Pure;

public interface IConfigSaver extends IConfigProvider{
    /**
     * This needs to be Async safe.
     * This will save an Object from the config at the specified config path.
     * @param path Config Path
     * @param typeToken Type of the value stored in the Config
     * @param value Value to be stored in the config
     * @return if the value was saved
     * @param <T> Type of Data stored in the config
     */
    @Pure
    //this is pure, because this does not actually save the value to the config file. This caches the value in memory.
    //And Caching is expressively allowed in a Pure method.
    <T> boolean saveConfigKey(@Nullable T value, @NonNull Class<T> typeToken, @NonNull String path);
    /**
     * This needs to be Async safe.
     * This will save a list from the config at the specified config path.
     * <p>
     * This method expects the actual type of value to be something like List&lt;T&gt;
     * @param path Config Path
     * @param typeToken Type of the value stored in the Config
     * @param value Value to be stored in the config
     * @return if the value was saved
     * @param <T> Type of Data stored in the config
     * @param <V> List Type
     */
    @Pure
    //this is pure, because this does not actually save the value to the config file. This caches the value in memory.
    //And Caching is expressively allowed in a Pure method.
    <V,T> boolean saveConfigKeyList(@Nullable V value, @NonNull Class<T> typeToken, @NonNull String path);

    /**
     * Saves the modified keys to the actual config file
     *
     * @return This
     */
    @Deterministic
    @SuppressWarnings("purity.more.sideeffectfree")
    @This
    IConfigSaver saveToConfig();
}
