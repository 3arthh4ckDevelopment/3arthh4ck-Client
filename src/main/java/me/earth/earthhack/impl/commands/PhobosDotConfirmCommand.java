package me.earth.earthhack.impl.commands;

import me.earth.earthhack.api.command.Command;
import me.earth.earthhack.api.util.interfaces.Globals;
import me.earth.earthhack.impl.util.text.ChatUtil;
import net.minecraft.entity.player.EntityPlayer;

public class PhobosDotConfirmCommand extends Command implements Globals {
    public PhobosDotConfirmCommand() {
        super(new String[][]{{"PhobosConfirm"}, {"player"}}, true);
    }

    @Override
    public void execute(String[] args) {
        if (mc.player != null && mc.world != null) {
            if (args.length > 1) {
                for (EntityPlayer p : mc.world.playerEntities) {
                    if (args[1].equals(p.getName())) {
                        ChatUtil.sendMessage("/msg " + p.getName() + "phobos.confirm##1337", 514087);
                        ChatUtil.deleteMessage(514087);
                    }
                }
            }
        }
    }

}
