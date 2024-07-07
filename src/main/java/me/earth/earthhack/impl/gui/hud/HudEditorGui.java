package me.earth.earthhack.impl.gui.hud;

import me.earth.earthhack.api.cache.ModuleCache;
import me.earth.earthhack.api.hud.HudCategory;
import me.earth.earthhack.api.hud.HudElement;
import me.earth.earthhack.impl.gui.click.component.Component;
import me.earth.earthhack.impl.gui.click.component.impl.ColorComponent;
import me.earth.earthhack.impl.gui.click.component.impl.KeybindComponent;
import me.earth.earthhack.impl.gui.click.component.impl.ModuleComponent;
import me.earth.earthhack.impl.gui.click.component.impl.StringComponent;
import me.earth.earthhack.impl.gui.click.frame.Frame;
import me.earth.earthhack.impl.managers.Managers;
import me.earth.earthhack.impl.modules.Caches;
import me.earth.earthhack.impl.modules.client.clickgui.ClickGui;
import me.earth.earthhack.impl.modules.client.editor.HudEditor;
import me.earth.earthhack.impl.util.misc.GuiUtil;
import me.earth.earthhack.impl.util.render.Render2DUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;

import javax.annotation.ParametersAreNonnullByDefault;
import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.*;

public class HudEditorGui extends GuiScreen {
    private static final ModuleCache<ClickGui> CLICK_GUI = Caches.getModule(ClickGui.class);

    public static Map<String, List<SnapPoint>> snapPoints;
    private final ArrayList<HudCategoryFrame> frames = new ArrayList<>();
    private double mouseClickedX, mouseClickedY, mouseReleasedX, mouseReleasedY;
    private boolean oldVal = false;
    private boolean selecting;

    public void init() {
        getFrames().clear();
        int x = 100;
        for (HudCategory hudCategory : HudCategory.values()) {
            getFrames().add(new HudCategoryFrame(hudCategory, Managers.ELEMENTS, x, 14, 110, 16));
            x += 130;
        }
        getFrames().forEach(Frame::init);

        oldVal = CLICK_GUI.get().catEars.getValue();
        snapPoints = new HashMap<>();
        List<SnapPoint> points = new ArrayList<>();
        ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());
        points.add(new SnapPoint(2,resolution.getScaledHeight() - 4, 2, true, SnapPoint.Orientation.LEFT));
        points.add(new SnapPoint(2, resolution.getScaledHeight() - 4, resolution.getScaledWidth() - 2, true, SnapPoint.Orientation.RIGHT));
        points.add(new SnapPoint(2, resolution.getScaledWidth() - 4, 2, true, SnapPoint.Orientation.TOP));
        points.add(new SnapPoint(2, resolution.getScaledWidth() - 4, resolution.getScaledHeight() - 2, true, SnapPoint.Orientation.BOTTOM));
        points.add(new SnapPoint(2, resolution.getScaledHeight() - 4, resolution.getScaledWidth() / 2.0f, true, SnapPoint.Orientation.VERTICAL_CENTER));
        points.add(new SnapPoint(2, resolution.getScaledWidth() - 4, resolution.getScaledHeight() / 2.0f, true, SnapPoint.Orientation.HORIZONTAL_CENTER));
        snapPoints.put("default", points);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void onResize(Minecraft mcIn, int w, int h) {
        super.onResize(mcIn, w, h);
        init();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        if (mc.world == null) {
            ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
            String text = "You need to be in a world to use the HUD editor!";
            Managers.TEXT.drawString(text, (float) scaledResolution.getScaledWidth() / 2 - Managers.TEXT.getStringWidth(text), (float) (scaledResolution.getScaledHeight() - Managers.TEXT.getStringHeightI()) / 2, 0xFFFFFF);
        }

        if (oldVal != CLICK_GUI.get().catEars.getValue()) {
            init();
            oldVal = CLICK_GUI.get().catEars.getValue();
        }

        if (CLICK_GUI.get().blur.getValue() == ClickGui.BlurStyle.Directional) {
            ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
            Render2DUtil.drawBlurryRect(0, 0, scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight(), CLICK_GUI.get().blurAmount.getValue(),CLICK_GUI.get().blurSize.getValue());
        }

        for (List<SnapPoint> points : snapPoints.values()) {
            for (SnapPoint point : points) {
                if (point.isVisible()) {
                    point.draw(mouseX, mouseY, partialTicks);
                }
                point.update(Managers.ELEMENTS.getRegistered());
            }
        }

        for (HudElement element : Managers.ELEMENTS.getRegistered()) {
            if (element.isEnabled()) {
                element.guiUpdate(mouseX, mouseY, partialTicks);
                element.guiDraw();
            }
        }

        if (selecting) {
            double minX = Math.min(mouseX, mouseClickedX);
            double minY = Math.min(mouseY, mouseClickedY);
            double maxX = Math.max(mouseX, mouseClickedX);
            double maxY = Math.max(mouseY, mouseClickedY);
            Render2DUtil.drawBorderedRect((float) minX, (float) minY, (float) maxX, (float) maxY, 0.2f, new Color(255, 255, 255, 90).getRGB(), new Color(255, 255, 255, 160).getRGB());
        }
        getFrames().forEach(frame -> frame.drawScreen(mouseX, mouseY, partialTicks));
    }

