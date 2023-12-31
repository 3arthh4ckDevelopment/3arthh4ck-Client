package me.earth.earthhack.impl.core.transformer;

public interface PatchManager
{
    void addPatch(Patch patch);

    byte[] transform(String name, String transformedName, byte[] bytes);

}
