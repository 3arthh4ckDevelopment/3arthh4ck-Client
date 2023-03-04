package me.earth.earthhack.impl.modules.client.clickgui;

import me.earth.earthhack.api.module.data.DefaultData;

public class ClickGuiData extends DefaultData<ClickGui> {
    public ClickGuiData(ClickGui module){
        super(module);
        register(module.guiScale, "Sets the scale for the ClickGUI. This is" +
                " currently under development, because it is" +
                " handled very poorly, and will go haywire if" +
                " you exceed values over 1.3 and 0.5.");
        register(module.color, "Color of the ClickGui.");
        register(module.catEars, "Draws very cute CatEars on the ClickGui.");
        register(module.blur, "Blurs the background of the ClickGui." +
                " Planned to have a new blur" +
                " implemented soon, this is why" +
                " this is currently called OldBlur.");
        register(module.blurAmount, "Blur strength for Blur.");
        register(module.blurSize, "Size of the blur, for Blur.");
    }
    public String getDescription() {
        return "Beautiful ClickGui by oHare.";
    }
}
