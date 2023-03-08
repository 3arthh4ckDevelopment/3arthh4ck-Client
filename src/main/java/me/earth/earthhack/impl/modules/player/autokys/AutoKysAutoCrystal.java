package me.earth.earthhack.impl.modules.player.autokys;

import me.earth.earthhack.api.module.util.Category;
import me.earth.earthhack.impl.modules.combat.autocrystal.AutoCrystal;
import me.earth.earthhack.impl.modules.combat.autocrystal.modes.Target;

public class AutoKysAutoCrystal extends AutoCrystal {
    public AutoKysAutoCrystal() {
        super("SuicideAutoCrystal", Category.Combat);
        this.suicide.setValue(true);
        this.unregister(suicide);
        this.maxSelfBreak.setValue(20.0f);
        this.unregister(maxSelfBreak);
        this.efficient.setValue(false);
        this.unregister(efficient);
        this.targetMode.setValue(Target.Closest);
        this.unregister(targetMode);
    }

    @Override
    public boolean isSuicideModule() {
        return true;
    }

}
