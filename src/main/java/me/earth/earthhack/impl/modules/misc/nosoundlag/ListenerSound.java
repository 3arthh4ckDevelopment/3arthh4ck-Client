package me.earth.earthhack.impl.modules.misc.nosoundlag;

import me.earth.earthhack.impl.event.events.network.PacketEvent;
import me.earth.earthhack.impl.event.listeners.ModuleListener;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.SoundEvent;

final class ListenerSound extends
        ModuleListener<NoSoundLag, PacketEvent.Receive<SPacketSoundEffect>>
{
    public ListenerSound(NoSoundLag module)
    {
        super(module, PacketEvent.Receive.class, SPacketSoundEffect.class);
    }

    @Override
    public void invoke(PacketEvent.Receive<SPacketSoundEffect> event)
    {
        SoundEvent sound = event.getPacket().getSound();
        if ((module.armor.getValue() && NoSoundLag.ARMOR_SOUNDS.contains(sound))
                || (module.withers.getValue() && NoSoundLag.WITHER_SOUNDS.contains(sound)))
        {
            event.setCancelled(true);
        }
    }

}
