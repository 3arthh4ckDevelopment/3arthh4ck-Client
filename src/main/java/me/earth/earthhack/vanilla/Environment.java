package me.earth.earthhack.vanilla;

import me.earth.earthhack.impl.core.util.AsmUtil;
import me.earth.earthhack.tweaker.EarthhackTweaker;
import net.minecraft.launchwrapper.Launch;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

import java.io.File;
import java.io.IOException;
import java.util.List;

public enum Environment
{
    SEARGE,
    MCP;

    private static Environment environment;

    public static Environment getEnvironment()
    {
        return environment;
    }

    /**
     * {@link EarthhackTweaker#acceptOptions(List, File, File, String)}
     */
    @SuppressWarnings("unused")
    public static void loadEnvironment()
    {
        Environment env = SEARGE;

        String world = "net.minecraft.world.World";
        byte[] bs = null;
        try
        {
            bs = Launch.classLoader.getClassBytes(world);
        }
        catch (IOException ignored) { }
        if (bs != null)
        {
            ClassNode node = new ClassNode();
            ClassReader reader = new ClassReader(bs);
            reader.accept(node, 0);
            if (AsmUtil.findField(node, "loadedEntityList") != null)
            {
                env = MCP;
            }
        }

        environment = env;
    }

}
