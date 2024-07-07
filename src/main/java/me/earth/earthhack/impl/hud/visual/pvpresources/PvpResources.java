package me.earth.earthhack.impl.hud.visual.pvpresources;

import me.earth.earthhack.api.hud.HudCategory;
import me.earth.earthhack.api.hud.HudElement;
import me.earth.earthhack.api.setting.Setting;
import me.earth.earthhack.api.setting.settings.BooleanSetting;
import me.earth.earthhack.api.setting.settings.ColorSetting;
import me.earth.earthhack.api.setting.settings.EnumSetting;
import me.earth.earthhack.api.setting.settings.StringSetting;
import me.earth.earthhack.impl.util.helpers.addable.ItemAddingModule;
import me.earth.earthhack.impl.util.render.Render2DUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.awt.*;
import java.util.ArrayList;

public class PvpResources extends HudElement {

    private final Setting<Mode> mode =
            register(new EnumSetting<>("Mode", Mode.Simple));
    private final Setting<Styles> style =
            register(new EnumSetting<>("Style", Styles.Vertical));
    private final Setting<Boolean> pretty =
            register(new BooleanSetting("Pretty", true));
    private final Setting<Color> color =
            register(new ColorSetting("Color", new Color(0, 0, 0, 0)));
    private final Setting<Boolean> obby =
            register(new BooleanSetting("Obsidian", false));
    private final Setting<String> blocks =
            register(new StringSetting("Add/Remove", "Add/Remove"));

    private final ArrayList<Integer> itemIds = new ArrayList<>();
    private final int[] defaultIds = { 426, 384, 322, 449 };
    private float x = 0.0f, y = 0.0f;
    private int finalOffset;
    
    protected void onRender() {
        if (mode.getValue() == Mode.Simple) {
            for (int i : defaultIds)
                if (!itemIds.contains(i))
                    itemIds.add(i);
        }
        if (obby.getValue()) {
            if (!itemIds.contains(49))
                itemIds.add(49);
        } else if (itemIds.contains(49))
            itemIds.remove((Object) 49);

        x = getX();
        y = getY();
        if (style.getValue() == Styles.Square)
            drawSquare();
        else if (style.getValue() == Styles.Vertical)
            drawVertical();
        else
            drawHorizontal();
    }

    private void drawVertical() {
        finalOffset = itemIds.size() * 20;
        GlStateManager.pushMatrix();
        if (pretty.getValue())
            Render2DUtil.roundedRect(x, y - 1, x + 16, y + finalOffset - 3, 2.0f, color.getValue().getRGB());
        else
            Render2DUtil.drawRect(x - 3, y - 3, x + 20, y + finalOffset, color.getValue().getRGB());
        GlStateManager.popMatrix();

        int offset = 0;
        for (int i : itemIds) {
            renderItem(i, x, y + offset);
            offset += 20;
        }
    }

    private void drawSquare() {
        GlStateManager.pushMatrix();
        if (pretty.getValue())
            Render2DUtil.roundedRect(x + 1, y + 1, x + 36, y + 36, 4.0f, color.getValue().getRGB());
        else
            Render2DUtil.drawRect(x - 3, y - 3, x + 40, y + 40, color.getValue().getRGB());
        GlStateManager.popMatrix();

        renderItem(426, x, y);
        renderItem(384, x, y + 20);
        renderItem(322, x + 20, y);
        renderItem(449, x + 20, y + 20);
    }

    private void drawHorizontal() {
        GlStateManager.pushMatrix();
        finalOffset = itemIds.size() * 20;
        if (pretty.getValue())
            Render2DUtil.roundedRect(x - 1, y + 1, x + finalOffset - 3, y + 16, 2.0f, color.getValue().getRGB());
        else
            Render2DUtil.drawRect(x - 2, y - 2, x + finalOffset - 1, y + 18, color.getValue().getRGB());
        GlStateManager.popMatrix();

        int offset = 0;
        for (int i : itemIds) {
            renderItem(i, x + offset, y);
            offset += 20;
        }
    }

    private void renderItem(int itemId, float x, float y) {
        Item item = Item.getItemById(itemId);
        int itemCount = mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == item).mapToInt(ItemStack::getCount).sum();
        if (mc.player.getHeldItemOffhand().getItem().equals(item))
            itemCount += mc.player.getHeldItemOffhand().getCount();
        Render2DUtil.drawItem(new ItemStack(item, itemCount), (int) x, (int) y, true, true);
    }

    public PvpResources() {
        super("Items", "Displays some items from your Inventory.", HudCategory.Visual, 60, 70);

        this.blocks.addObserver(event -> {
            if (!event.isCancelled()) {
                Item item = ItemAddingModule.getItemStartingWith(event.getValue(), i -> true);
                if (item != null) {
                    int itemId = Item.getIdFromItem(item);
                    if (!itemIds.contains(itemId))
                        itemIds.add(itemId);
                    else
                        itemIds.remove((Object)itemId);
                }
            }
        });

        this.mode.addObserver(event -> itemIds.clear());
    }

    @Override
    public float getWidth() {
        if (mode.getValue() == Mode.List && itemIds.isEmpty())
            return 17;
        else
            return style.getValue() == Styles.Horizontal
                    ? finalOffset
                    : style.getValue() == Styles.Square
                        ? 37
                        : 17;
    }

    @Override
    public float getHeight() {
        if (mode.getValue() == Mode.List && itemIds.isEmpty())
            return 17;
        else
            return style.getValue() == Styles.Vertical
                    ? finalOffset
                    : style.getValue() == Styles.Square
                        ? 37
                        : 20;
    }

    private enum Styles {
        Vertical,
        Horizontal,
        Square
    }

    private enum Mode {
        Simple,
        List
    }
}
