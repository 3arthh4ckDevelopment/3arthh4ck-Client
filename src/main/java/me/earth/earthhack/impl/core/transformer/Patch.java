package me.earth.earthhack.impl.core.transformer;

import org.objectweb.asm.tree.ClassNode;

public interface Patch
{
    String getName();

    String getTransformedName();

    void apply(ClassNode node);

    boolean isFinished();

}