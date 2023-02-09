package me.earth.earthhack.impl.modules.misc.autokys;

import me.earth.earthhack.api.module.Module;
import me.earth.earthhack.api.module.util.Category;
import me.earth.earthhack.api.setting.Setting;
import me.earth.earthhack.api.setting.settings.StringSetting;
import net.minecraft.client.Minecraft;

public class AutoKys extends Module {

    protected final Setting<String> kysCommand =
            register(new StringSetting("Command", "/kill"));
    public AutoKys(){
        super("AutoKys", Category.Misc);
    }

    private final Minecraft mc = Minecraft.getMinecraft();
    public void onEnable(){
        mc.player.sendChatMessage(kysCommand.getValue());
        this.disable();
    }

    public void onDisable(){
        this.disable();
    }
}
