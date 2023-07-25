package com.c0d3m4513r.config;

import com.c0d3m4513r.config.iface.provider.IConfigLoader;
import com.c0d3m4513r.config.iface.provider.IConfigLoaderSaver;
import com.c0d3m4513r.config.iface.provider.IConfigSaver;
import com.c0d3m4513r.config.iface.provider.IConfigStorage;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.common.returnsreceiver.qual.This;
import org.checkerframework.dataflow.qual.Pure;

import java.io.File;
import java.util.List;

//todo: https://github.com/awhitford/lombok.maven/issues/163
@SuppressWarnings("type.anno.before.decl.anno")
public final class ConfigStorage {

    public final static IConfigStorage configStorage = new IConfigStorage() {
        @Override
        public @lombok.NonNull IConfigLoader getConfigLoader() {
            return getConfigLoaderSaver();
        }

        @Override
        public @NonNull IConfigSaver getConfigSaver() {
            return getConfigLoaderSaver();
        }
    };
    @MonotonicNonNull
    private static IConfigLoaderSaver configLoaderSaver = null;
    @NonNull
    private static final IConfigLoaderSaver nopConfigLoaderSaver = new IConfigLoaderSaver(){
        @Override
        @Pure
        public @Nullable File getConfigFolder() {
            return null;
        }

        @Override
        @Pure
        public @Nullable File getConfigFile() {
            return null;
        }

        @Override
        @Pure
        public <T> boolean saveConfigKey(@Nullable T value, @NonNull Class<T> typeToken, @NonNull String path) {
            return false;
        }

        @Override
        @Pure
        public <T> boolean saveConfigKeyList(@Nullable List<T> value, @NonNull Class<T> typeToken, @NonNull String path) {
            return false;
        }

        @Override
        @Pure
        public @This IConfigSaver saveToConfig() {
            return this;
        }

        @Override
        @Pure
        public @This IConfigLoader reloadConfigLoader() {
            return this;
        }

        @Override
        @Pure
        public <T> @Nullable T loadConfigKey(String path, Class<T> type) {
            return null;
        }

        @Override
        @Pure
        public @Nullable <T> List<T> loadConfigKeyList(String path, Class<T> type) {
            return null;
        }
    };

    public static void setConfigLoaderSaver(@Nullable IConfigLoaderSaver configLoaderSaver) {
        if (configLoaderSaver == null) return;
        if (ConfigStorage.configLoaderSaver == null) ConfigStorage.configLoaderSaver = configLoaderSaver;
    }

    @Pure
    public static @NonNull IConfigLoaderSaver getConfigLoaderSaver() {
        return configLoaderSaver == null ? nopConfigLoaderSaver : configLoaderSaver;
    }

}
