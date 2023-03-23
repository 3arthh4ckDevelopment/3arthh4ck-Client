package me.earth.earthhack.impl.modules.misc.rpc;

import me.earth.earthhack.api.module.Module;
import me.earth.earthhack.api.module.util.Category;
import me.earth.earthhack.api.setting.Setting;
import me.earth.earthhack.api.setting.settings.BooleanSetting;
import me.earth.earthhack.api.setting.settings.StringSetting;
import me.earth.earthhack.impl.event.events.client.ShutDownEvent;
import me.earth.earthhack.impl.event.listeners.LambdaListener;
import me.earth.earthhack.impl.util.discord.DiscordPresence;

public class RPC extends Module
{
    public final Setting<String> state =
            register(new StringSetting("State", ":wave:"));
    public final Setting<String> details =
            register(new StringSetting("Details", ":wave:"));
    public final Setting<Boolean> customDetails =
            register(new BooleanSetting("CustomDetails", false));
    public final Setting<Boolean> showIP =
            register(new BooleanSetting("ShowIP", false));

    // private final DiscordPresence presence = new DiscordPresence(this); // this causes issues for pojav? weird

    public RPC()
    {
        super("RPC", Category.Misc);

        if(System.getProperty("os.version").toLowerCase().contains("android"))      // for Pojav Launcher
            return;

        DiscordPresence presence = new DiscordPresence(this);
        this.listeners.add(new LambdaListener<>(ShutDownEvent.class,
                                                e -> presence.stop()));
    }

    @Override
    protected void onEnable()
    {
        if(!System.getProperty("os.version").toLowerCase().contains("android"))
        {
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
