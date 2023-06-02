package me.earth.earthhack.impl.modules.client.rpc;

import me.earth.earthhack.api.module.Module;
import me.earth.earthhack.api.module.util.Category;
import me.earth.earthhack.api.setting.Complexity;
import me.earth.earthhack.api.setting.Setting;
import me.earth.earthhack.api.setting.settings.BooleanSetting;
import me.earth.earthhack.api.setting.settings.EnumSetting;
import me.earth.earthhack.api.setting.settings.NumberSetting;
import me.earth.earthhack.api.setting.settings.StringSetting;
import me.earth.earthhack.impl.event.events.client.ShutDownEvent;
import me.earth.earthhack.impl.event.listeners.LambdaListener;
import me.earth.earthhack.impl.util.discord.DiscordPresence;

public class RPC extends Module
{
    public final Setting<LargeImage> logoBig =
            register(new EnumSetting<>("LargeLogo", LargeImage.Phobos));
    public final Setting<SmallImage> logoSmall =
            register(new EnumSetting<>("SmallLogo", SmallImage.Skin));
    public final Setting<String> Line1 =
            register(new StringSetting("Line1", "3arthh4ck"));
    public final Setting<String> Line2 =
            register(new StringSetting("Line2", ""));
    public final Setting<Boolean> showIP =
            register(new BooleanSetting("ShowIP", false));
    public final Setting<Boolean> join =
            register(new BooleanSetting("JoinButton", false));
    public final Setting<Integer> partyMax =
            register(new NumberSetting<>("MaxParty", 5, 1, 15));
    public final Setting<String> custom =
            register(new StringSetting("CustomId", "Application ID"))
                    .setComplexity(Complexity.Expert);
    public final Setting<String> assetLarge =
            register(new StringSetting("LargeImage", "Large Asset Name"))
                    .setComplexity(Complexity.Expert);
    public final Setting<String> assetLargeText =
            register(new StringSetting("LargeImageText", "Large Asset Text"))
                    .setComplexity(Complexity.Expert);
    public final Setting<Boolean> smallImage =
            register(new BooleanSetting("SmallImageSetting", false));
    public final Setting<String> assetSmall =
            register(new StringSetting("SmallImage", "Small Asset Name"))
                    .setComplexity(Complexity.Expert);
    public final Setting<String> assetSmallText =
            register(new StringSetting("SmallImageText", "Small Asset Text"))
                    .setComplexity(Complexity.Expert);

    public RPC()
    {
        super("RPC", Category.Client);
        this.setData(new RPCData(this));
        String os = System.getProperty("os.version").toLowerCase();
        if (os.contains("android") || os.contains("ios"))
            return;

        DiscordPresence presence = new DiscordPresence(this);
        this.listeners.add(new LambdaListener<>(ShutDownEvent.class, e -> presence.stop()));
    }

    @Override
    protected void onEnable()
    {
        String os = System.getProperty("os.version").toLowerCase();
        if (os.contains("android") || os.contains("ios"))
            this.disable();
        else {
            DiscordPresence presence = new DiscordPresence(this);
            presence.start();
        }
    }

    @Override
    protected void onDisable()
    {
        DiscordPresence presence = new DiscordPresence(this);
        presence.stop();
    }
}
