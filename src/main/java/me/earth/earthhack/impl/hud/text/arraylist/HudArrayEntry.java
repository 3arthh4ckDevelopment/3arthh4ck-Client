package me.earth.earthhack.impl.hud.text.arraylist;

import me.earth.earthhack.api.module.Module;
import me.earth.earthhack.api.module.util.Hidden;
import me.earth.earthhack.api.util.interfaces.Globals;
import me.earth.earthhack.impl.managers.Managers;
import me.earth.earthhack.impl.util.math.StopWatch;
import me.earth.earthhack.impl.util.render.RenderUtil;
import me.earth.earthhack.impl.util.render.hud.HudRenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

/**
 * @author Gerald
 * @since 3/22/2021
 **/

//TODO: entirely rewrite this

public class HudArrayEntry extends HudArrayList implements Globals {
    private Module module;
    private float x, startX;
    private boolean atDesired;
    private final StopWatch stopWatch = new StopWatch();
    public HudArrayEntry(Module module) {
        this.module = module;
        this.x = this.startX = getX();
        stopWatch.reset();
    }

    public void drawArrayEntry(float desiredX, float desiredY) {
        final float textWidth = Managers.TEXT.getStringWidth(getHudName(getModule()));
        final float xSpeed = textWidth / (Minecraft.getDebugFPS() >> 2);
        GlStateManager.pushMatrix();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        RenderUtil.scissor(desiredX - textWidth, desiredY, desiredX, desiredY + Managers.TEXT.getStringHeightI() + 3);
        HudRenderUtil.renderText(getHudName(getModule()), getX(), desiredY);
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GlStateManager.popMatrix();
        if (module.isEnabled() && module.isHidden() != Hidden.Hidden) {
            if (stopWatch.passed(1000)) {
                setX(desiredX - textWidth);
                setAtDesired(true);
            } else {
                if (isAtDesired()) {
                    setX(desiredX - textWidth);
                } else {
                    if (!isDone(desiredX)) {
                        if (getX() != desiredX - textWidth) {
                            setX(Math.max(getX() - xSpeed, desiredX - textWidth));
                        }
                    } else {
                        setAtDesired(true);
                    }
                }
            }
        } else {
            if (!shouldDelete()) {
                setX(getX() + xSpeed);
            } else {
                HudArrayList.getRemoveEntries().put(module,this);
            }
            setAtDesired(false);
            stopWatch.reset();
        }
    }

    private boolean isDone(float desiredX) {
        final float textWidth = Managers.TEXT.getStringWidth(getHudName(getModule()));
        return getX() <= desiredX - textWidth;
    }

    private boolean shouldDelete() {
        return getX() > getStartX();
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public boolean isAtDesired() {
        return atDesired;
    }

    public void setAtDesired(boolean atDesired) {
        this.atDesired = atDesired;
    }

    public float getStartX() {
        return startX;
    }

    public void setStartX(float startX) {
        this.startX = startX;
    }
}
