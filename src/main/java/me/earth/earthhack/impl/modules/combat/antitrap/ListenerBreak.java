package me.earth.earthhack.impl.modules.combat.antitrap;

import me.earth.earthhack.impl.event.events.network.PacketEvent;
import me.earth.earthhack.impl.event.listeners.ModuleListener;
import me.earth.earthhack.impl.modules.combat.antitrap.util.AntiTrapMode;
import me.earth.earthhack.impl.util.math.position.PositionUtil;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.server.SPacketBlockBreakAnim;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

public class ListenerBreak extends ModuleListener<AntiTrap, PacketEvent.Receive<SPacketBlockBreakAnim>> {
    public ListenerBreak(AntiTrap module){
        super(module, PacketEvent.Receive.class);
    }


    Vec3i[] breakable = new Vec3i[]{

            new Vec3i(1,0,0),
            new Vec3i(-1,0,0),
            new Vec3i(0,0,1),
            new Vec3i(0,0,-1)
    };


    public void invoke(PacketEvent.Receive<SPacketBlockBreakAnim> e)
    {
        BlockPos pos = e.getPacket().getPosition();
        BlockPos player = PositionUtil.getPosition(mc.player);

        if(mc.world.getBlockState(pos).getBlock() == Blocks.BEDROCK)
            return;
        if(pos.getDistance((int) mc.player.posX, (int) mc.player.posY, (int) mc.player.posZ) > 10)
            return;
        if(mc.player == null || mc.world == null)
            return;
        if(!module.smart.getValue())
            return;


        switch(module.mode.getValue())
        {
            case Bomb:
                if(pos.equals(player.add(0,2,0)))
                {
                    module.positions.add(new BlockPos(player.add(0,3,0))); // This could be better maybe?
                }
                else
                    return;
            break;
            case FacePlace:
                for(Vec3i positions : breakable)
                {
                    if(pos.equals(positions)) // good code? probably not... :(
                    {
                        for(Vec3i placement : AntiTrapMode.FacePlace.getOffsets())
                        {
                            module.positions.add(player.add(placement));
                        }
                    }
                    else
                        return;
                }
            break;
            // modes Crystal and Fill don't need this. Btw this could be smarter still, but I'm too lazy and tired as of now to write it. :(
        }
    }
}
