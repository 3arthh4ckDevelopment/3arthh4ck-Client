package me.earth.earthhack.impl.modules.client.clickgui;

import me.earth.earthhack.impl.event.events.render.GuiScreenEvent;
import me.earth.earthhack.impl.event.listeners.ModuleListener;
import me.earth.earthhack.impl.gui.click.Click;
import net.minecraft.client.renderer.OpenGlHelper;

final class ListenerScreen extends ModuleListener<ClickGui, GuiScreenEvent<?>>
{
    public ListenerScreen(ClickGui module)
    {
        super(module, GuiScreenEvent.class);
    }

    @Override
    public void invoke(GuiScreenEvent<?> event)
    {
        if (mc.currentScreen instanceof Click)
        {
            module.fromEvent = true;
            module.disable();

            if(!module.newBlur.getValue() && OpenGlHelper.areShadersSupported())
                mc.entityRenderer.stopUseShader();
        }
    }

}
