package me.earth.earthhack.impl.commands;

import me.earth.earthhack.api.module.Module;
import me.earth.earthhack.api.module.util.Hidden;
import me.earth.earthhack.api.register.exception.CantUnregisterException;
import me.earth.earthhack.impl.commands.abstracts.AbstractModuleCommand;
import me.earth.earthhack.impl.commands.util.CommandDescriptions;
import me.earth.earthhack.impl.managers.Managers;
import me.earth.earthhack.impl.util.text.ChatUtil;
import me.earth.earthhack.impl.util.text.TextColor;

public class HideCommand extends AbstractModuleCommand {
    public HideCommand()
    {
        super("hide", new String[][]{{""}}); // doesn't need anything here I think
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
            if(!module.getHiddenState() && module.isRegistered())
            {
                module.setHiddenState(true);
                module.setHidden(Hidden.Hidden); // For hiding the module from the Arraylist, this isn't that necessary but a detail.
                Managers.MODULES.getHiddenModules().add(module); // Adding the module to this List, so it's accessible I guess.

                try{
                    if(!module.isEnabled() && module.getBind() == null)
                    {
                        Managers.MODULES.unregister(module);
                        module.setRegistered(false);
                        ChatUtil.sendMessage(TextColor.GREEN + " Module successfully hidden!");
                    }
                    else
                    {
                        ChatUtil.sendMessage(TextColor.RED + " You can't hide modules you are using or have bound to anything!");
                    }

                    /*
                     * A bad way of handling this? Yes, but it's temporary, and typically modules you want to hide you don't use.
                     * This means, that it doesn't necessarily matter if we just unregister the module, if we can re-register it later using the Show command.
                     */

                }catch(CantUnregisterException ignored) {}
            }
            else
                ChatUtil.sendMessage(TextColor.RED + " This module is already hidden!");

        }
    }
}
