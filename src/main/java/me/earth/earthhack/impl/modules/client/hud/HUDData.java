package me.earth.earthhack.impl.modules.client.hud;

import me.earth.earthhack.api.module.data.DefaultData;

public class HUDData extends DefaultData<HUD>
{
    public HUDData(HUD hud)
    {
        super(hud);
        register(hud.colorMode, "HUD color modes.");
        register(hud.logo, "Watermark.");
        register(hud.coordinates, "Shows your current coords");
        register(hud.armor, "Shows the armor.");
        register(hud.durability, "Shows armor durability.");
        register(hud.totems, "Shows totem count.");
        register(hud.shadow, "Text shadow.");
        register(hud.ping, "Shows your ping.");
        register(hud.speed, "Shows your speed.");
        register(hud.renderModules, "Arraylist disposition");
        register(hud.fps, "Shows your fps.");
        register(hud.tps, "Shows the server tps.");
        register(hud.currentTps, "Shows the server current tps.");
        register(hud.animations, "Animate the arraylist");
        register(hud.potions, "Potions position");
        register(hud.potionColor, "Potions color");
        register(hud.serverBrand, "Shows the server brand");
        register(hud.model, "Render your 3D model on screen");



        register(hud.timeFormat, "All letters 'A' to 'Z' and 'a' to 'z' are reserved as pattern letters.\n\n" +
            "    Symbol  Meaning                    \n" +
            "    ------  -------                    \n" +
            "     G       era                       \n" +
            "     u       year                      \n" +
            "     y       year-of-era               \n" +
            "     D       day-of-year               \n" +
            "     M/L     month-of-year             \n" +
            "     d       day-of-month              \n" +
            "  \n" +
            "     Q/q     quarter-of-year           \n" +
            "     Y       week-based-year           \n" +
            "     w       week-of-week-based-year   \n" +
            "     W       week-of-month             \n" +
            "     E       day-of-week               \n" +
            "     e/c     localized day-of-week     \n" +
            "     F       week-of-month             \n" +
            "  \n" +
            "     a       am-pm-of-day              \n" +
            "     h       clock-hour-of-am-pm (1-12)\n" +
            "     K       hour-of-am-pm (0-11)      \n" +
            "     k       clock-hour-of-am-pm (1-24)\n" +
            "  \n" +
            "     H       hour-of-day (0-23)         \n" +
            "     m       minute-of-hour             \n" +
            "     s       second-of-minute           \n" +
            "     S       fraction-of-second         \n" +
            "     A       milli-of-day               \n" +
            "     n       nano-of-second             \n" +
            "     N       nano-of-day                \n" +
            "  \n" +
            "     V       time-zone ID               \n" +
            "     z       time-zone name             \n" +
            "     O       localized zone-offset      \n" +
            "     X       zone-offset 'Z' for zero   \n" +
            "     x       zone-offset                \n" +
            "     Z       zone-offset                \n" +
            "  \n" +
            "     p       pad next                   \n" +
            "  \n" +
            "     '       escape for text            \n" +
            "     ''      single quote               \n" +
            "     [       optional section start\n" +
            "     ]       optional section end\n" +
            "     #       reserved for future use\n" +
            "     {       reserved for future use\n" +
            "     }       reserved for future use");
        register(hud.image, "Render an image");
        register(hud.imageName, "Choose the image name");
        register(hud.imageScale, "The image scale");
        register(hud.imageX, "The image X");
        register(hud.imageY, "The image X");
        register(hud.reloadImages,
                "Reloads all the images. To add\n" +
                        "an image do +folder and go to the\n" +
                        "images folder, then click reload");
    }

    @Override
    public int getColor()
    {
        return 0xffffffff;
    }

    @Override
    public String getDescription()
    {
        return "Displays info like ping, tps or toggled modules on your screen.";
    }

}
