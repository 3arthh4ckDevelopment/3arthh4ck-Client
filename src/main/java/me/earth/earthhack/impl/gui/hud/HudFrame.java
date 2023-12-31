package me.earth.earthhack.impl.gui.hud;

import me.earth.earthhack.api.cache.ModuleCache;
import me.earth.earthhack.impl.gui.click.component.Component;
import me.earth.earthhack.impl.gui.click.component.SettingComponent;
import me.earth.earthhack.impl.gui.click.frame.Frame;
import me.earth.earthhack.impl.gui.click.frame.impl.CategoryFrame;
import me.earth.earthhack.impl.gui.visibility.Visibilities;
import me.earth.earthhack.impl.managers.Managers;
import me.earth.earthhack.impl.modules.Caches;
import me.earth.earthhack.impl.modules.client.clickgui.ClickGui;
import me.earth.earthhack.impl.util.render.Render2DUtil;
import me.earth.earthhack.impl.util.render.RenderUtil;
import me.earth.earthhack.pingbypass.input.Mouse;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;

public class HudFrame extends Frame {
    private static final ModuleCache<ClickGui> CLICK_GUI = Caches.getModule(ClickGui.class);

    public HudFrame(String name, float posX, float posY, float width, float height) {
        super(name, posX, posY, width, height);
        this.setExtended(true);
    }

    @Override
    public void moved(float posX, float posY) {
        super.moved(posX, posY);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        final float scrollMaxHeight = new ScaledResolution(
                Minecraft.getMinecraft()).getScaledHeight();

        if (CLICK_GUI.get().catEars.getValue()) {
            CategoryFrame.catEarsRender(getPosX(), getPosY(), getWidth());
        }
        Render2DUtil.drawRect(getPosX(), getPosY(), getPosX() + getWidth(), getPosY() + getHeight(), CLICK_GUI.get().getTopBgColor().getRGB());
        if (CLICK_GUI.get().getBoxes())
            Render2DUtil.drawBorderedRect(getPosX(), getPosY(), getPosX() + getWidth(), getPosY() + getHeight(), 0.5f, 0, CLICK_GUI.get().getTopColor().getRGB());
        Managers.TEXT.drawStringWithShadow(getLabel(), getPosX() + 3, getPosY() + getHeight() / 2 - (Managers.TEXT.getStringHeightI() >> 1), 0xFFFFFFFF);
        if (CLICK_GUI.get().size.getValue()) {
            String disString = "[" + getComponents().size() + "]";
            Managers.TEXT.drawStringWithShadow(disString, (getPosX() + getWidth() - 3 - Managers.TEXT.getStringWidth(disString)), (getPosY() + getHeight() / 2 - (Managers.TEXT.getStringHeightI() >> 1)), 0xFFFFFFFF);
        }
        if (isExtended()) {
            if (RenderUtil.mouseWithinBounds(mouseX, mouseY, getPosX(), getPosY() + getHeight(), getWidth(), (Math.min(getScrollCurrentHeight(), scrollMaxHeight)) + 1) && getScrollCurrentHeight() > scrollMaxHeight) {
                final float scrollSpeed =(CLICK_GUI.get().scrollSpeed.getValue() >> 2);
                int wheel = Mouse.getDWheel();
                if (wheel < 0) {
                    if (getScrollY() - scrollSpeed < -(getScrollCurrentHeight() - Math.min(getScrollCurrentHeight(), scrollMaxHeight)))
                        setScrollY((int) -(getScrollCurrentHeight() - Math.min(getScrollCurrentHeight(), scrollMaxHeight)));
                    else setScrollY((int) (getScrollY() - scrollSpeed));
                } else if (wheel > 0) {
                    setScrollY((int) (getScrollY() + scrollSpeed));
                }
            }
            if (getScrollY() > 0) setScrollY(0);
            if (getScrollCurrentHeight() > scrollMaxHeight) {
                if (getScrollY() - 6 < -(getScrollCurrentHeight() - scrollMaxHeight))
                    setScrollY((int) -(getScrollCurrentHeight() - scrollMaxHeight));
            } else if (getScrollY() < 0) setScrollY(0);
            Render2DUtil.drawRect(getPosX(), getPosY() + getHeight(), getPosX() + getWidth(), getPosY() + getHeight() + 1 + (getCurrentHeight()), 0x92000000);
            GL11.glPushMatrix();
            GL11.glEnable(GL11.GL_SCISSOR_TEST);
            RenderUtil.scissor(getPosX(), getPosY() + getHeight() + 1, getPosX() + getWidth(), getPosY() + getHeight() + scrollMaxHeight + 1);
            getComponents().forEach(component -> component.drawScreen(mouseX, mouseY, partialTicks));
            GL11.glDisable(GL11.GL_SCISSOR_TEST);
            GL11.glPopMatrix();
        }
        updatePositions();
    }

    @Override
    public void keyTyped(char character, int keyCode) {
        super.keyTyped(character, keyCode);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        final float scrollMaxHeight = new ScaledResolution(Minecraft.getMinecraft()).getScaledHeight() - getHeight();
        if (isExtended() && RenderUtil.mouseWithinBounds(mouseX, mouseY, getPosX(), getPosY() + getHeight(), getWidth(), (Math.min(getScrollCurrentHeight(), scrollMaxHeight)) + 1))
            getComponents().forEach(component -> component.mouseClicked(mouseX, mouseY, mouseButton));
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        super.mouseReleased(mouseX, mouseY, mouseButton);
    }

    private void updatePositions() {
        float offsetY = getHeight() + 1;
        for (Component component : getComponents()) {
            component.setOffsetY(offsetY);
            component.moved(getPosX(), getPosY() + getScrollY());
            if (component instanceof HudElementComponent) {
                if (component.isExtended()) {
                    for (Component component1 : ((HudElementComponent) component).getComponents()) {
                        if (component1 instanceof SettingComponent
                                && Visibilities.VISIBILITY_MANAGER.isVisible(((SettingComponent<?, ?>) component1).getSetting())) {
                            offsetY += component1.getHeight();
                        }
                    }
                    offsetY += 3.f;
                }
            }
            offsetY += component.getHeight();
        }
    }

    private float getScrollCurrentHeight() {
        return getCurrentHeight() + getHeight() + 3.f;
    }

    private float getCurrentHeight() {
        float cHeight = 1;
        for (Component component : getComponents()) {
            if (component instanceof HudElementComponent) {
                if (component.isExtended()) {
                    for (Component component1 : ((HudElementComponent) component).getComponents()) {
                        if (component1 instanceof SettingComponent
                                && Visibilities.VISIBILITY_MANAGER.isVisible(((SettingComponent<?, ?>) component1).getSetting())) {
                            cHeight += component1.getHeight();
                        }
                    }
                    cHeight += 3.f;
                }
            }
            cHeight += component.getHeight();
        }
        return cHeight;
    }
}
