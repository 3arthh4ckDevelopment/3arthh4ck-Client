package me.earth.earthhack.impl.modules.render.hiteffects;

import me.earth.earthhack.api.module.Module;
import me.earth.earthhack.api.module.util.Category;
import me.earth.earthhack.api.setting.Setting;
import me.earth.earthhack.api.setting.settings.BooleanSetting;
import me.earth.earthhack.api.setting.settings.NumberSetting;
import net.minecraft.entity.player.EntityPlayer;

public class HitEffects extends Module {
    public HitEffects(){
        super("HitEffects", Category.Render);
        this.listeners.add(new ListenerDamage(this));
        this.setData(new HitEffectsData(this));
    }

    protected EntityPlayer target;
    protected final Setting<Boolean> lightning =
            register(new BooleanSetting("Lightning", true));
    protected final Setting<Boolean> superheroFx =
            register(new BooleanSetting("SuperheroFX", false));
    protected final Setting<Float> superheroFadeTime =
            register(new NumberSetting<>("FadeTime", 1.5f,0.1f,5.0f));
    protected final Setting<Boolean> onlyOnKill =
            register(new BooleanSetting("OnlyKills", false));
    protected final Setting<Boolean> onlyTargets =
            register(new BooleanSetting("OnlyTargets", false));

}
