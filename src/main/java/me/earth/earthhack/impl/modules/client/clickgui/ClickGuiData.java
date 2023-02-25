package me.earth.earthhack.impl.modules.client.clickgui;

import me.earth.earthhack.api.module.data.DefaultData;

public class ClickGuiData extends DefaultData<ClickGui> {
    public ClickGuiData(ClickGui module){
        super(module);
        register(module.guiScale, "Sets the scale for the ClickGUI. As of now, "
                + "this is under development and doesn't yet support CatEars.");
        register(module.color, "Color of the ClickGui.");
        register(module.catEars, "Draws very cute CatEars on the ClickGui.");
        register(module.blur, "Blurs the background of the ClickGui." +
                " Planned to be redone soon.");
        register(module.blurAmount, "Blur strength for Blur.");
        register(module.blurSize, "Size of the blur, for Blur.");
    }
    public String getData() {
        return "Beautiful ClickGui by oHare";
    }
}
