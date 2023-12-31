package me.earth.earthhack.impl.hud.visual.textradar;

import me.earth.earthhack.impl.util.text.TextColor;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PotionManager {

    public static final PotionManager INSTANCE = new PotionManager();
    private final Map<EntityPlayer, PotionList> potions = new ConcurrentHashMap<>();

    public List<PotionEffect> getPlayerPotions(EntityPlayer player) {
        PotionList list = potions.get(player);
        List<PotionEffect> potions = new ArrayList<>();
        if (list != null) {
            potions = list.getEffects();
        }
        return potions;
    }
    public class PotionList {
        private List<PotionEffect> effects = new ArrayList<>();

        public void addEffect(PotionEffect effect) {
            if(effect != null) {
                this.effects.add(effect);
            }
        }

        public List<PotionEffect> getEffects() {
            return this.effects;
        }
    }


    public PotionEffect[] getImportantPotions(EntityPlayer player) {
        PotionEffect[] array = new PotionEffect[3];
        for(PotionEffect effect : getPlayerPotions(player)) {
            Potion potion = effect.getPotion();
            switch((I18n.format(potion.getName())).toLowerCase()) {
                case "strength" :
                    array[0] = effect;
                    break;
                case "weakness" :
                    array[1] = effect;
                    break;
                case "speed" :
                    array[2] = effect;
                    break;
                default:
            }
        }
        return array;
    }

    public String getTextRadarPotion(EntityPlayer player) {
        PotionEffect[] array = getImportantPotions(player);
        PotionEffect strength = array[0];
        PotionEffect weakness = array[1];
        PotionEffect speed = array[2];
        return "" + (strength != null ? TextColor.RED + " S" + (int)(strength.getAmplifier() + 1) + " " : "")
                + (weakness != null ? TextColor.DARK_GRAY + " W " : "")
                + (speed != null ? TextColor.AQUA + " S" + (int)(speed.getAmplifier() + 1) + " " : "");
    }

}
