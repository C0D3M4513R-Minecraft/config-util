package com.c0d3m4513r.config.iface.provider;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.dataflow.qual.Pure;

public interface IConfigStorage {
    @Pure
    @NonNull IConfigLoader getConfigLoader();

    @Pure
    @NonNull IConfigSaver getConfigSaver();
}
