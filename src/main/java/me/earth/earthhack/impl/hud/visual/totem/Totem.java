package me.earth.earthhack.impl.hud.visual.totem;

import me.earth.earthhack.api.hud.HudCategory;
import me.earth.earthhack.api.hud.HudElement;
import me.earth.earthhack.impl.util.minecraft.InventoryUtil;
import me.earth.earthhack.impl.util.render.Render2DUtil;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class Totem extends HudElement {

    public Totem() {
        super("Totem", "Displays your totems.", HudCategory.Visual, 120, 120);
    }

    @Override
    protected void onRender() {
        Render2DUtil.drawItem(new ItemStack(Items.TOTEM_OF_UNDYING, InventoryUtil.getCount(Items.TOTEM_OF_UNDYING)), (int) getX(), (int) getY(), true, true);
    }

    @Override
    public float getWidth() {
        return 18.0f;
    }

    @Override
    public float getHeight() {
        return 18.0f;
    }
}
