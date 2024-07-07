package me.earth.earthhack.impl;

import me.earth.earthhack.api.util.interfaces.Globals;
import me.earth.earthhack.impl.core.ducks.IMinecraft;
import me.earth.earthhack.impl.managers.Managers;
import me.earth.earthhack.impl.managers.thread.GlobalExecutor;
import me.earth.earthhack.impl.modules.client.commands.Commands;
import me.earth.earthhack.impl.util.math.geocache.Sphere;
import me.earth.earthhack.impl.util.render.Icon;
import me.earth.earthhack.impl.util.render.loadingscreen.SplashScreenHelper;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;

@Mod(modid = Earthhack.MODID, name = Earthhack.NAME, version = Earthhack.VERSION)
public class Earthhack implements Globals
{
    private static final Logger LOGGER = LogManager.getLogger("3arthh4ck");
    public static final String NAME = "3arthh4ck";
    public static final String MODID = "earthhack";
    public static final String VERSION = "2.0.0";
    public static long startMS;

    public static void preInit()
    {
        startMS = System.currentTimeMillis();
        GlobalExecutor.EXECUTOR.submit(() -> Sphere.cacheSphere(LOGGER));
    }

    public static void init() {
        LOGGER.info("\n\n ------------------ Initializing 3arthh4ck. ------------------ \n");
        SplashScreenHelper.setSplashScreen("Initializing 3arthh4ck", 7);
        Display.setTitle(NAME + " - " + VERSION);
        Managers.load();
        LOGGER.info("Prefix is " + Commands.getPrefix());
        SplashScreenHelper.clear();
        LOGGER.info("\n\n ------------------ 3arthh4ck initialized ------------------ \n");
    }

    public static void postInit()
    {
        Icon.setIcon();
    }
    
    public static Logger getLogger()
    {
        return LOGGER;
    }

    public static boolean isRunning()
    {
        return ((IMinecraft) mc).isEarthhackRunning();
    }

}