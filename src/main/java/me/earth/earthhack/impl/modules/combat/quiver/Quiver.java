package me.earth.earthhack.impl.modules.combat.quiver;

import me.earth.earthhack.api.module.Module;
import me.earth.earthhack.api.module.util.Category;
import me.earth.earthhack.api.setting.Setting;
import me.earth.earthhack.api.setting.settings.BooleanSetting;
import me.earth.earthhack.api.setting.settings.EnumSetting;
import me.earth.earthhack.api.setting.settings.NumberSetting;
import me.earth.earthhack.impl.modules.combat.quiver.modes.*;
import me.earth.earthhack.impl.util.minecraft.InventoryUtil;
import net.minecraft.init.Items;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.Potion;

public class Quiver extends Module {
    protected final Setting<HUDMode> hudMode     =
            register(new EnumSetting<>("HUDMode", HUDMode.Arrows));
    protected final Setting<SwitchMode> switchMode     =
            register(new EnumSetting<>("HUDMode", SwitchMode.Normal));
    protected final Setting<QuiverMode> quiverMode     =
            register(new EnumSetting<>("HUDMode", QuiverMode.Automatic));
    protected final Setting<Integer> delay   =
            register(new NumberSetting<>("Delay", 6, 0, 20));
    protected final Setting<Boolean> tpsSync =
            register(new BooleanSetting("TPS-Sync", false));
    protected final Setting<RotationMode> rotateMode     =
            register(new EnumSetting<>("HUDMode", RotationMode.Normal));
    protected final Setting<Boolean> spam =
            register(new BooleanSetting("Spam", true));

    public Quiver() {
        super("Quiver", Category.Combat);
        this.setData(new QuiverData(this));
        this.listeners.add(new ListenerMotion(this));
    }

    // stuff
    boolean inInv = false;

    PotionEffect speedEffect = mc.player.getActivePotionEffect(Potion.getPotionById(1));
    PotionEffect strengthEffect = mc.player.getActivePotionEffect(Potion.getPotionById(5)); // WIP

    int arrowHits = 0;
    int arrowCount;

    public void doQuiver()
    {
        switch(rotateMode.getName()){
            case "Normal":
                if(quiverMode.getName().equals("Automatic")){

                }
            break;
            case "Packet": // don't cry if this code is massive spaghetti :sob:
                if(quiverMode.getName().equals("Automatic")){

                }
            break;
        }
    }

    public int getArrowCount(){
        arrowCount = InventoryUtil.getCount(Items.TIPPED_ARROW); // We could check only for arrows with either Speed or Strength effects.
        return arrowCount;
    }
    @Override
    public String getDisplayInfo()
    {
        if(hudMode.getName().equals("Arrows"))
        {
            return Integer.toString(getArrowCount());
        }
        else if(hudMode.getName().equals("Hits"))
        {
            return Integer.toString(arrowHits);
        }
        return null;
    }
}
