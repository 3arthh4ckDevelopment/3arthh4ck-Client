package me.earth.earthhack.impl.modules.misc.autokys;

import me.earth.earthhack.api.module.Module;
import me.earth.earthhack.api.module.util.Category;
import me.earth.earthhack.api.setting.Setting;
import me.earth.earthhack.api.setting.settings.NumberSetting;
import me.earth.earthhack.api.setting.settings.StringSetting;
import net.minecraft.client.Minecraft;

public class AutoKys extends Module {

    protected final Setting<Integer> delay     =
            register(new NumberSetting<>("Delay", 0, 0, 500));
    protected final Setting<String> kysCommand =
            register(new StringSetting("Command", "/kill"));
    public AutoKys(){
        super("AutoKys", Category.Misc);
        this.setData(new AutoKysData(this));
    }

    private final Minecraft mc = Minecraft.getMinecraft();
    public void onEnable(){
        try{
            Thread.sleep(delay.getValue());
            mc.player.sendChatMessage(kysCommand.getValue());
            this.disable();
        }catch(InterruptedException e){
            this.disable();
            // this should probably have better stuff? idk about it though, since this shouldn't happen :P
        }
    }

    public void onDisable(){
        this.disable();
    }
}
