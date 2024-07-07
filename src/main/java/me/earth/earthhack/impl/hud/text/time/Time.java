package me.earth.earthhack.impl.hud.text.time;

import me.earth.earthhack.api.hud.HudCategory;
import me.earth.earthhack.api.hud.HudElement;
import me.earth.earthhack.api.setting.Setting;
import me.earth.earthhack.api.setting.settings.StringSetting;
import me.earth.earthhack.impl.managers.Managers;
import me.earth.earthhack.impl.util.render.hud.HudRenderUtil;
import me.earth.earthhack.impl.util.text.ChatIDs;
import me.earth.earthhack.impl.util.text.ChatUtil;
import me.earth.earthhack.impl.util.text.TextColor;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Time extends HudElement {

    private final Setting<String> name =
            register(new StringSetting("Text", "Time"));
    private final Setting<String> timeFormat =
            register(new StringSetting("TimeFormat", "hh:mm:ss"));

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss");
    private String text = TextColor.GRAY + TextColor.RED + "Invalid";

    protected void onRender() {
        LocalDateTime actualTime = LocalDateTime.now();
        try {
            text = name.getValue() + " " + TextColor.GRAY + formatter.format(actualTime);
        } catch (DateTimeException e) {
            Managers.CHAT.sendDeleteMessage(TextColor.RED + "Can not render time: " + e.getMessage(), this.getName(), ChatIDs.MODULE);
            text = name.getValue() + TextColor.GRAY + TextColor.RED + "Invalid";
        }

        HudRenderUtil.renderText(text, getX(), getY());
    }

    public Time() {
        super("Time", "Displays the time.", HudCategory.Text, 320, 70);

        timeFormat.addObserver(e -> {
            if (!e.isCancelled()) {
                try {
                    formatter = DateTimeFormatter.ofPattern(e.getValue());
                } catch (IllegalArgumentException iae) {
                    ChatUtil.sendMessageScheduled(TextColor.RED + "Invalid DateTimeFormat: " + TextColor.WHITE + e.getValue());
                    formatter = DateTimeFormatter.ofPattern("hh:mm:ss");
                }
            }
        });
    }

    @Override
    public float getWidth() {
        return Managers.TEXT.getStringWidth(text.trim());
    }

    @Override
    public float getHeight() {
        return Managers.TEXT.getStringHeight();
    }
}
