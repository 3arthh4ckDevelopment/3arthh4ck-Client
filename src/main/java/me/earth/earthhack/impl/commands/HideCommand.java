package me.earth.earthhack.impl.commands;

import me.earth.earthhack.api.module.Module;
import me.earth.earthhack.api.util.interfaces.Globals;
import me.earth.earthhack.impl.commands.abstracts.AbstractModuleCommand;
import me.earth.earthhack.impl.commands.gui.YesNoNonPausing;
import me.earth.earthhack.impl.commands.util.CommandDescriptions;
import me.earth.earthhack.impl.managers.Managers;
import me.earth.earthhack.impl.managers.thread.scheduler.Scheduler;
import me.earth.earthhack.impl.util.text.ChatUtil;
import me.earth.earthhack.impl.util.text.TextColor;
import net.minecraft.client.gui.GuiScreen;

public class HideCommand extends AbstractModuleCommand implements Globals {
    public HideCommand(){
        super("hide", new String[][]{{"module"}});
        CommandDescriptions.register(this, "Hides modules from the ClickGui.");
    }

    @Override
    public void execute(String[] args) {
        if (args.length == 1)
        {
            GuiScreen before = mc.currentScreen;
            Scheduler.getInstance().schedule(() ->
                    mc.displayGuiScreen(new YesNoNonPausing(
                            (result, id) ->
                            {
                                mc.displayGuiScreen(before);
                                if (!result)
                                    return;

                                for (Module m : Managers.MODULES.getRegistered())
                                    m.setShown(false);
                            },
                            "Hide every module?",
                            "",
                            1337)
                    ));
            return;
        }

        Module module = Managers.MODULES.getObject(args[1]);
        if (module == null) {
            ChatUtil.sendMessage(TextColor.RED + "Module " + TextColor.WHITE
                    + args[1] + TextColor.RED + " not found!");
        } else {
            if (module.isVisible()) {
                module.setShown(false);
                ChatUtil.sendMessage(TextColor.GREEN + "Module " + TextColor.WHITE
                        + args[1] + TextColor.GREEN + " is now hidden!");
            } else {
                ChatUtil.sendMessage(TextColor.RED + "Module " + TextColor.WHITE
                        + args[1] + TextColor.RED + " is already hidden!");
            }
        }
    }
}
