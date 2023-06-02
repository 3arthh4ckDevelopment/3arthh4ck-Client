package me.earth.earthhack.impl.modules.movement.clip;

import me.earth.earthhack.api.module.data.DefaultData;

final class ClipData extends DefaultData<Clip>
{
    public ClipData(Clip module)
    {
        super(module);
        register(module.mode, "" +
                "-Center centers you into the middle \n" +
                "of the block your standing on\n" +
                "-Corner Clips you into the corner \n" +
                "of a block to take less crystal damage" +
                "- FiveB 5b5t.org CornerClip");
        register(module.delay, "Delay before clipping into a corner");
        register(module.disable, "Disable the module after clipping");
    }

    @Override
    public int getColor()
    {
        return 0xffffffff;
    }

    @Override
    public String getDescription()
    {
        return "Clips into blocks";
    }

}
