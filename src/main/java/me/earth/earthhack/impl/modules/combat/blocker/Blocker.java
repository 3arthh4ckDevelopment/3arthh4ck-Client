package me.earth.earthhack.impl.modules.combat.blocker;

import me.earth.earthhack.api.cache.ModuleCache;
import me.earth.earthhack.api.module.util.Category;
import me.earth.earthhack.api.setting.Setting;
import me.earth.earthhack.api.setting.settings.BooleanSetting;
import me.earth.earthhack.api.setting.settings.EnumSetting;
import me.earth.earthhack.api.setting.settings.NumberSetting;
import me.earth.earthhack.impl.gui.visibility.PageBuilder;
import me.earth.earthhack.impl.gui.visibility.Visibilities;
import me.earth.earthhack.impl.modules.Caches;
import me.earth.earthhack.impl.modules.combat.blocker.util.BlockerDetection;
import me.earth.earthhack.impl.modules.combat.blocker.util.BlockerPages;
import me.earth.earthhack.impl.modules.player.speedmine.Speedmine;
import me.earth.earthhack.impl.util.helpers.blocks.ObbyListenerModule;
import me.earth.earthhack.impl.util.minecraft.PlayerUtil;
import me.earth.earthhack.impl.util.minecraft.entity.EntityUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

// A rewrite of the old blocker

public class Blocker extends ObbyListenerModule<ListenerObsidian> {

    protected final Setting<BlockerPages> pages =
            register(new EnumSetting<>("Page", BlockerPages.Place));

    /* general */
    protected final Setting<Boolean> holeCheck =
            register(new BooleanSetting("OnlyHole", true));
    protected final Setting<Boolean> onlyTarget =
            register(new BooleanSetting("OnlyTarget", false));
    protected final Setting<Integer> placeDelay =
            register(new NumberSetting<>("PlaceDelay", 200, 0, 2000));

    /* sides */
    protected final Setting<Boolean> sides =
            register(new BooleanSetting("Sides", true));
    protected final Setting<Boolean> face =
            register(new BooleanSetting("Face", true));
    protected final Setting<BlockerDetection> modeSides =
            register(new EnumSetting<>("Sides-Detect", BlockerDetection.Touch));

    /* cev */
    protected final Setting<Boolean> cev =
            register(new BooleanSetting("Cev", true));
    protected final Setting<BlockerDetection> modeCev =
            register(new EnumSetting<>("Cev-Detect", BlockerDetection.Touch));
    protected final Setting<Boolean> stepCev =
            register(new BooleanSetting("Step-Helper", true));
    protected final Setting<Integer> detectTime =
            register(new NumberSetting<>("Touch-Time", 3, 0, 20));



    protected EntityPlayer target;
    protected final ModuleCache<Speedmine> SPEEDMINE = Caches.getModule(Speedmine.class);
    Vec3i[] replaceList = new Vec3i[]{
            new Vec3i(0, 3, 0),
            new Vec3i(1, 0, 0),
            new Vec3i(-1, 0, 0),
            new Vec3i(0, 0, 1),
            new Vec3i(0, 0, -1),
            new Vec3i(1, 1, 0),
            new Vec3i(-1, 1, 0),
            new Vec3i(0, 1, 1),
            new Vec3i(0, 1, -1)};

    public Blocker() {
        super("Blocker", Category.Combat);
        listeners.add(new ListenerBlockBreakAnim(this));
        listeners.add(new ListenerBlockChange(this));
        listeners.add(new ListenerUpdate(this));
        this.setData(new BlockerData(this));

        new PageBuilder<>(this, pages)
                .addPage(p -> p == BlockerPages.Place, holeCheck, placeDelay)
                .addPage(p -> p == BlockerPages.Offsets, sides, detectTime)
                .register(Visibilities.VISIBILITY_MANAGER);
    }

    @Override
    protected boolean shouldHelp(EnumFacing facing, BlockPos pos) {
        return super.shouldHelp(facing, pos);
    }

    protected void scanAndPlace(BlockPos pos, boolean replace) {
        if (mc.world == null || mc.player == null) {
            return;
        }

        BlockPos playerPos = PlayerUtil.getPlayerPos();
        if (pos == SPEEDMINE.get().getPos()) {
            if (stepCev.getValue()
                    && pos == playerPos.add(0, 2, 0)
                    && mc.world.getBlockState(playerPos.add(0,3,0)).isFullBlock()
                    && !mc.world.isAirBlock(playerPos.add(0,3,0)))
                ListenerUpdate.scheduledPlacements.add(pos.add(0,2,0));
            return;
        }

        if (onlyTarget.getValue()) {
            target = EntityUtil.getClosestEnemy();
            if (target == null || pos.getDistance(target.getPosition().getX(), target.getPosition().getY(), target.getPosition().getZ()) >= 6.0f) {
                return;
            }
        }

        if (holeCheck.getValue() && !PlayerUtil.isInHole(mc.player)) {
            return;
        }

        for (Vec3i vec3i : replaceList) {
            if (playerPos.add(vec3i).equals(pos)) break;
            if (!vec3i.equals(replaceList[replaceList.length - 1])) continue;
            return;
        }

        if (replace) {
            ListenerUpdate.scheduledPlacements.add(pos);
        }

        if (cev.getValue()) {
            if (pos == playerPos.add(0, 3, 0)) {
                ListenerUpdate.scheduledPlacements.add(pos.add(0,1,0));
                return;
            }
        }

        for (EnumFacing enumFacing : EnumFacing.values()) {
            if (pos.offset(enumFacing).equals(playerPos)) continue;

            if (mc.world.isAirBlock(pos.offset(EnumFacing.DOWN))) {
                ListenerUpdate.scheduledPlacements.add(pos.offset(EnumFacing.DOWN));
            }

            if (!playerPos.offset(enumFacing).equals(pos)) continue;

            if (face.getValue()) {
                ListenerUpdate.scheduledPlacements.add(playerPos.offset(enumFacing).add(0, 1, 0));
                ListenerUpdate.scheduledPlacements.add(playerPos.offset(enumFacing).offset(enumFacing));
                ListenerUpdate.scheduledPlacements.add(playerPos.offset(enumFacing).offset(enumFacing.rotateYCCW()));
                ListenerUpdate.scheduledPlacements.add(playerPos.offset(enumFacing).offset(enumFacing.rotateY()));
            }
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

}
