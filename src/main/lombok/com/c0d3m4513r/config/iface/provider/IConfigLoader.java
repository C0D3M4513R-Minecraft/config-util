package com.c0d3m4513r.config.iface.provider;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.common.returnsreceiver.qual.This;
import org.checkerframework.dataflow.qual.Deterministic;
import org.checkerframework.dataflow.qual.Pure;
import java.util.List;

public interface IConfigLoader extends IConfigProvider {
    /***
     * This needs to be Async safe.
     * This will load an Object from the config at the specified config path.
     * @param path Config Path
     * @param type Type of the value stored in the Config
     * @return Returns null or a valid Object of the specified type
     * @param <T> Type of Data stored in the config
     */
    @Pure
    <T> @Nullable T loadConfigKey(String path, Class<T> type);
    /***
     * This needs to be Async safe.
     * This will load a list of Objects from the config at the specified config path.
     * @param path Config Path
     * @param type Type of the value stored in the list
     * @return Returns null or a valid Object of the specified type
     * @param <T> Type of Data stored in the config
     */
    @Pure
    <T> @Nullable List<T> loadConfigKeyList(String path, Class<T> type);

    /***
     * Updates the config Loader, and loads new keys.
     * Errors should be logged to the console.
     */
    @Deterministic
    @SuppressWarnings("purity.more.sideeffectfree")
    @This
    IConfigLoader reloadConfigLoader();
}
