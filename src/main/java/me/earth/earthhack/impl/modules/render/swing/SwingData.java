package me.earth.earthhack.impl.modules.render.swing;

import me.earth.earthhack.api.module.data.DefaultData;

final class SwingData extends DefaultData<Swing> {
    public SwingData(Swing module) {
        super(module);
        register(module.swingSpeed, "The speed at which your hand swings. 6 is default speed. Higher the speed is, more it will take to swing.");

    }

    @Override
    public int getColor() {
        return 0xffaa001D;
    }

    @Override
    public String getDescription() {
        return "Modifies how swinging hands work.";
    }
}