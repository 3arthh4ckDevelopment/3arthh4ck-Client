package me.earth.earthhack.impl.gui.click.frame.impl;

import me.earth.earthhack.api.cache.ModuleCache;
import me.earth.earthhack.impl.gui.click.Click;
import me.earth.earthhack.impl.gui.click.frame.Frame;
import me.earth.earthhack.impl.managers.Managers;
import me.earth.earthhack.impl.modules.Caches;
import me.earth.earthhack.impl.modules.client.colors.Colors;
import me.earth.earthhack.impl.util.render.Render2DUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.util.List;

public class DescriptionFrame extends Frame
{
    public static final ModuleCache<Colors> COLOR_MODULE = Caches.getModule(Colors.class);
    private static final ResourceLocation LEFT_EAR = new ResourceLocation("earthhack:textures/gui/left_ear.png");
    private static final ResourceLocation RIGHT_EAR = new ResourceLocation("earthhack:textures/gui/right_ear.png");


    private String description;

    public DescriptionFrame(float posX, float posY, float width, float height)
    {
        this("Description", posX, posY, width, height);
    }

    public DescriptionFrame(String label, float posX, float posY, float width, float height)
    {
        super(label, posX, posY, width, height);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        if (description == null || !Click.CLICK_GUI.get().description.getValue())
        {
            return;
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
        final Color clr = Click.COLOR_MODULE.get().getCatEars();
        if (Click.CLICK_GUI.get().catEars.getValue())
        {
            Minecraft.getMinecraft().getTextureManager().bindTexture(LEFT_EAR);
            GlStateManager.color(clr.getRed() / 255.f, clr.getGreen() / 255.f, clr.getBlue() / 255.f, 1.0F);
            Gui.drawScaledCustomSizeModalRect((int) getPosX() - 7, (int) getPosY() - 8, 0, 0, 20, 20, 20, 20, 20, 20);
            Minecraft.getMinecraft().getTextureManager().bindTexture(RIGHT_EAR);
            GlStateManager.color(clr.getRed() / 255.f, clr.getGreen() / 255.f, clr.getBlue() / 255.f, 1.0F);
            Gui.drawScaledCustomSizeModalRect((int) (getPosX() + getWidth()) - 14, (int) getPosY() - 8, 0, 0, 20, 20, 20, 20, 20, 20);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        }
        Render2DUtil.drawRect(getPosX(), getPosY(), getPosX() + getWidth(), getPosY() + getHeight(), Click.COLOR_MODULE.get().getTopBgColor().getRGB());
        Render2DUtil.drawBorderedRect(getPosX(), getPosY(), getPosX() + getWidth(), getPosY() + getHeight(), 0.5f, 0, Click.COLOR_MODULE.get().getTopColor().getRGB());
        Managers.TEXT.drawStringWithShadow(getLabel(), getPosX() + 3, getPosY() + getHeight() / 2 - (Managers.TEXT.getStringHeightI() >> 1), 0xFFFFFFFF);


        float y = this.getPosY() + 2 + (getHeight() / 2) + Managers.TEXT.getStringHeightI();
        List<String> strings = Managers.TEXT.listFormattedStringToWidth(this.getDescription(), (int) this.getWidth() - 1);

        Render2DUtil.drawRect(getPosX(), getPosY() + getHeight(), getPosX() + getWidth(), getPosY() + getHeight() + 3 + (Managers.TEXT.getStringHeightI() + 1) * strings.size(), 0x92000000);

        for (String string : strings)
        {
            Managers.TEXT.drawStringWithShadow(string, this.getPosX() + 3, y, COLOR_MODULE.get().getTextColorDesc().getRGB());
            y += Managers.TEXT.getStringHeightI() + 1;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        super.mouseReleased(mouseX, mouseY, mouseButton);
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

}
