package me.earth.earthhack.impl.util.render;

import me.earth.earthhack.impl.Earthhack;
import me.earth.earthhack.impl.modules.Caches;
import me.earth.earthhack.impl.modules.client.management.Management;
import me.earth.earthhack.impl.util.render.image.ImageUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Util;
import org.lwjgl.opengl.Display;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;


public class Icon {

    public static void setIcon() {
        if (Caches.getModule(Management.class).get().icon.getValue()) {
            if (Util.getOSType() != Util.EnumOS.OSX) {
                try {
                    InputStream inputstream = Minecraft.class.getResourceAsStream("/assets/earthhack/textures/icon/icon-16x16.png");
                    InputStream inputstream1 = Minecraft.class.getResourceAsStream("/assets/earthhack/textures/icon/icon-32x32.png");

                    if (inputstream != null && inputstream1 != null) {
                        Display.setIcon(new ByteBuffer[]{ImageUtil.readImageToBuffer(inputstream), ImageUtil.readImageToBuffer(inputstream1)});
                    } else
                        Earthhack.getLogger().error("Couldn't set custom icon!!");
                } catch (IOException e) {
                    Earthhack.getLogger().error("Couldn't set custom icon!!", e);
                }
            }
        }
    }
}
