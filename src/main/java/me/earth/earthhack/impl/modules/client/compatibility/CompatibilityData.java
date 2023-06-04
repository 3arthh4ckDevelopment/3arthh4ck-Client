package me.earth.earthhack.impl.modules.client.compatibility;

import me.earth.earthhack.api.module.data.DefaultData;

final class CompatibilityData extends DefaultData<Compatibility>
{
    public CompatibilityData(Compatibility module)
    {
        super(module);
        register(module.showRotations, "Makes Rotations compatible with Future.");
        register(module.swiftSneak, "Makes the Swift Sneak Enchantment work.");
    }

    @Override
    public int getColor()
    {
        return 0xffffffff;
    }

    @Override
    public String getDescription()
    {
        return "Allows the client to be compatible with other things.";
    }

}
