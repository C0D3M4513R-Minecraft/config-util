package com.c0d3m4513r.config.ConfigEntry;

import com.c0d3m4513r.config.ConfigStorage;
import com.c0d3m4513r.config.ClassValue;
import com.c0d3m4513r.config.iface.provider.IConfigLoader;
import com.c0d3m4513r.config.iface.key.IConfigSavable;
import com.c0d3m4513r.config.iface.provider.IConfigSaver;
import com.c0d3m4513r.config.iface.provider.IConfigStorage;
import lombok.*;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.dataflow.qual.Pure;
import org.checkerframework.dataflow.qual.SideEffectFree;

/**
 * A config entry that stores a value at a config path inside the {@link ConfigStorage#getConfigLoaderSaver()}.
 * @param <T> The type of value stored at the specified config path
 */
@EqualsAndHashCode(callSuper = true)
public class ConfigEntry<T> extends SuperConfigEntry<T,T>{
    /**
     * Creates a new ConfigEntry, with the specified value, type and config path.
     * The value will be printed to the console when it changes.
     * @param value The type and value to be stored in the config
     * @param configPath The path to the value in the config
     */
    @SideEffectFree
    public ConfigEntry(@NonNull final ClassValue<T, T> value,@NonNull final String configPath) {
        super(value,configPath, true);
    }

    /**
     * Creates a new ConfigEntry, with the specified value, type and config path.
     * If the value should be printed to the console when it changes, is specified by the printValue parameter.
     * @param value The type and value to be stored in the config
     * @param configPath The path to the value in the config
     * @param printValue If the value should be printed to the console when it changes
     */
    @SideEffectFree
    public ConfigEntry(@NonNull final ClassValue<T, T> value,@NonNull final String configPath, final boolean printValue){
        super(value,configPath, printValue);
    }

    /**
     * Creates a new ConfigEntry, with the specified value, type and config path.
     * If the value should be printed to the console when it changes, is specified by the printValue parameter.
     * @param value The type and value to be stored in the config
     * @param configPath The path to the value in the config
     * @param printValue If the value should be printed to the console when it changes
     * @param configStorage The config storage to use
     */
    @SideEffectFree
    public ConfigEntry(@NonNull final ClassValue<T, T> value,@NonNull final String configPath,final boolean printValue, @NonNull final IConfigStorage configStorage){
        super(value,configPath, printValue, configStorage);
    }

    @Pure
    protected IConfigLoader getConfigLoader(){
        return ConfigStorage.getConfigLoaderSaver();
    }

    @Pure
    protected IConfigSaver getConfigSaver(){
        return ConfigStorage.getConfigLoaderSaver();
    }

    /** Loads a value from the config, and returns it.
     * @return The value from the config, or null if the value could not be loaded or isn't present in the config
     */
    @Pure
    @Override
    @Nullable
    protected T getValueFromLoader() {
        return getConfigLoader().loadConfigKey(configPath,value.getClazz());
    }

    /**
     * @see IConfigSavable#saveValue()
     */
    @SideEffectFree
    @SuppressWarnings({"purity.more.pure","purity.more.deterministic"})
    @Override
    public void saveValue(){
        getConfigSaver().saveConfigKey(value.getValue(),value.getClazz(), configPath);
    }
}