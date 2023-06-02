package me.earth.earthhack.impl.util.discord;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import me.earth.earthhack.api.util.interfaces.Globals;
import me.earth.earthhack.impl.Earthhack;
import me.earth.earthhack.impl.modules.client.rpc.LargeImage;
import me.earth.earthhack.impl.modules.client.rpc.RPC;
import me.earth.earthhack.impl.modules.client.rpc.SmallImage;
import me.earth.earthhack.impl.util.math.StopWatch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

public class DiscordPresence implements Globals
{
    private static final Logger LOGGER = LogManager.getLogger(DiscordPresence.class);
    private static final DiscordRichPresence presence = new DiscordRichPresence();
    private static final DiscordRPC rpc = DiscordRPC.INSTANCE;
    private final RPC module;
    private Thread thread;
    private int catCounterBig = 1, catCounterSmall = 1;
    private boolean customId = false;

    public void details() {
        if (module.logoBig.getValue() == LargeImage.Logo) {
            presence.largeImageKey = "logo";
            presence.largeImageText = Earthhack.NAME + " " + Earthhack.VERSION;
        } else if (module.logoBig.getValue() == LargeImage.Skin) {
            presence.largeImageKey = "skin";
            presence.largeImageText = Earthhack.NAME + " " + Earthhack.VERSION;
        } else if (module.logoBig.getValue() == LargeImage.Phobos) {
            presence.largeImageKey = "phobos";
            presence.largeImageText = Earthhack.NAME + " " + Earthhack.VERSION;
        } else if (module.logoBig.getValue() == LargeImage.Cats) {
            presence.largeImageKey = "cat" + catCounterBig;
            presence.largeImageText = "EarthCat " + Earthhack.VERSION;
            if (catCounterBig >= 16) {
                catCounterBig = 0;
            }
            catCounterBig++;
        }

        if (module.logoSmall.getValue() != SmallImage.None) {
            if (module.logoSmall.getValue() == SmallImage.Logo) {
                presence.smallImageKey = "logo";
                presence.smallImageText = Earthhack.NAME + " " + Earthhack.VERSION;
            } else if (module.logoSmall.getValue() == SmallImage.Skin) {
                presence.smallImageKey = "skin";
                presence.smallImageText = Earthhack.NAME + " " + Earthhack.VERSION;
            } else if (module.logoSmall.getValue() == SmallImage.Phobos) {
                presence.smallImageKey = "phobos";
                presence.smallImageText = Earthhack.NAME + " " + Earthhack.VERSION;
            } else if (module.logoSmall.getValue() == SmallImage.Cats) {
                presence.largeImageKey = "cat" + catCounterSmall;
                presence.smallImageText = "EarthCat " + Earthhack.VERSION;
                if (catCounterSmall >= 16) {
                    catCounterSmall = 0;
                }
                catCounterSmall++;
            }
        }
        if (customId) {
            presence.largeImageKey = module.assetLarge.getValue();
            if (module.assetLargeText.getValue() != module.assetLargeText.getInitial()) {
                presence.largeImageText = module.assetLargeText.getValue();
            } else {
                presence.largeImageText = module.assetLarge.getValue();
            }
            if (module.smallImage.getValue()) {
                presence.smallImageKey = module.assetSmall.getValue();
                if (module.assetSmallText.getValue() != module.assetSmallText.getInitial()) {
                    presence.smallImageText = module.assetSmallText.getValue();
                } else {
                    presence.smallImageText = module.assetSmall.getValue();
                }
            }
        }
    }

    public DiscordPresence(RPC module)
    {
        this.module = module;
    }

    public synchronized void start()
    {
        if (thread != null)
        {
            thread.interrupt();
        }

        LOGGER.info("Initializing Discord RPC");
        DiscordEventHandlers handlers = new DiscordEventHandlers();
        if (module.logoBig.getValue() == LargeImage.Custom && module.logoSmall.getValue() == SmallImage.Custom) {
            rpc.Discord_Initialize(module.custom.getValue(), handlers, true, "");
            rpc.Discord_Register(module.custom.getValue(), null);
            customId = true;
        } else {
            rpc.Discord_Initialize("1076164046249791628", handlers, true, "");
            rpc.Discord_Register("1076164046249791628", null);
        }

        presence.startTimestamp = System.currentTimeMillis() / 1000L;
        presence.details = module.Line1.getValue();
        presence.state = line2();
        details();

        rpc.Discord_UpdatePresence(DiscordPresence.presence);
        StopWatch timer = new StopWatch();
        String oldlogoLarge = module.logoBig.getValue().toString();
        String oldlogoSmall = module.logoSmall.getValue().toString();
        timer.reset();
        thread = new Thread(() ->
        {
            while (!Thread.currentThread().isInterrupted())
            {
                try
                {
                    //noinspection BusyWait
                    Thread.sleep(2000);
                }
                catch (InterruptedException ignored)
                {
                    Thread.currentThread().interrupt();
                    return;
                }

                rpc.Discord_RunCallbacks();
                presence.details = module.Line1.getValue();
                presence.state = line2();
                if (timer.passed(1000))
                {
                    if (oldlogoLarge != module.logoBig.getValue().toString() || oldlogoSmall != module.logoSmall.getValue().toString()) {
                        stop();
                        start();
                    } else {
                        details();

                        if (module.join.getValue()) {
                            presence.partyId = "id";
                            presence.joinSecret = "secret";
                            presence.partyMax = module.partyMax.getValue();
                            presence.partySize = 1;
                        }
                        rpc.Discord_UpdatePresence(presence);
                    }
                }
            }
        }, "RPC-Callback-Handler");
        timer.reset();
        thread.setDaemon(true);
        thread.start();
    }

    public synchronized void stop()
    {
        LOGGER.info("Shutting down Discord RPC");
        if (thread != null && !thread.isInterrupted())
        {
            thread.interrupt();
            thread = null;
        }

        rpc.Discord_Shutdown();
    }

    private String line2()
    {
        // module.showIP.getValue() twice, because it won't work unless I do this like this lol

        return module.showIP.getValue()
                ? module.showIP.getValue()
                    ? "Playing on " + Objects.requireNonNull(mc.getCurrentServerData()).serverIP + "!"
                    : module.Line2.getValue()
                : mc.player == null
                    ? "Not in-game."
                    : mc.isIntegratedServerRunning()
                        ? "Playing Singleplayer."
                        : "Playing Multiplayer.";

    }
}
