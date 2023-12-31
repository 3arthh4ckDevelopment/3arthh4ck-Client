package me.earth.earthhack.impl.modules.client.capes;

import me.earth.earthhack.api.module.Module;
import me.earth.earthhack.api.module.util.Category;
import me.earth.earthhack.api.setting.Setting;
import me.earth.earthhack.api.setting.settings.EnumSetting;
import me.earth.earthhack.impl.util.client.SimpleData;
import net.minecraft.util.ResourceLocation;

public class Capes extends Module {

    private final Setting<CapeType> enumCape =
            register(new EnumSetting<>("Cape", CapeType.Phobos));
    public Capes() {
        super("Capes", Category.Client);
        this.setData(new SimpleData(this, "Renders capes."));
    }

    public ResourceLocation getCapeResource() {
        return enumCape.getValue().getCape();
    }

    private enum CapeType {
        Phobos(new ResourceLocation("earthhack:textures/capes/phobos.png")),
        RusherHack(new ResourceLocation("earthhack:textures/capes/rusherhack.png")),
        Future(new ResourceLocation("earthhack:textures/capes/future.png")),
        Cobalt(new ResourceLocation("earthhack:textures/capes/cobalt.png")),
        Founder(new ResourceLocation("earthhack:textures/capes/founder.png")),
        MineCon2011(new ResourceLocation("earthhack:textures/capes/minecon2011.png")),
        MineCon2012(new ResourceLocation("earthhack:textures/capes/minecon2012.png")),
        MineCon2013(new ResourceLocation("earthhack:textures/capes/minecon2013.png")),
        MineCon2015(new ResourceLocation("earthhack:textures/capes/minecon2015.png")),
        MineCon2016(new ResourceLocation("earthhack:textures/capes/minecon2016.png")),
        Mojang(new ResourceLocation("earthhack:textures/capes/mojang.png")),
        MojangClassic(new ResourceLocation("earthhack:textures/capes/mojang_classic.png")),
        MojangStudios(new ResourceLocation("earthhack:textures/capes/mojang_studios.png")),
        Snowman(new ResourceLocation("earthhack:textures/capes/snowman.png"));

        private final ResourceLocation location;

        CapeType(ResourceLocation loc){
            this.location = loc;
        }

        private ResourceLocation getCape(){
            return location;
        }
    }

}
