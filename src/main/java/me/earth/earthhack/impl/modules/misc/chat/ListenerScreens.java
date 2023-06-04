package me.earth.earthhack.impl.modules.misc.chat;

import me.earth.earthhack.impl.event.events.render.GuiScreenEvent;
import me.earth.earthhack.impl.event.listeners.ModuleListener;
import net.minecraft.client.gui.GuiGameOver;

final class ListenerScreens extends
        ModuleListener<Chat, GuiScreenEvent<GuiGameOver>>
{
    public ListenerScreens(Chat module)
    {
        super(module, GuiScreenEvent.class, GuiGameOver.class);
    }

    @Override
    public void invoke(GuiScreenEvent<GuiGameOver> event)
    {
        if (mc.player != null || mc.world != null || mc.currentScreen != null)
        {
            if (module.kit.getValue()) {
                module.kitTimer.reset();
                module.needsKit = true;
            }
        }
    }

}
