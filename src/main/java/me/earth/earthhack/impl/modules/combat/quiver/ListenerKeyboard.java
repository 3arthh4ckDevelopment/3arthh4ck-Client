package me.earth.earthhack.impl.modules.combat.quiver;

import me.earth.earthhack.impl.event.events.keyboard.KeyboardEvent;
import me.earth.earthhack.impl.event.listeners.ModuleListener;

final class ListenerKeyboard extends ModuleListener<Quiver, KeyboardEvent>
{
    public ListenerKeyboard(Quiver module)
    {
        super(module, KeyboardEvent.class);
    }

    @Override
    public void invoke(KeyboardEvent event)
    {
        if (module.cycleButton.getValue().getKey() == event.getKey()
            && event.getEventState())
        {
            module.cycle(false, false);
        }
    }

}
