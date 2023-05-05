package com.c0d3m4513r.config.ConfigEntry;

import com.c0d3m4513r.config.ClassValue;
import com.c0d3m4513r.config.ConfigStorage;
import com.c0d3m4513r.config.iface.key.IConfigLoadable;
import com.c0d3m4513r.config.iface.key.IConfigLoadableSaveable;
import com.c0d3m4513r.config.iface.provider.IConfigStorage;
import com.c0d3m4513r.logger.Logging;
import lombok.*;
import lombok.experimental.NonFinal;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.dataflow.qual.Pure;
import org.checkerframework.dataflow.qual.SideEffectFree;

/**
 * A config entry stores a value at a config path.
 * Which config to load a value from is specified by the implementation of this class.
 * @param <V> This is the regular type, e.g. List, String or whatever
 * @param <T> This is the due to type erasure, and contains the type of the List elements or Type V
 */
@Value
@AllArgsConstructor(onConstructor_ = {@SideEffectFree, @SuppressWarnings("purity.not.sideeffectfree.assign.field")})
@NonFinal
@ToString(doNotUseGetters = true)
@Setter(AccessLevel.PROTECTED)
//todo: https://github.com/awhitford/lombok.maven/issues/163
@SuppressWarnings("type.anno.before.decl.anno")
public abstract class SuperConfigEntry<V,T> implements IConfigLoadableSaveable {
    /**
     * This contains the value of the config entry, and the type of the value.
     * What the type means is dependent on the superclass.
     */
    @NonFinal
    protected @NonNull ClassValue<V,T> value;
    /**
     * The path to the config value
     */
    protected @NonNull String configPath;
    /**
     * Should the value be printed to the console when it is loaded from the config?
     */
    protected boolean printValue;
    @NonNull
    protected IConfigStorage configStorage;

    /**
     * Creates a new ConfigEntry, with the specified value, type and config path.
     * Will pull the config storage from the {@link ConfigStorage} class.
     * The value will be printed to the console when it changes.
     * @param value The type and value to be stored in the config
     * @param configPath The path to the config value
     */
    @SideEffectFree
    public SuperConfigEntry(@NonNull final ClassValue<V,T> value, @NonNull final String configPath){
        this(value,configPath,true,ConfigStorage.configStorage);
    }
    /**
     * Creates a new ConfigEntry, with the specified value, type and config path.
     * The value will be printed to the console when it changes.
     * @param value The type and value to be stored in the config
     * @param configPath The path to the config value
     * @param storage The config loader and saver that will be used
     */
    @SideEffectFree
    public SuperConfigEntry(@NonNull final ClassValue<V,T> value, @NonNull final String configPath, @NonNull IConfigStorage storage){
        this(value,configPath,true,storage);
    }

    /**
     * Creates a new ConfigEntry, with the specified value, type and config path.
     * Will pull the config storage from the {@link ConfigStorage} class.
     * @param value The type and value to be stored in the config
     * @param configPath The path to the config value
     * @param printValue Should the value be printed to the console when it is loaded from the config?
     */
    @SideEffectFree
    public SuperConfigEntry(@NonNull final ClassValue<V,T> value, @NonNull final String configPath, final boolean printValue){
        this(value,configPath,printValue,ConfigStorage.configStorage);
    }

    @Pure
    protected abstract @Nullable V getValueFromLoader();

    /**
     * @see IConfigLoadable#loadValue()
     */
    public void loadValue(){
        V val = getValueFromLoader();
        if(val!=null){
            if (!value.getValue().equals(val)){
                if (printValue)
                    Logging.INSTANCE.info("[API] For config string '{}' replacing '{}' with new Value '{}'",configPath,value.getValue(),val);
                else
                    Logging.INSTANCE.info("[API] Updating config key '{}'. Value is hidden for security.", configPath);
            }
            value=new ClassValue<>(val,value.getClazz());
        }else{
            Logging.INSTANCE.debug("[API] No value from load value. Saving "+configPath);
            saveValue();
        }
    }

    /**
     * Gets the value of the config entry
     * @return Returns the value of the config entry
     */
    @Pure
    public @NonNull V getValue(){
        return value.getValue();
    }
}
