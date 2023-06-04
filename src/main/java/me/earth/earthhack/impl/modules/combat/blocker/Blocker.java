package me.earth.earthhack.impl.modules.combat.blocker;

import me.earth.earthhack.api.cache.ModuleCache;
import me.earth.earthhack.api.module.util.Category;
import me.earth.earthhack.api.setting.Complexity;
import me.earth.earthhack.api.setting.Setting;
import me.earth.earthhack.api.setting.settings.BooleanSetting;
import me.earth.earthhack.api.setting.settings.EnumSetting;
import me.earth.earthhack.api.setting.settings.NumberSetting;
import me.earth.earthhack.impl.modules.Caches;
import me.earth.earthhack.impl.modules.player.speedmine.Speedmine;
import me.earth.earthhack.impl.util.helpers.blocks.ObbyListenerModule;
import me.earth.earthhack.impl.util.minecraft.PlayerUtil;
import me.earth.earthhack.impl.util.minecraft.entity.EntityUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;


// from a 3arthh4ck skid, remapped and cleaned (module originally made by xyzbtw and AquaMinerale2b2t)
public class Blocker extends ObbyListenerModule<ListenerObsidian> {
    protected final Setting<Boolean> antiCev =
            register(new BooleanSetting("AntiCev", false));
    protected final Setting<Boolean> extend =
            register(new BooleanSetting("Extend", true));
    protected final Setting<Boolean> face =
            register(new BooleanSetting("Face", true));
    protected final Setting<Boolean> hole =
            register(new BooleanSetting("HoleCheck", true));
    protected final Setting<Boolean> fullExtend =
            register(new BooleanSetting("FullExtend", true));
    protected final Setting<Boolean> extendxyz =
            register(new BooleanSetting("Extend-diag", false));
    protected final Setting<Float> range =
            register(new NumberSetting<>("Range", 6.0f,0.0f, 10.0f));
    protected final Setting<Integer> surroundDelay =
            register(new NumberSetting<>("SurroundDelay", 500, 0, 3000));
    protected final Setting<DetectMode> mode =
            register(new EnumSetting<>("Detect", DetectMode.Breaked))
                    .setComplexity(Complexity.Expert);
    protected final Setting<DetectMode> modeCev =
            register(new EnumSetting<>("CevDetect", DetectMode.Touched))
                    .setComplexity(Complexity.Expert);


    protected EntityPlayer target;
    protected final ModuleCache<Speedmine> SPEEDMINE = Caches.getModule(Speedmine.class);
    Vec3i[] replaceList = new Vec3i[]{new Vec3i(0, 3, 0), new Vec3i(1, 0, 0), new Vec3i(-1, 0, 0), new Vec3i(0, 0, 1), new Vec3i(0, 0, -1), new Vec3i(1, 1, 0), new Vec3i(-1, 1, 0), new Vec3i(0, 1, 1), new Vec3i(0, 1, -1)};

    public Blocker() {
        super("Blocker", Category.Combat);
        listeners.add(new ListenerBlockBreakAnim(this));
        listeners.add(new ListenerBlockChange(this));
        listeners.add(new ListenerUpdate(this));
        setData(new BlockerData(this));
    }

    @Override
    protected boolean shouldHelp(EnumFacing facing, BlockPos pos) {
        return super.shouldHelp(facing, pos);
    }

    protected void scanAndPlace(BlockPos pos, boolean replace) {
        if (mc.world == null || mc.player == null) {
            return;
        }

        if (pos == SPEEDMINE.get().getPos()) {
            return;
        }

        target = EntityUtil.getClosestEnemy();
        if (target == null || pos.getDistance(target.getPosition().getX(), target.getPosition().getY(), target.getPosition().getZ()) >= (double) range.getValue()) {
            return;
        }
        
        if (hole.getValue() && !PlayerUtil.isInHole(mc.player)) {
            return;
        }
        
        BlockPos playerPos = PlayerUtil.getPlayerPos();
        for (Vec3i vec3i : replaceList) {
            if (playerPos.add(vec3i).equals(pos)) break;
            if (!vec3i.equals(replaceList[replaceList.length - 1])) continue;
            return;
        }

        if (pos == playerPos.add(0, 3, 0)) {
            ListenerUpdate.scheduledPlacements.add(pos);
            return;
        }

        if (replace) {
            ListenerUpdate.scheduledPlacements.add(pos);
        }

        for (EnumFacing enumFacing : EnumFacing.values()) {
            if (pos.offset(enumFacing).equals(playerPos)) continue;

            if (mc.world.isAirBlock(pos.offset(EnumFacing.DOWN))) {
                ListenerUpdate.scheduledPlacements.add(pos.offset(EnumFacing.DOWN));
            }

            if (fullExtend.getValue()) {
                if (pos.getY() == playerPos.getY()) {
                    ListenerUpdate.scheduledPlacements.add(pos.offset(enumFacing));
                    continue;
                }
                ListenerUpdate.scheduledPlacements.add(pos.add(0, 1, 0));
                continue;
            }

            if (!playerPos.offset(enumFacing).equals(pos)) continue;

            if (extend.getValue()) {
                ListenerUpdate.scheduledPlacements.add(playerPos.offset(enumFacing).offset(enumFacing));
            }

            if (face.getValue()) {
                ListenerUpdate.scheduledPlacements.add(playerPos.offset(enumFacing).add(0, 1, 0));
            }

            if (!extendxyz.getValue())
                continue;

            ListenerUpdate.scheduledPlacements.add(playerPos.offset(enumFacing).offset(enumFacing.rotateYCCW()));
            ListenerUpdate.scheduledPlacements.add(playerPos.offset(enumFacing).offset(enumFacing.rotateY()));
        }
    }

    @Override
    public boolean execute() {
        return super.execute();
    }

    @Override
    protected ListenerObsidian createListener() {
        return new ListenerObsidian(this);
    }

    protected enum DetectMode {
        Breaked,
        Touched
    }

}