    @Override
    protected void keyTyped(char character, int keyCode) throws IOException {
        super.keyTyped(character, keyCode);
        getFrames().forEach(frame -> frame.keyTyped(character, keyCode));
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        List<HudElement> clicked = new ArrayList<>();
        boolean isDragging = false;
        for (HudElement element : Managers.ELEMENTS.getRegistered()) {
            if (element.isEnabled() && GuiUtil.isHovered(element, mouseX, mouseY)) {
                clicked.add(element);
                if (element.isDragging())
                    isDragging = true;
            }
        }
        clicked.sort(Comparator.comparing(HudElement::getZ));

        boolean clickedFrame = false;
        for (HudCategoryFrame frame : getFrames()) {
            if (GuiUtil.isHovered(frame, mouseX, mouseY)) {
                clickedFrame = true;
                break;
            }
            for (Component component : frame.getComponents()) {
                if (GuiUtil.isHovered(component.getFinishedX(), component.getFinishedY(), component.getWidth(), component.getHeight(), mouseX, mouseY)) {
                    clickedFrame = true;
                    break;
                }
            }
        }

        if (!clickedFrame) {
            if (!clicked.isEmpty()) {
                clicked.get(0).guiMouseClicked(mouseX, mouseY, mouseButton);
            } else if (!isDragging) {
                selecting = true;
                mouseClickedX = mouseX;
                mouseClickedY = mouseY;
            }
        }
        getFrames().forEach(frame -> frame.mouseClicked(mouseX, mouseY, mouseButton));
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        super.mouseReleased(mouseX, mouseY, mouseButton);

        if (selecting) {
            mouseReleasedX = mouseX;
            mouseReleasedY = mouseY;
        }

        for (HudElement element : Managers.ELEMENTS.getRegistered()) {
            if (element.isEnabled()) {
                element.guiMouseReleased(mouseX, mouseY, mouseButton);
                double minX = Math.min(mouseClickedX, mouseReleasedX);
                double minY = Math.min(mouseClickedY, mouseReleasedY);
                double maxWidth = Math.max(mouseClickedX, mouseReleasedX) - minX;
                double maxHeight = Math.max(mouseClickedY, mouseReleasedY) - minY;
                if (selecting && GuiUtil.isOverlapping(
                        new double[]{minX, minY, minX + maxWidth, minY + maxHeight},
                        new double[]{element.getX(), element.getY(), element.getX() + element.getWidth(), element.getY() + element.getHeight()})) {
                    element.setDraggingX(mouseX - element.getX());
                    element.setDraggingY(mouseY - element.getY());
                    element.setDragging(true);
                }
            }
        }
        selecting = false;
        getFrames().forEach(frame -> frame.mouseReleased(mouseX, mouseY, mouseButton));
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        getFrames().forEach(frame -> {
            for (Component comp : frame.getComponents()) {
                if (comp instanceof ModuleComponent) {
                    final ModuleComponent moduleComponent = (ModuleComponent) comp;
                    for (Component component : moduleComponent.getComponents()) {
                        if (component instanceof KeybindComponent) {
                            final KeybindComponent keybindComponent = (KeybindComponent) component;
                            keybindComponent.setBinding(false);
                        }
                        if (component instanceof StringComponent) {
                            final StringComponent stringComponent = (StringComponent) component;
                            stringComponent.setListening(false);
                        }
                    }
                }
            }
        });
        for (HudElement element : Managers.ELEMENTS.getRegistered()) {
            element.setDragging(false);
        }
        selecting = false;
        Caches.getModule(HudEditor.class).disable();

        if (OpenGlHelper.shadersSupported)
            mc.entityRenderer.stopUseShader();
    }

    public void onGuiOpened() {
        getFrames().forEach(frame -> {
            for (Component comp : frame.getComponents()) {
                if (comp instanceof ModuleComponent) {
                    final ModuleComponent moduleComponent = (ModuleComponent) comp;
                    for (Component component : moduleComponent.getComponents()) {
                        if (component instanceof ColorComponent) {
                            final ColorComponent colorComponent = (ColorComponent) component;
                            float[] hsb = Color.RGBtoHSB(colorComponent.getColorSetting().getRed(), colorComponent.getColorSetting().getGreen(), colorComponent.getColorSetting().getBlue(), null);
                            colorComponent.setHue(hsb[0]);
                            colorComponent.setSaturation(hsb[1]);
                            colorComponent.setBrightness(hsb[2]);
                            colorComponent.setAlpha(colorComponent.getColorSetting().getAlpha() / 255.f);
                        }
                    }
                }
            }
        });
    }

    private ArrayList<HudCategoryFrame> getFrames() {
        return frames;
    }

}
