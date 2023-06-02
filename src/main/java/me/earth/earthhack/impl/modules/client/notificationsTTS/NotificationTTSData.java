package me.earth.earthhack.impl.modules.client.notificationsTTS;

import me.earth.earthhack.api.module.data.DefaultData;

final class NotificationTTSData extends DefaultData<NotificationsTTS>
{
    public NotificationTTSData(NotificationsTTS module)
    {
        super(module);
        this.descriptions.put(module.target,
                "Announces the target pops.");
        this.descriptions.put(module.pops,
                "Announces your own pops.");
        this.descriptions.put(module.helmet,
                "Announces the helmet durability.");
        this.descriptions.put(module.chest,
                "Announces the chestplate durability.");
        this.descriptions.put(module.legs,
                "Announces the leggings durability.");
        this.descriptions.put(module.boots,
                "Announces the boots durability.");
        this.descriptions.put(module.percentage,
                "Also announces at this armor piece percentage.");
        this.descriptions.put(module.targetDistance,
                "The target player distance.");
        this.descriptions.put(module.name,
                "Your name.");
    }

    @Override
    public int getColor()
    {
        return 0xff34A1FF;
    }

    @Override
    public String getDescription()
    {
        return "TTS notifications for some stuff.";
    }

}
