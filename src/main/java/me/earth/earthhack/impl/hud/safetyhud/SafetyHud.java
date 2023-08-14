package me.earth.earthhack.impl.hud.safetyhud;

import me.earth.earthhack.api.hud.HudElement;
import me.earth.earthhack.api.setting.Setting;
import me.earth.earthhack.api.setting.settings.BooleanSetting;
import me.earth.earthhack.api.setting.settings.EnumSetting;
import me.earth.earthhack.api.setting.settings.StringSetting;
import me.earth.earthhack.impl.managers.Managers;
import me.earth.earthhack.impl.util.client.SimpleHudData;
import me.earth.earthhack.impl.util.render.hud.HudRenderUtil;
import me.earth.earthhack.impl.util.text.TextColor;
import net.minecraft.client.renderer.GlStateManager;

public class SafetyHud extends HudElement {

    private final Setting<TextColor> safeColor =
            register(new EnumSetting<>("SafeColor", TextColor.Green));
    private final Setting<TextColor> unsafeColor =
            register(new EnumSetting<>("UnsafeColor", TextColor.Red));
    private final Setting<String> name =
            register(new StringSetting("CustomName", ""));
    private final Setting<String> safeName =
            register(new StringSetting("SafeName", "SAFE"));
    private final Setting<String> unsafeName =
            register(new StringSetting("UnsafeName", "UNSAFE"));
    private final Setting<Boolean> showBrackets =
            register(new BooleanSetting("ShowBrackets", true));
    private final Setting<Boolean> customBrackets =
            register(new BooleanSetting("CustomBrackets", true));

    String text = "UNSAFE";

    private void render() {
        if (mc.player != null && mc.world != null) {
            text = name.getValue()
                    + (showBrackets.getValue() ? actualBracket()[0] : "")
                    + (Managers.SAFETY.isSafe() ? safeColor.getValue().getColor() + safeName.getValue() : unsafeColor.getValue().getColor() + unsafeName.getValue())
                    + (showBrackets.getValue() ? actualBracket()[1] : "");
        }

        GlStateManager.pushMatrix();
        HudRenderUtil.renderText(text, getX(), getY());
        GlStateManager.popMatrix();
    }

    private String[] actualBracket() {
        if (customBrackets.getValue())
            return new String[]{ HudRenderUtil.BracketsColor() + HudRenderUtil.Brackets()[0] + HudRenderUtil.BracketsTextColor(), HudRenderUtil.BracketsColor() + HudRenderUtil.Brackets()[1] + TextColor.WHITE };
        else
            return new String[]{ TextColor.GRAY + "[", TextColor.GRAY + "]"};
    }

    public SafetyHud() {
        super("Safety", 30, 30);
        this.setData(new SimpleHudData(this, "Displays if you're safe or not."));
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
        setHeight(Managers.TEXT.getStringHeight());
    }

    @Override
    public void hudUpdate(float partialTicks) {
        super.hudUpdate(partialTicks);
        setWidth(getWidth());
        setHeight(getHeight());
    }

    @Override
    public float getWidth() {
        return Managers.TEXT.getStringWidth(text);
    }

    @Override
    public float getHeight() {
        return Managers.TEXT.getStringHeight();
    }

}
