package me.earth.earthhack.api.cache;

import me.earth.earthhack.api.hud.HudElement;
import me.earth.earthhack.api.hud.data.HudData;
import me.earth.earthhack.api.register.Register;

import java.util.function.Supplier;

/**
 * Cache/Proxy for a {@link HudElement}.
 * <p>
 * The main idea behind this Cache is to call
 * {@link Register#getByClass(Class)} the least
 * amount of times possible.
 *
 * @param <T> the type of hud module being cached.
 */
@SuppressWarnings("unused")
public class HudCache<T extends HudElement> extends Cache<T>
{
    protected Class<T> type;

    /** Private Ctr. */
    private HudCache() { }

    /**
     * Constructs a HudCache whose supplier calls
     * {@link Register#getByClass(Class)} for the
     * given type. The given ModuleManager is Nullable.
     *
     * @param moduleManager the moduleManager to get the HudElement from.
     * @param type the type of the HudElement.
     */
    public HudCache(Register<HudElement> moduleManager, Class<T> type)
    {
        this(() ->
        {
            if (moduleManager != null && type != null)
            {
                return moduleManager.getByClass(type);
            }

            return null;
        }, type);
    }

    public HudCache(Supplier<T> getter, Class<T> type)
    {
        super(getter);
        this.type = type;
    }

    /**
     * Sets the ModuleManager this Cache gets its value from.
     * Note that the supplier will then call {@link Register#getByClass(Class)}.
     *
     * @param moduleManager the moduleManager.
     */
    public void setModuleManager(Register<HudElement> moduleManager)
    {
        this.getter = () ->
        {
            if (moduleManager != null && type != null)
            {
                return moduleManager.getByClass(type);
            }

            return null;
        };
    }

    /**
     * Calls {@link HudElement#enable()}, if it's present.
     *
     * @return <tt>true</tt> if present.
     */
    public boolean enable()
    {
        return computeIfPresent(HudElement::enable);
    }

    /**
     * Calls {@link HudElement#disable()}, if it's present.
     *
     * @return <tt>true</tt> if present.
     */
    public boolean disable()
    {
        return computeIfPresent(HudElement::disable);
    }

    /**
     * Calls {@link HudElement#toggle()}, if it's present.
     *
     * @return <tt>true</tt> if present.
     */
    public boolean toggle()
    {
        return computeIfPresent(HudElement::toggle);
    }

    /**
     * Returns <tt>true</tt>, only if the module
     * isPresent and {@link HudElement#isEnabled()}.
     *
     * @return <tt>false</tt> if not present or disabled.
     */
    public boolean isEnabled()
    {
        if (isPresent())
        {
            return get().isEnabled();
        }

        return false;
    }


    /**
     * @return {@link HudElement#getData()}, if present.
     */
    public HudData<?> getData()
    {
        if (isPresent())
        {
            return get().getData();
        }

        return null;
    }

    /**
     * Calls {@link HudElement#setData(HudData)} (ModuleData)}, if present.
     *
     * @return <tt>true</tt>, if present.
     */
    public boolean setData(HudData<?> data)
    {
        if (isPresent())
        {
            get().setData(data);
            return true;
        }

        return false;
    }

    /**
     * Constructs a HudCache whose Supplier
     * calls {@link Register#getObject(String)}.
     *
     * @param name the name for the HudElement.
     * @param manager the Register to get the HudElement from.
     * @return a HudCache for Modules of the given Name.
     */
    public static HudCache<HudElement> forName(String name,
                                           Register<HudElement> manager)
    {
        NameCache cache = new NameCache(name);
        cache.setModuleManager(manager);
        return cache;
    }

    private static final class NameCache extends HudCache<HudElement>
    {
        private final String name;

        public NameCache(String name)
        {
            this.name = name;
            this.type = HudElement.class;
        }

        @Override
        public void setModuleManager(Register<HudElement> moduleManager)
        {
            this.getter = () ->
            {
                if (moduleManager != null)
                {
                    return moduleManager.getObject(name);
                }

                return null;
            };
        }
    }

}
