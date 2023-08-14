package me.earth.earthhack.impl.modules.client.notifications;

import me.earth.earthhack.api.module.data.DefaultData;

final class NotificationData extends DefaultData<Notifications>
{
    public NotificationData(Notifications module)
    {
        super(module);
        register(module.modules, "Announces when modules get toggled.");
        register(module.configure, "Configure the which modules should be announced.");
        register(module.categories, "Click through the module categories.");
        register(module.totems, "Announces when players in visual range pop a totem.");
        register(module.totemAmountColor, "Color of the TotemPop Amount in the TotemPop Message.");
        register(module.totemColor, "Color of the TotemPop Message.");
        register(module.totemPlayerColor, "Color of the PlayerName the TotemPop Message.");
        register(module.duration, "Hud notification duration.");
        /*
        register(module.entered, "Announces when players enter visual range.");
        register(module.leave, "Announces when players leave visual range.");
        register(module.enteredColor, "Color of the enter Message.");
        register(module.leftColor, "Color of the leave message.");
        register(module.visualRangePlayerColor, "Color of the PlayerName in the VisualRange messages.");

         */

        register(module.pops, "Announces your own pops.");
        register(module.percentage, "Also announces at this armor piece percentage.");
        register(module.targetDistance, "The target player distance.");
        register(module.name, "Your name.");
    }

    @Override
    public int getColor()
    {
        return 0xff34A1FF;
    }

    @Override
    public String getDescription()
    {
        return "Chat notifications for all sorts of stuff.";
    }

}
