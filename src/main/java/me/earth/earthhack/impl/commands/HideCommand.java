package me.earth.earthhack.impl.commands;

import me.earth.earthhack.api.module.Module;
import me.earth.earthhack.api.module.util.Hidden;
import me.earth.earthhack.impl.commands.abstracts.AbstractModuleCommand;
import me.earth.earthhack.impl.commands.util.CommandDescriptions;
import me.earth.earthhack.impl.managers.Managers;
import me.earth.earthhack.impl.util.text.ChatUtil;
import me.earth.earthhack.impl.util.text.TextColor;

public class HideCommand extends AbstractModuleCommand {
    public HideCommand()
    {
        super("hide", new String[][]{{"hide"}});
        CommandDescriptions.register(this, "Sets the visibility of modules in the ClickGUI and Arraylist.");
    }
    @Override
    public void execute(String[] args) {
        if(args.length == 0)
            return;
        if (args.length == 1)
        {
            ChatUtil.sendMessage(TextColor.RED + " Please specify a module!");
            return;
        }

        Module module = Managers.MODULES.getObject(args[1]);

        if (module == null)
        {
            ChatUtil.sendMessage(TextColor.RED + "Module " + TextColor.WHITE
                    + args[1] + TextColor.RED + " not found!");
            return;
        }

        if (args.length == 2)
        {
            if(!module.getHiddenState())
            {
                module.setHiddenState(true);
                module.setHidden(Hidden.Hidden); // For hiding the module from the Arraylist, this isn't that necessary but a detail.
                Managers.MODULES.getHiddenModules().add(module);
                Managers.MODULES.init();
                ChatUtil.sendMessage(TextColor.GREEN + " Module successfully hidden!");
            }
            else
                ChatUtil.sendMessage(TextColor.RED + " This module is already hidden!");

        }
    }
}
