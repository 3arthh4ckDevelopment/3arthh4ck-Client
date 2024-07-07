package me.earth.earthhack.api.hud;

import me.earth.earthhack.api.event.bus.api.Listener;
import me.earth.earthhack.api.event.bus.api.Subscriber;
import me.earth.earthhack.api.event.bus.instance.Bus;
import me.earth.earthhack.api.setting.Complexity;
import me.earth.earthhack.api.setting.Setting;
import me.earth.earthhack.api.setting.SettingContainer;
import me.earth.earthhack.api.setting.event.SettingEvent;
import me.earth.earthhack.api.setting.settings.BooleanSetting;
import me.earth.earthhack.api.setting.settings.NumberSetting;
import me.earth.earthhack.api.util.interfaces.Globals;
import me.earth.earthhack.api.util.interfaces.Nameable;
import me.earth.earthhack.impl.util.misc.GuiUtil;
import me.earth.earthhack.impl.util.render.Render2DUtil;
import me.earth.earthhack.impl.util.render.hud.HudRenderUtil;
import me.earth.earthhack.impl.util.text.TextColor;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class HudElement extends SettingContainer
        implements Globals, Subscriber, Nameable {

    private final Setting<Boolean> enabled =
            register(new BooleanSetting("Enabled", false));
    private final Setting<Float> x =
            register(new NumberSetting<>("X", 2.0f, -20.0f, 2000.0f))
                    .setComplexity(Complexity.Dev);
    private final Setting<Float> y =
            register(new NumberSetting<>("Y", 2.0f, -20.0f, 2000.0f))
                    .setComplexity(Complexity.Dev);
    private final Setting<Integer> z =
            register(new NumberSetting<>("Z", 0, -2000, 2000))
                    .setComplexity(Complexity.Dev); // Z level determines rendering order.
    private final Setting<Float> scale =
            register(new NumberSetting<>("Scale", 1.0f, 0.1f, 3.0f))
                    .setComplexity(Complexity.Dev);

    protected final List<Listener<?>> listeners = new ArrayList<>();
    private final AtomicBoolean enableCheck = new AtomicBoolean();
    private final AtomicBoolean inOnEnable  = new AtomicBoolean();
    private boolean isScreenGui = false;
    private final HudCategory category;
    private final String name, description;
    private float width = 100;
    private float height = 100;

    private float draggingX, draggingY;
    private boolean dragging;

    /**
     * Creates a new HudElement. It's important that the given name
     * does not contain any whitespaces and that no hud elements with the
     * same name exist. A hud element's name is its unique identifier.
     *
     * @param name name of the hud element
     * @param x x of the element
     * @param y y of the element
     */
    public HudElement(String name, String description, HudCategory category, float x, float y) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.enabled.addObserver(this::onEnabledEvent);
        this.x.setValue(x);
        this.y.setValue(y);
    }

    protected void onEnabledEvent(SettingEvent<Boolean> event) {
        if (event.isCancelled())
            return;

        enableCheck.set(event.getValue());
        if (event.getValue() && !Bus.EVENT_BUS.isSubscribed(this)) {
            inOnEnable.set(true);
            onEnable();
            inOnEnable.set(false);
            if (enableCheck.get()) {
                Bus.EVENT_BUS.subscribe(this);
            }
        }
        else if (!event.getValue()
                && (Bus.EVENT_BUS.isSubscribed(this) || inOnEnable.get()))
        {
            Bus.EVENT_BUS.unsubscribe(this);
            onDisable();
        }
    }

    public final boolean isEnabled() {
        return enabled.getValue();
    }

    public final void toggle() {
        if (isEnabled())
            this.disable();
        else
            this.enable();
    }

    public final void enable() {
        if (!isEnabled())
            enabled.setValue(true);
    }

    public final void disable() {
        if (isEnabled())
            enabled.setValue(false);
    }

    protected void onEnable()
    {
        /* Implemented by the module */
    }

    protected void onDisable()
    {
        /* Implemented by the module */
    }

    protected void onLoad()
    {
        /* Implemented by the module */
    }

    /* Implemented by the module */
    protected abstract void onRender();

    /**
     * A method to check if the screen is part of the gui
     * @return true if the screen is part of gui, false if it's world
     */
    public boolean isGui() {
        return isScreenGui;
    }

    public HudCategory getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public final void load() {
        if (this.isEnabled() && !Bus.EVENT_BUS.isSubscribed(this)) {
            Bus.EVENT_BUS.subscribe(this);
        }
        onLoad();
    }

    /**
     * Called when the screen is the gui
     */
    public void guiDraw() {
        if (mc.world != null && mc.player != null) {
            isScreenGui = true;
            setWidth(getWidth());
            setHeight(getHeight());

            GlStateManager.pushMatrix();
            GlStateManager.scale(getScale(), getScale(), getScale());
            if (width != 0 && height != 0) {
                Render2DUtil.drawBorderedRect(getX(), getY(), getX() + getWidth(), getY() + getHeight(), 0.7f, 0x00000000, 0xaa000000);
            }
            onRender();
            GlStateManager.scale(1, 1, 1);
            GlStateManager.popMatrix();
        }
    }

    /**
     * Called when the screen is world (null)
     */
    public void hudDraw() {
        if (mc.world != null && mc.player != null) {
            isScreenGui = false;
            setWidth(getWidth());
            setHeight(getHeight());

            GlStateManager.pushMatrix();
            GlStateManager.scale(getScale(), getScale(), getScale());
            onRender();
            GlStateManager.scale(1, 1, 1);
            GlStateManager.popMatrix();
        }
    }

    public void guiUpdate(int mouseX, int mouseY, float partialTicks) {
        if (dragging) {
            setX(mouseX - draggingX);
            setY(mouseY - draggingY);
            GlStateManager.pushMatrix();
            GlStateManager.scale(getScale(), getScale(), getScale());
            Render2DUtil.drawRect(getX(), getY(), getX() + getWidth(), getY() + getHeight(),  new Color(51, 204, 255, 130).getRGB(), -100);
            GlStateManager.scale(1, 1, 1);
            GlStateManager.popMatrix();
        }
    }

    public void guiMouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (GuiUtil.isHovered(this, mouseX, mouseY)) {
            setDragging(true);
            draggingX = mouseX - getX();
            draggingY = mouseY - getY();
        }
    }

    public void guiMouseReleased(int mouseX, int mouseY, int mouseButton) {
        setDragging(false);
    }

    @Override
    public Collection<Listener<?>> getListeners() {
        return this.listeners;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        else if (o instanceof HudElement) {
            return this.name != null && this.name.equals(((HudElement) o).name);
        }

        return false;
    }

    public String surroundWithBrackets(String text) {
        return HudRenderUtil.getBracketsColor() + HudRenderUtil.getBrackets()[0] + HudRenderUtil.getBracketsTextColor() + text + HudRenderUtil.getBracketsColor() + HudRenderUtil.getBrackets()[1] + TextColor.WHITE;
    }

    public float getX() {
        return x.getValue();
    }

    public void setX(float x) {
        ScaledResolution resolution = new ScaledResolution(mc);
        this.x.setValue(MathHelper.clamp(x, 0, resolution.getScaledWidth() - width));
    }

    public float getY() {
        return MathHelper.clamp(y.getValue(), HudPositionOffsetManager.getY(), HudPositionOffsetManager.getEndY() - height);
    }

    public void setY(float y) {
        ScaledResolution resolution = new ScaledResolution(mc);
        this.y.setValue(MathHelper.clamp(y, 0, resolution.getScaledHeight() - height));
    }

    public int getZ() {
        return z.getValue();
    }

    public void setZ(int z) {
        this.z.setValue(z);
    }

    public float getScale() {
        return scale.getValue();
    }

    public void setScale(float scale) {
        this.scale.setValue(scale);
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void setDraggingX(float draggingX) {
        this.draggingX = draggingX;
    }

    public void setDraggingY(float draggingY) {
        this.draggingY = draggingY;
    }

    public boolean isDragging() {
        return dragging;
    }

    public void setDragging(boolean dragging) {
        this.dragging = dragging;
    }
}
