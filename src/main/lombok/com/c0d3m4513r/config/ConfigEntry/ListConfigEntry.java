package com.c0d3m4513r.config.ConfigEntry;

import com.c0d3m4513r.config.ClassValue;
import com.c0d3m4513r.config.iface.provider.IConfigStorage;
import lombok.EqualsAndHashCode;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.dataflow.qual.Pure;
import org.checkerframework.dataflow.qual.SideEffectFree;

import java.util.List;

/**
 * @param <T> The type of elements inside the list
 */
@EqualsAndHashCode(callSuper = true)
public class ListConfigEntry<T> extends SuperConfigEntry<List<T>,T>{

    /**
     * Creates a new ConfigEntry, with the specified value, type and config path.
     * The value will be printed to the console when it changes.
     * @param value The type and value to be stored in the config
     * @param configPath The path to the value in the config
     */
    @SideEffectFree
    public ListConfigEntry(@NonNull final ClassValue<List<T>, T> value, @NonNull final String configPath) {
        super(value,configPath);
    }

    /**
     * Creates a new ConfigEntry, with the specified value, type and config path.
     * If the value should be printed to the console when it changes, is specified by the printValue parameter.
     * @param value The type and value to be stored in the config
     * @param configPath The path to the value in the config
     * @param printValue If the value should be printed to the console when it changes
     */
    @SideEffectFree
    public ListConfigEntry(@NonNull final ClassValue<List<T>, T> value, @NonNull final String configPath, final boolean printValue) {
        super(value,configPath, printValue);
    }

    /**
     * Creates a new ConfigEntry, with the specified value, type and config path.
     * If the value should be printed to the console when it changes, is specified by the printValue parameter.
     * @param value The type and value to be stored in the config
     * @param configPath The path to the value in the config
     * @param printValue If the value should be printed to the console when it changes
     * @param configStorage The config storage to be used, to get config values
     */
    @SideEffectFree
    public ListConfigEntry(@NonNull final ClassValue<List<T>, T> value, @NonNull final String configPath, final boolean printValue, @NonNull final IConfigStorage configStorage){
        super(value,configPath, printValue, configStorage);
    }

    @Override
    @Pure
    protected @Nullable List<T> getValueFromLoader() {
        return configStorage.getConfigLoader().loadConfigKeyList(configPath,value.getClazz());
    }

    @Override
    @SuppressWarnings({"purity.more.deterministic","purity.more.pure"})
    @SideEffectFree
    public void saveValue(){
        configStorage.getConfigSaver().saveConfigKeyList(value.getValue(),value.getClazz(), configPath);
    }
}