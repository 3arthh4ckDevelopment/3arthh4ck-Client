package me.earth.earthhack.impl.managers.client;

import me.earth.earthhack.api.event.bus.instance.Bus;
import me.earth.earthhack.api.hud.HudElement;
import me.earth.earthhack.api.register.IterationRegister;
import me.earth.earthhack.api.register.Registrable;
import me.earth.earthhack.api.register.exception.CantUnregisterException;
import me.earth.earthhack.impl.Earthhack;
import me.earth.earthhack.impl.hud.armor.Armor;
import me.earth.earthhack.impl.hud.arraylist.ArrayList;
import me.earth.earthhack.impl.hud.binds.Binds;
import me.earth.earthhack.impl.hud.clock.Clock;
import me.earth.earthhack.impl.hud.compass.Compass;
import me.earth.earthhack.impl.hud.coordinates.Coordinates;
import me.earth.earthhack.impl.hud.direction.Direction;
import me.earth.earthhack.impl.hud.fps.FPS;
import me.earth.earthhack.impl.hud.greeter.Greeter;
import me.earth.earthhack.impl.hud.inventory.Inventory;
import me.earth.earthhack.impl.hud.model.Model;
import me.earth.earthhack.impl.hud.ping.Ping;
import me.earth.earthhack.impl.hud.pops.Pops;
import me.earth.earthhack.impl.hud.potions.Potions;
import me.earth.earthhack.impl.hud.pvpresources.PvpResources;
import me.earth.earthhack.impl.hud.safetyhud.SafetyHud;
import me.earth.earthhack.impl.hud.serverbrand.ServerBrand;
import me.earth.earthhack.impl.hud.session.Session;
import me.earth.earthhack.impl.hud.skeetline.SkeetLine;
import me.earth.earthhack.impl.hud.speed.HudSpeed;
import me.earth.earthhack.impl.hud.targethud.TargetHud;
import me.earth.earthhack.impl.hud.textradar.TextRadar;
import me.earth.earthhack.impl.hud.time.Time;
import me.earth.earthhack.impl.hud.totem.Totem;
import me.earth.earthhack.impl.hud.tps.Tps;
import me.earth.earthhack.impl.hud.watermark.Watermark;

import java.util.Comparator;

public class HudElementManager extends IterationRegister<HudElement> {

    private int currentZ = 0;

    public void init()
    {
        Earthhack.getLogger().info("Initializing HUD elements...");
        this.forceRegister(new Watermark());
        this.forceRegister(new Greeter());
        this.forceRegister(new Coordinates());
        this.forceRegister(new Ping());
        this.forceRegister(new Tps());
        this.forceRegister(new FPS());
        this.forceRegister(new ServerBrand());
        this.forceRegister(new Direction());
        this.forceRegister(new Totem());
        this.forceRegister(new Model());
        this.forceRegister(new Armor());
        this.forceRegister(new Time());
        this.forceRegister(new Potions());
        this.forceRegister(new TargetHud());
        this.forceRegister(new PvpResources());
        this.forceRegister(new Pops());
        this.forceRegister(new TextRadar());
        this.forceRegister(new ArrayList());
        this.forceRegister(new Clock());
        this.forceRegister(new Session());
        this.forceRegister(new Compass());
        this.forceRegister(new Binds());
        this.forceRegister(new SkeetLine());
        this.forceRegister(new SafetyHud());
        this.forceRegister(new Inventory());
        this.forceRegister(new HudSpeed());

        // this.forceRegister(new ImageRender());
    }

    public void load()
    {
        for (HudElement element : getRegistered())
        {
            Earthhack.getLogger().info(element.getName());
            element.load();
        }
        registered.sort(Comparator.comparing(HudElement::getZ));
    }

    @Override
    public void unregister(HudElement element) throws CantUnregisterException
    {
        super.unregister(element);
        Bus.EVENT_BUS.unsubscribe(element);
    }

    private void forceRegister(HudElement element)
    {
        registered.add(element);
        if (element instanceof Registrable)
        {
            ((Registrable) element).onRegister();
        }
        element.setZ(currentZ);
        currentZ++;
    }

}
