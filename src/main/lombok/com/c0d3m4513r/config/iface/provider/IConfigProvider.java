package com.c0d3m4513r.config.iface.provider;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.dataflow.qual.Pure;

import java.io.File;

public interface IConfigProvider {
    /**
     * @return The folder in which the config file resides.
     */
    @Pure
    @SuppressWarnings("unused")
    @Nullable File getConfigFolder();
    /**
     * This method returns the config file being used, or null in case the config is not stored as a file.
     * @return The config file, being read by this provider.
     */
    @Pure
    @SuppressWarnings("unused")
    @Nullable File getConfigFile();
}
