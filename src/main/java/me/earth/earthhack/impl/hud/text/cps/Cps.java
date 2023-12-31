package me.earth.earthhack.impl.hud.text.cps;

import me.earth.earthhack.api.hud.HudCategory;
import me.earth.earthhack.api.hud.HudElement;
import me.earth.earthhack.api.setting.Setting;
import me.earth.earthhack.api.setting.settings.EnumSetting;
import me.earth.earthhack.api.setting.settings.StringSetting;
import me.earth.earthhack.impl.core.ducks.network.ICPacketUseEntity;
import me.earth.earthhack.impl.event.listeners.PostSendListener;
import me.earth.earthhack.impl.event.listeners.ReceiveListener;
import me.earth.earthhack.impl.managers.Managers;
import me.earth.earthhack.impl.util.client.SimpleHudData;
import me.earth.earthhack.impl.util.render.hud.HudRenderUtil;
import me.earth.earthhack.impl.util.text.TextColor;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Cps extends HudElement {

    //TODO: make everything simpler???

    private final Setting<String> name =
            register(new StringSetting("CustomName", "Cps "));
    private final Setting<CountMode> mode =
            register(new EnumSetting<>("Mode", CountMode.Place));

    private final List<BlockPos> attack = new ArrayList<>();
    private final Map<BlockPos, Long> place = new ConcurrentHashMap<>();
    private final Map<Integer, BlockPos> ids = new ConcurrentHashMap<>();
    private final List<Integer> time = new ArrayList<>();

    private void render() {
        if (mc.player != null && mc.world != null) {
            int currentTime = (int) System.currentTimeMillis();
            time.removeIf(i -> currentTime - i > 1000);
        }

        GlStateManager.pushMatrix();
        HudRenderUtil.renderText(name.getValue() + TextColor.GRAY + time.size(), getX(), getY());
        GlStateManager.popMatrix();
    }

    public Cps() {
        super("Cps", HudCategory.Text, 130, 30);
        this.setData(new SimpleHudData(this, "Displays how many crystals are placed/destroyed every second."));
        this.mode.addObserver(e -> {
            attack.clear();
            place.clear();
            ids.clear();
            time.clear();
        });

        this.listeners.add(new ReceiveListener<>(SPacketSoundEffect.class, e ->
        {
            if (mode.getValue() == CountMode.Break) {
                SPacketSoundEffect p = e.getPacket();
                if (p.getCategory() == SoundCategory.BLOCKS
                        && p.getSound() == SoundEvents.ENTITY_GENERIC_EXPLODE) {
                    BlockPos pos = new BlockPos(p.getX(), p.getY() - 1, p.getZ());
                    if (attack.contains(pos)) {
                        attack.remove(pos);
                        time.add((int) System.currentTimeMillis());
                    }
                }
            }
        }));

        this.listeners.add(new PostSendListener<>(CPacketUseEntity.class, e ->
        {
            if (mode.getValue() == CountMode.Break) {
                int entityId = ((ICPacketUseEntity) e.getPacket()).getEntityID();
                Entity entity = mc.world.getEntityByID(entityId);
                BlockPos pos;
                if (entity == null) {
                    pos = ids.get(entityId);
                } else {
                    pos = entity.getPosition().down();
                }

                if (pos != null) {
                    attack.add(pos);
                }
            }
        }));

        this.listeners.add(new ReceiveListener<>(SPacketSpawnObject.class, e ->
        {
            if (e.getPacket().getType() == 51) {
                BlockPos pos = new BlockPos(e.getPacket().getX(),
                        e.getPacket().getY() - 1,
                        e.getPacket().getZ());

                if (mode.getValue() == CountMode.Place) {
                    Long l = place.remove(pos);
                    if (l != null) {
                        time.add((int) System.currentTimeMillis());
                    }
                } else
                    ids.put(e.getPacket().getEntityID(), pos);
            }
        }));

        this.listeners.add(new PostSendListener<>(
                CPacketPlayerTryUseItemOnBlock.class, e ->
        {
            if (mode.getValue() == CountMode.Place) {
                if (mc.player.getHeldItem(e.getPacket().getHand()).getItem()
                        == Items.END_CRYSTAL
                        && !place.containsKey(e.getPacket().getPos())) {
                    place.put(e.getPacket().getPos(), System.currentTimeMillis());
                }
            }
        }));
    }

    @Override
    public void guiDraw(int mouseX, int mouseY, float partialTicks) {
        super.guiDraw(mouseX, mouseY, partialTicks);
        render();
    }

    @Override
    public void hudDraw(float partialTicks) {
        render();
    }

    @Override
    public void guiUpdate(int mouseX, int mouseY, float partialTicks) {
        super.guiUpdate(mouseX, mouseY, partialTicks);
        setWidth(getWidth());
        setHeight(Managers.TEXT.getStringHeight());
    }

    @Override
    public void hudUpdate(float partialTicks) {
        super.hudUpdate(partialTicks);
        setWidth(getWidth());
        setHeight(getHeight());
    }

    @Override
    public float getWidth() {
        return Managers.TEXT.getStringWidth(name.getValue() + "00");
    }

    @Override
    public float getHeight() {
        return Managers.TEXT.getStringHeight();
    }

    private enum CountMode {
        Place,
        Break
    }

}
