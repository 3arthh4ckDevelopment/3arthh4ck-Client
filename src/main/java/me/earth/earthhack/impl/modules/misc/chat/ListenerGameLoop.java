package me.earth.earthhack.impl.modules.misc.chat;

import me.earth.earthhack.impl.core.ducks.gui.IGuiNewChat;
import me.earth.earthhack.impl.event.events.misc.GameLoopEvent;
import me.earth.earthhack.impl.event.listeners.ModuleListener;
import me.earth.earthhack.impl.managers.Managers;
import me.earth.earthhack.impl.util.text.ChatIDs;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.network.play.client.CPacketChatMessage;

final class ListenerGameLoop extends ModuleListener<Chat, GameLoopEvent>
{
    public ListenerGameLoop(Chat module)
    {
        super(module, GameLoopEvent.class);
    }

    @Override
    public void invoke(GameLoopEvent event)
    {
        if (!module.cleared && mc.ingameGUI != null)
        {
            IGuiNewChat chat = (IGuiNewChat) mc.ingameGUI.getChatGUI();
            if (chat.getScrollPos() == 0)
                module.clearNoScroll();
        }

        if (!mc.isGamePaused() && !(mc.currentScreen instanceof GuiGameOver) && module.needsKit) {
            module.needsKit = false;
            mc.player.connection.sendPacket(new CPacketChatMessage("/kit " + module.kitName.getValue()));
            Managers.CHAT.sendDeleteMessage(module.kitName.getValue(), module.getName(), ChatIDs.COMMAND);
        }
    }

}
