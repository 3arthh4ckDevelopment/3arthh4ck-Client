package me.earth.earthhack.impl.modules.client.rpc;

import me.earth.earthhack.api.module.data.DefaultData;

final class RPCData extends DefaultData<RPC>
{
    public RPCData(RPC module)
    {
        super(module);
        this.descriptions.put(module.logoBig,
                "The large image.");
        this.descriptions.put(module.logoSmall,
                "The small image.");
        this.descriptions.put(module.Line1,
                "Text in the first line.");
        this.descriptions.put(module.Line2,
                "Text in the second line.");
        this.descriptions.put(module.showIP,
                "Shows the server ip you're playing on.");
        this.descriptions.put(module.join,
                "Discord join button.");
        this.descriptions.put(module.partyMax,
                "The party size.");
        this.descriptions.put(module.custom,
                "Your application ID.");
        this.descriptions.put(module.assetLarge,
                "The name of the large image.");
        this.descriptions.put(module.assetLargeText,
                "The displayed text of the large image.");
        this.descriptions.put(module.smallImage,
                "Select if you want the small image or no.");
        this.descriptions.put(module.assetSmall,
                "The name of the small image.");
        this.descriptions.put(module.assetSmallText,
                "The displayed text of the small image.");

        this.descriptions.put(module.custom, "Your application ID.\n" +
                "---------------------------------------\n" +
                "How to setup this:                     \n" +
                "1) Go to the Discord Developer Portal: \n" +
                " discord.com/developers/applications   \n" +
                "\n" +
                "2) Create a New Application:           \n" +
                " The name of the application will be   \n" +
                " displayed on top of the image         \n" +
                "\n" +
                "3) Under the application name, copy the\n" +
                " Application ID, paste it here (CustomId)\n" +
                "\n" +
                "4) Now go to Rich Presence>Art Assets: \n" +
                " [Cover Image--> It's the invite image]\n" +
                "\n" +
                "5) Click on Add Image, copy the name and\n" +
                " paste it in the LargeImage/SmallImage \n" +
                " setting.                              \n" +
                "- - - - - - - - - - - - - - - - - - - -\n" +
                "NOTES:                                 \n" +
                "-To turn on the custom mode, LargeLogo \n" +
                " and SmallLogo settings must be set to \n" +
                " custom mode. (at the same time!)      \n" +
                "\n" +
                "-The LargeImageText and SmallImageText \n" +
                " is the text of the corresponding image\n" +
                " when hovered.                         \n" +
                "\n" +
                "-If the SmallImageSetting is off the rpc\n" +
                " will only use the LargeImage.          \n" +
                "\n" +
                "-You might need to restart your client  \n" +
                " to make the custom RPC work.\n");
    }

    @Override
    public int getColor()
    {
        return 0x428df5ff;
    }

    @Override
    public String getDescription()
    {
        return "Discord RPC (expert mode for more settings)";
    }

}
