package me.earth.earthhack.impl.modules.client.clickgui;

import me.earth.earthhack.api.module.data.DefaultData;

public class ClickGuiData extends DefaultData<ClickGui> {
    public ClickGuiData(ClickGui module){
        super(module);
        register(module.guiScale, "Sets the scale for the ClickGUI. This is" +
                " currently under development, because it is" +
                " handled very poorly, and will go haywire if" +
                " you exceed values over 1.3 and 0.5.");
        register(module.color, "Accent color for the ClickGUI. A color for the dark background is planned" +
                " to be implemented soon.");
        register(module.catEars, "Draws very cute CatEars on the ClickGui.");
        register(module.blur, "Blurs the background of the ClickGui." +
                " Planned to have a new blur" +
                " implemented soon, this is why" +
                " this is currently called OldBlur.");
        register(module.newBlur, "Renders a very cool blur for the ClickGui." +
                " This looks more like a Gaussian blur than OldBlur, which is like a linear blur." +
                " In development!!!!");
        register(module.blurAmount, "Blur strength for OldBlur.");
        register(module.blurSize, "Size of the blur, for OldBlur.");
        register(module.scrollSpeed, "How fast the ClickGUI is scrolled through. This doesn't" +
                " smoothen the ClickGUI, but instead just makes navigating around faster" +
                " I guess.");
        register(module.white, "Draws a thin, white outline to a modules settings to not " +
                " get so easily confused where the settings actually end. Recommended," +
                " if you easily get confused, or skip lines without noticing.");
        register(module.description, "Whether or not the Description box should be drawn.");
        register(module.showBind, "Shows modules' bindings next to their names in the ClickGUI" +
                " in brackets. For example: [B]");
        register(module.size, "Displays how many modules are in each category.");
        register(module.descriptionWidth, "The width the description box should be." +
                " Recommended, if the box doesn't fit in your screen completely.");
        register(module.descNameValue, "When enabled, description boxes will show the type of setting," +
                " e.g. Boolean and its value, e.g. true/false. This isn't necessary, but" +
                " looks pretty cool.");
    }
    public String getDescription() {
        return "Beautiful ClickGui by oHare.";
    }
}
