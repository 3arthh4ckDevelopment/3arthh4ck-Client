package me.earth.earthhack.impl.hud.clock;

import me.earth.earthhack.api.hud.HudElement;
import me.earth.earthhack.api.setting.Setting;
import me.earth.earthhack.api.setting.settings.BooleanSetting;
import me.earth.earthhack.api.setting.settings.ColorSetting;
import me.earth.earthhack.api.setting.settings.NumberSetting;
import me.earth.earthhack.impl.util.client.SimpleHudData;
import me.earth.earthhack.impl.util.render.Render2DUtil;
import net.minecraft.util.math.Vec2f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Disk;
import org.lwjgl.util.glu.GLU;

import java.awt.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glVertex3d;

public class Clock extends HudElement {

    private final Setting<Integer> slices =
            register(new NumberSetting<>("Slices", 5,0,5));
    private final Setting<Integer> loops =
            register(new NumberSetting<>("Loops", 1,0,5));
    private final Setting<Float> lineWidth =
            register(new NumberSetting<>("LineWidth", 1.0f,0.0f,5.0f));
    private final Setting<Float> scale =
            register(new NumberSetting<>("Scale", 8.0f,1.0f,15.0f));
    private final Setting<Boolean> outline =
            register(new BooleanSetting("OutLine", false));
    private final Setting<Boolean> rainbow =
            register(new BooleanSetting("Rainbow", false));
    private final Setting<Color> colorS =
            register(new ColorSetting("Color", new Color(0, 184, 255, 255)));

    private void render() {
        float x = getX() + getWidth() / 2;
        float y = getY() + getHeight() / 2;
        float radius = scale.getValue() + 2;
        Color color = colorS.getValue();
        Disk disk = new Disk();
        int hourAngle = 180 + -((Calendar.getInstance().get(Calendar.HOUR) * 30) + (Calendar.getInstance().get(Calendar.MINUTE) / 2)); // DO NOT MODIFY THE -
        int minuteAngle = 180 + -((Calendar.getInstance().get(Calendar.MINUTE) * 6) + (Calendar.getInstance().get(Calendar.SECOND) / 10));
        int secondAngle = 180 + -((Calendar.getInstance().get(Calendar.SECOND) * 6));
        if (!outline.getValue()) {
            GL11.glPushMatrix();
            GL11.glColor4f(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
            GL11.glBlendFunc(770, 771);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glLineWidth(lineWidth.getValue());
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            disk.setOrientation(GLU.GLU_OUTSIDE);
            disk.setDrawStyle(GLU.GLU_FILL);
            GL11.glTranslated(x, y, 0.0d);
            disk.draw(0.0f, radius, slices.getValue(), loops.getValue());
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glPopMatrix();
        } else {
            GL11.glPushMatrix();
            GL11.glColor4f(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glLineWidth(lineWidth.getValue());
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glBegin(GL11.GL_LINE_STRIP);
            List<Vec2f> hVectors = new ArrayList<>();
            float hue = (System.currentTimeMillis() % (360 * 20)) / (360f * 20);
            for (int i = 0; i <= 360; i++) {
                Vec2f vec = new Vec2f(x + (float) Math.sin(i * Math.PI / 180.0) * radius, y + (float) Math.cos(i * Math.PI / 180.0) * radius);
                hVectors.add(vec);
            }
            Color color1 = (rainbow.getValue() ? new Color(Color.HSBtoRGB(hue, 1.0f, 1.0f)) : colorS.getValue());
            for (int j = 0; j < hVectors.size() - 1; j++) {
                glColor4f(color1.getRed() / 255f, color1.getGreen() / 255f, color1.getBlue() / 255f, color1.getAlpha() / 255f);
                glVertex3d(hVectors.get(j).x, hVectors.get(j).y, 0.0d);
                glVertex3d(hVectors.get(j + 1).x, hVectors.get(j + 1).y, 0.0d);
                if (rainbow.getValue()) {
                    hue += (1.0f / 360.0f);
                    color1 = new Color(Color.HSBtoRGB(hue, 1.0f, 1.0f));
                } else {
                    color1 = colorS.getValue();
                }
            }
            GL11.glEnd();
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glPopMatrix();
        }

        Render2DUtil.drawLine(x, y, x + ((float) Math.sin(hourAngle * Math.PI / 180.0) * (radius / 2.0f)), y + ((float) Math.cos(hourAngle * Math.PI / 180.0) * (radius / 2.0f)), 1.0f, Color.WHITE.getRGB());
        Render2DUtil.drawLine(x, y, x + ((float) Math.sin(minuteAngle * Math.PI / 180.0) * (radius - (radius / 10.0f))), y + ((float) Math.cos(minuteAngle * Math.PI / 180.0) * (radius - (radius / 10.0f))), 1.0f, Color.WHITE.getRGB());
        Render2DUtil.drawLine(x, y, x + ((float) Math.sin(secondAngle * Math.PI / 180.0) * (radius - (radius / 10.0f))), y + ((float) Math.cos(secondAngle * Math.PI / 180.0) * (radius - (radius / 10.0f))), 1.0f, Color.RED.getRGB());
    }

    public Clock() {
        super("Clock", 160, 370);
        this.setData(new SimpleHudData(this, "Displays a clock"));
    }

    @Override
    public void guiDraw(int mouseX, int mouseY, float partialTicks) {
        super.guiDraw(mouseX, mouseY, partialTicks);
        render();
    }

    @Override
    public void hudDraw(float partialTicks) {
        render();
    }

    @Override
    public void guiUpdate(int mouseX, int mouseY, float partialTicks) {
        super.guiUpdate(mouseX, mouseY, partialTicks);
        setWidth(getWidth());
        setHeight(getHeight());
    }

    @Override
    public void hudUpdate(float partialTicks) {
        super.hudUpdate(partialTicks);
        setWidth(getWidth());
        setHeight(getHeight());
    }

    @Override
    public float getWidth() {
        return 4.0f * scale.getValue();
    }

    @Override
    public float getHeight() {
        return 5.2f * scale.getValue() / 2.0f;
    }

}
