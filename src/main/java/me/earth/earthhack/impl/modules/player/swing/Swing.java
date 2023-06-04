package me.earth.earthhack.impl.modules.player.swing;

import me.earth.earthhack.api.module.Module;
import me.earth.earthhack.api.module.util.Category;
import me.earth.earthhack.api.setting.settings.BooleanSetting;
import me.earth.earthhack.api.setting.settings.EnumSetting;
import me.earth.earthhack.api.setting.settings.NumberSetting;
import me.earth.earthhack.impl.event.events.misc.UpdateEvent;
import me.earth.earthhack.impl.event.events.network.PacketEvent;
import me.earth.earthhack.impl.event.listeners.LambdaListener;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.util.EnumHand;

public class Swing extends Module {
    public final NumberSetting<Integer> swingSpeed =
            register(new NumberSetting<>("Swing-Delay", 6, 0, 20));
    public final BooleanSetting clientside =
            register(new BooleanSetting("ClientSide", false));
    protected final EnumSetting<SwingModes> mode =
            register(new EnumSetting<>("Hand", SwingModes.Mainhand));


    public Swing() {
        super("Swing", Category.Player);
        this.setData(new SwingData(this));
        this.listeners.add(new LambdaListener<>(UpdateEvent.class, e -> {
            if(mc.player == null && mc.world == null) return;

            switch(mode.getValue()){
                case Mainhand:
                    mc.player.swingingHand = EnumHand.MAIN_HAND;
                break;

                case Offhand:
                    mc.player.swingingHand = EnumHand.OFF_HAND;
                break;

            } // Using a switch rn for the future, since I plan on implementing Shuffle/switch for this
        }));
        this.listeners.add(new LambdaListener<>(PacketEvent.Send.class,e -> {
            if (!clientside.getValue()) {
                if (e.getPacket() instanceof CPacketAnimation) {
                    switch (mode.getValue())
                    {
                        case Mainhand:
                           if (((CPacketAnimation) e.getPacket()).getHand() != EnumHand.MAIN_HAND)
                           {
                               e.setCancelled(true);
                               mc.player.swingArm(EnumHand.MAIN_HAND);
                           }
                        break; // Using a switch rn for the future, since I plan on implementing Shuffle/Switch for this

                        case Offhand:
                           if (((CPacketAnimation) e.getPacket()).getHand() != EnumHand.OFF_HAND)
                           {
                                e.setCancelled(true);
                                mc.player.swingArm(EnumHand.OFF_HAND);
                           }
                        break;
                    }
                }
            }
        }));
    }

}
