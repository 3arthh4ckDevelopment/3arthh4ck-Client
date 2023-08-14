package me.earth.earthhack.impl.hud;

import me.earth.earthhack.api.cache.Cache;
import me.earth.earthhack.api.cache.HudCache;
import me.earth.earthhack.api.cache.SettingCache;
import me.earth.earthhack.api.hud.HudElement;
import me.earth.earthhack.api.register.Register;
import me.earth.earthhack.api.setting.Setting;
import me.earth.earthhack.impl.Earthhack;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Allows you to get and store references to modules and Settings
 * that might not have been instantiated yet, or when the ModuleManager
 * should not be loaded yet, wrapped in {@link HudCache}s and
 * {@link SettingCache}s.
 */
public class HudCaches
{
    private static final
        Map<Class<? extends HudElement>, HudCache<? extends HudElement>>
            MODULES = new ConcurrentHashMap<>();
    private static final
        Map<Class<?>, Map<SettingType<?>, SettingCache<?, ?, ?>>>
            SETTINGS = new ConcurrentHashMap<>();
    private static Register<HudElement> moduleManager;

    /**
     * Creates a new HudCache (or gets the already existing one)
     * for a HudElement that is an instance of the given class.
     *
     * @param clazz the class the hud module should belong to.
     * @param <M> the type of hud module.
     * @return a HudCache for the HudElement.
     */
    @SuppressWarnings("unchecked")
    public static <M extends HudElement> HudCache<M> getModule(Class<M> clazz)
    {
        return (HudCache<M>) MODULES.computeIfAbsent(clazz, v ->
                                    new HudCache<>(moduleManager, clazz));
    }

    /**
     * Creates a new SettingCache (or gets the already existing one)
     * for the given HudElement, a Setting with the given Name. The Default
     * Value will be returned by the Cache if the setting is not present.
     *
     * @param module the hud module to get the setting from.
     * @param settingType the type of the setting.
     * @param setting the name of the setting.
     * @param defaultValue the default value of the cache.
     * @param <T> the type of value of the setting.
     * @param <S> the type of setting.
     * @param <E> the type of hud module.
     * @return A settingCache for the Setting.
     */
    @SuppressWarnings("unchecked")
    public static <T, S extends Setting<T>, E extends HudElement>
        SettingCache<T, S, E> getSetting(Class<E> module,
                                         Class<?> settingType,
                                         String setting,
                                         T defaultValue)
    {
        Class<S> converted = (Class<S>) settingType;
        Map<SettingType<?>, SettingCache<?, ?, ?>> inner =
            SETTINGS.computeIfAbsent(module, v -> new ConcurrentHashMap<>());

        SettingType<S> type = new SettingType<>(setting, converted);
        return  (SettingCache<T, S, E>) inner.computeIfAbsent(type, v ->
                {
                    Cache<E> moduleCache = getModule(module);
                    return SettingCache.newHudSettingCache(
                            setting, converted, moduleCache, defaultValue);
                });
    }

    /**
     * Sets the moduleManager for all existing ModuleCaches and future
     * ModuleCaches and tries to make their value present. The SettingCaches
     * use the  ModuleCaches so their value will be made present as well.
     * <p>
     * If a caches value is not present afterwards the Cache will be
     * frozen via {@link Cache#setFrozen(boolean)}.
     *
     * @param moduleManagerIn the moduleManager
     *                     you want to get ModuleReferences from.
     */
    public static void setManager(Register<HudElement> moduleManagerIn)
    {
        moduleManager = moduleManagerIn;
        for (Map.Entry<Class<? extends HudElement>, HudCache<?>> entry :
                MODULES.entrySet())
        {
            entry.getValue().setModuleManager(moduleManagerIn);
            entry.getValue().setFrozen(false);
            if (!entry.getValue().isPresent())
            {
                Earthhack.getLogger().error("HudElement-Caches: couldn't make "
                                        + entry.getKey().getName()
                                        + " present.");
                entry.getValue().setFrozen(true);
            }
        }

        for (Map.Entry<Class<?>, Map<SettingType<?>, SettingCache<?, ?, ?>>>
                    entry : SETTINGS.entrySet())
            {
                if (entry.getValue() != null)
                {
                    for (Map.Entry<SettingType<?>, SettingCache<?, ?, ?>>
                            entry1 : entry.getValue().entrySet())
                {
                    entry1.getValue().setFrozen(false);
                    if (!entry1.getValue().isPresent())
                    {
                        Earthhack.getLogger().error(
                                "Setting-Caches: couldn't make "
                                + entry.getKey().getName()
                                + " - "
                                + entry1.getKey().getName()
                                + " ("
                                + entry1.getKey().getType().getName()
                                + ") present.");

                        entry1.getValue().setFrozen(true);
                    }
                }
            }
        }
    }

    /**
     * For type safety in the Setting Map.
     *
     * @param <S> the type of setting.
     */
    private static final class SettingType<S extends Setting<?>>
    {
        private final Class<S> type;
        private final String name;

        public SettingType(String name, Class<S> type)
        {
            this.name = name;
            this.type = type;
        }

        public Class<S> getType()
        {
            return type;
        }

        public String getName()
        {
            return name;
        }

        @Override
        public boolean equals(Object o)
        {
            if (o instanceof SettingType<?>)
            {
                return ((SettingType<?>) o).type == this.type
                        && this.name.equals(((SettingType<?>) o).name);
            }

            return false;
        }

        @Override
        public int hashCode()
        {
            return name.hashCode();
        }
    }

}
