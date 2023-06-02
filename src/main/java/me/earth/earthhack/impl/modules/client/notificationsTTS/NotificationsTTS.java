package me.earth.earthhack.impl.modules.client.notificationsTTS;

import com.mojang.text2speech.Narrator;
import me.earth.earthhack.api.module.Module;
import me.earth.earthhack.api.module.util.Category;
import me.earth.earthhack.api.setting.Setting;
import me.earth.earthhack.api.setting.settings.BooleanSetting;
import me.earth.earthhack.api.setting.settings.NumberSetting;
import me.earth.earthhack.api.setting.settings.StringSetting;
import me.earth.earthhack.impl.event.events.misc.TickEvent;
import me.earth.earthhack.impl.event.listeners.LambdaListener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.Objects;

import static me.earth.earthhack.impl.util.otherplayers.IgnoreSelfClosest.GetClosestIgnore;
import static me.earth.earthhack.impl.util.text.NumbersToWords.numberWords;

public class NotificationsTTS extends Module
{
    protected final Setting<Boolean> target =
            register(new BooleanSetting("TargetPops", false));
    protected final Setting<Boolean> pops   =
            register(new BooleanSetting("OwnPops", false));
    protected final Setting<Boolean> helmet =
            register(new BooleanSetting("Helmet", true));
    protected final Setting<Boolean> chest =
            register(new BooleanSetting("ChestPlate", true));
    protected final Setting<Boolean> legs =
            register(new BooleanSetting("Leggings", true));
    protected final Setting<Boolean> boots =
            register(new BooleanSetting("Boots", true));
    protected final Setting<Integer> percentage =
            register(new NumberSetting<>("percentage", 5, 0,30));
    protected final Setting<Double> targetDistance =
            register(new NumberSetting<>("TargetDistance", 10.0, 0.0,30.0));
    protected final Setting<String> name =
            register(new StringSetting("PlayerName", "You"));

    private final Narrator narrator = Narrator.getNarrator();

    public NotificationsTTS()
    {
        super("NotificationsTTS", Category.Client);
        this.listeners.add(new ListenerTotems(this));
        this.listeners.add(new ListenerDeath(this));
        this.setData(new NotificationTTSData(this));

        String[][] armorPieces = {{"boots", "0"},{"leggings", "0"},{"chestplate", "0"},{"helmet", "0"}};

        this.listeners.add(new LambdaListener<>(TickEvent.class, e -> {
            if (mc.player == null || mc.world == null) {return;}
            for (int i = 3; i >= 0; i--) {
                ItemStack stack = mc.player.inventory.armorInventory.get(i);
                if (!stack.isEmpty()
                        && (stack.getMaxDamage() - stack.getItemDamage()) * 100 / stack.getMaxDamage() <= percentage.getValue()
                        && !Objects.equals(armorPieces[i][1], "1")) {
                    narrator.clear();
                    narrator.say(armorPieces[i][0] + " has " + numberWords.get((stack.getMaxDamage() - stack.getItemDamage())) + " durability left!");
                    armorPieces[i][1] = "1";
                } else if (stack.isEmpty() && Objects.equals(armorPieces[i][1], "1")) {
                    armorPieces[i][1] = "0"; // basically i check if the armor got replaced
                }
            }
        }));
    }

    @Override
    protected void onDisable() {}

    public void onPop(Entity player, int totemPops) {
        if (this.isEnabled()
                && pops.getValue()
                && (player.getName().equals(mc.player.getName()) || player.getName().equals(closestPlayerName()))) {
            String message = (player.getName() == mc.player.getName() ? name.getValue() : player.getName())
                    + " popped "
                    + numberWords.get(totemPops)
                    + " totem"
                    + (totemPops == 1 ? "." : "s.");

            narrator.clear();
            narrator.say(message);
        }
    }

    public void onDeath(Entity player, int totemPops)
    {
        if (this.isEnabled() && pops.getValue()
                && totemPops < 30
                && (player.getName().equals(closestPlayerName()) || player.getName().equals(mc.player.getName()))) // self or the enemy
        {
            String message =  (player.getName() == mc.player.getName() ? name.getValue() : player.getName())
                    + " died after popping "
                    + totemPops
                    + " totem"
                    + (totemPops == 1 ? "." : "s.");

            narrator.clear();
            narrator.say(message);
        }
    }

    public String closestPlayerName() {
        EntityPlayer closestPlayer;
        if (mc.player != null && mc.world != null) {
            closestPlayer = GetClosestIgnore(targetDistance.getValue());
            if (closestPlayer != null) {return closestPlayer.getName();}
        }
        return "null";
    }
}
