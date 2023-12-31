package me.earth.earthhack.api.module.data;

import me.earth.earthhack.api.config.preset.DefaultPreset;
import me.earth.earthhack.api.module.Module;
import me.earth.earthhack.api.util.AbstractData;

public class DefaultData<M extends Module> extends AbstractData<M>
{
    public DefaultData(M module)
    {
        super(module);
        this.presets.add(new DefaultPreset<>(module));
        register(module.getSetting("Enabled"),
                "Enables this module.");
        register(module.getSetting("Name"),
                "Name shown in the Arraylist.");
        register(module.getSetting("Hidden"),
                "Decides if this module should show up in the Arraylist.");
        register(module.getSetting("Bind"),
                "Keybind to toggle this module.");
        register(module.getSetting("Toggle"),
                "<Normal>: Toggle when you press the bind.\n" +
                "<Hold>: Toggle when you press or release.\n"   +
                "<Disable>: Toggle when pressed, disable when released.");
    }

    @Override
    public int getColor()
    {
        return 0xffffffff;
    }

    @Override
    public String getDescription()
    {
        return "A " + module.getCategory().toString() + " module.";
    }

    @Override
    public String[] getAliases() {
        return new String[]{};
    }

}
