package me.earth.earthhack.impl.gui.click.frame.impl;

import me.earth.earthhack.api.module.Module;
import me.earth.earthhack.api.module.util.Category;
import me.earth.earthhack.impl.gui.click.component.impl.ModuleComponent;
import me.earth.earthhack.impl.managers.client.ModuleManager;

import java.util.Comparator;
import java.util.List;

import static java.util.stream.Collectors.toSet;

public class CategoryFrame extends ModulesFrame {
    private final Category moduleCategory;
    private final ModuleManager moduleManager;

    public CategoryFrame(Category moduleCategory, ModuleManager moduleManager, float posX, float posY, float width, float height) {
        super(moduleCategory.name(), posX, posY, width, height);
        this.moduleCategory = moduleCategory;
        this.moduleManager = moduleManager;
        this.setExtended(true);
    }

    @Override
    public void init() {
        getComponents().clear();
        float offsetY = getHeight() + 1;
        List<Module> moduleList = moduleManager.getModulesFromCategory(getModuleCategory());
        List<Module> hiddenModuleList = moduleManager.getHiddenModules();

        moduleList.removeAll(hiddenModuleList.stream()
                    .map(Module::getName)
                        .collect(toSet())); // This doesn't currently work, since it is called once, and this should change.
        // ^ Checking if our moduleList contains any hidden modules and removing them, so they don't show up in the ClickGUI
        moduleList.sort(Comparator.comparing(Module::getName));
        for (Module module : moduleList) {
            getComponents().add(new ModuleComponent(module, getPosX(), getPosY(), 0, offsetY, getWidth(), 14));
            offsetY += 14;
        }
        super.init();
    }

    public Category getModuleCategory() {
        return moduleCategory;
    }
}