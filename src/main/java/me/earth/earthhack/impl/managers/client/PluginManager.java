package me.earth.earthhack.impl.managers.client;

import me.earth.earthhack.api.config.Jsonable;
import me.earth.earthhack.api.plugin.Plugin;
import me.earth.earthhack.api.plugin.PluginConfig;
import me.earth.earthhack.impl.Earthhack;
import me.earth.earthhack.impl.core.Core;
import me.earth.earthhack.impl.managers.client.exception.BadPluginException;
import me.earth.earthhack.impl.util.misc.ReflectionUtil;

import java.io.File;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * Manages {@link Plugin}s for 3arthh4ck.
 */
public class PluginManager
{
    private static final PluginManager INSTANCE = new PluginManager();
    private static final String EARTH_PATH = "earthhack/plugins";

    private final Map<PluginConfig, Plugin> plugins = new HashMap<>();
    private final Map<String, PluginConfig> configs = new HashMap<>();
    private final PluginRemapper remapper = new PluginRemapper();
    private ClassLoader classLoader;

    /** Private Ctr since this is a Singleton. */
    private PluginManager() { }

    /** @return the Singleton Instance for this Manager. */
    public static PluginManager getInstance()
    {
        return INSTANCE;
    }

    /**
     * Used by {@link Core#init(ClassLoader)}.
     *
     * Scans the "earthhack/plugins" folder for Plugins.
     * If it can find jarFiles whose Manifest contain a
     * "3arthh4ckConfig" the jar will be added to the classPath
     * and a {@link PluginConfig} will be created. If the PluginConfig
     * contains a "mixinConfig" entry that MixinConfig will be added by
     * the CoreMod.
     *
     * @param pluginClassLoader the classLoader to load Plugins with.
     */
    public void createPluginConfigs(ClassLoader pluginClassLoader)
    {
        if (!(pluginClassLoader instanceof URLClassLoader))
        {
            throw new IllegalArgumentException("PluginClassLoader was not" +
                    " an URLClassLoader, but: "
                    + pluginClassLoader.getClass().getName());
        }

        this.classLoader = pluginClassLoader;
        Core.LOGGER.info("PluginManager: Scanning for PluginConfigs.");

        File d = new File(EARTH_PATH);
        Map<String, File> remap = scanPlugins(d.listFiles(), pluginClassLoader);
        remap.keySet().removeAll(configs.keySet());

        try
        {
            File[] remappedPlugins = remapper.remap(remap.values());
            scanPlugins(remappedPlugins, pluginClassLoader);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private Map<String, File> scanPlugins(File[] files,
                                          ClassLoader pluginClassLoader)
    {
        Map<String, File> remap = new HashMap<>();

        try
        {
            for (File file : Objects.requireNonNull(files))
            {
                if (file.getName().endsWith(".jar"))
                {
                    Core.LOGGER.info("PluginManager: Scanning "
                            + file.getName());
                    try
                    {
                        scanJarFile(file, pluginClassLoader);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return remap;
    }

    /**
     * Called by {@link 3arthh4ck}.
     *
     * Instantiates all found Plugins.
     */
    public void instantiatePlugins()
    {
        for (PluginConfig config : configs.values())
        {
            if (plugins.containsKey(config))
            {
                Earthhack.getLogger().error("Can't register Plugin "
                        + config.getName()
                        + ", a plugin with that name is already registered.");
                continue;
            }

            Earthhack.getLogger().info("Instantiating: "
                                    + config.getName()
                                    + ", MainClass: "
                                    + config.getMainClass());
            try
            {
                Class<?> clazz = Class.forName(config.getMainClass());
                Constructor<?> constructor = clazz.getConstructor();
                constructor.setAccessible(true);
                Plugin plugin = (Plugin) constructor.newInstance();
                plugins.put(config, plugin);
            }
            catch (Throwable e)
            {
                Earthhack.getLogger().error("Error instantiating : "
                        + config.getName() + ", caused by:");

                e.printStackTrace();
            }
        }
    }

    private void scanJarFile(File file,
                             ClassLoader pluginClassLoader)
            throws Exception
    {
        try (JarFile jarFile = new JarFile(file)) {

            Manifest manifest = jarFile.getManifest();
            Attributes attributes = manifest.getMainAttributes();
            String configName = attributes.getValue("3arthh4ckConfig");

            if (configName == null) {
                throw new BadPluginException(jarFile.getName() + ": Manifest doesn't provide a 3arthh4ckConfig!");
            }

            // ._.
            ReflectionUtil.addToClassPath((URLClassLoader) pluginClassLoader, file);

            PluginConfig config = Jsonable.GSON.fromJson(
                    new InputStreamReader(
                            Objects.requireNonNull(
                                    pluginClassLoader.getResourceAsStream(configName))),
                    PluginConfig.class);

            if (config == null) {
                throw new BadPluginException(jarFile.getName()
                        + ": Found a PluginConfig, but couldn't instantiate it.");
            }

            Core.LOGGER.info("Found PluginConfig: "
                    + config.getName()
                    + ", MainClass: "
                    + config.getMainClass()
                    + ", Mixins: "
                    + config.getMixinConfig());

            configs.put(configName, config);
        }
    }

    /**
     * @return a map of all found PluginConfigs.
     */
    public Map<String, PluginConfig> getConfigs()
    {
        return configs;
    }

    /**
     * @return a Map of all found Plugins with their names as keys.
     */
    public Map<PluginConfig, Plugin> getPlugins()
    {
        return plugins;
    }

    public ClassLoader getPluginClassLoader()
    {
        return classLoader;
    }

}
