/*
 * Copyright (c) TheDragonTeam 2016-2017.
 */

package net.thedragonteam.pec.client.gui.config;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.thedragonteam.pec.Reference;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static net.thedragonteam.pec.PEC.configuration;

public class ConfigGui extends GuiConfig {

    public ConfigGui(GuiScreen parentScreen) {
        super(parentScreen, getConfigElements(parentScreen), Reference.MODID, false, true,
                new TextComponentTranslation("gui." + Reference.MODID + ".config.title").getFormattedText());
    }

    private static List<IConfigElement> getConfigElements(GuiScreen parent) {
        /* adds sections declared in ConfigHandler. toLowerCase() is used because the configuration class automatically does this, so must we. */
        return configuration.getCategoryNames().stream().map(name -> new ConfigElement(configuration.getCategory(name.toLowerCase(Locale.ENGLISH)))).collect(Collectors.toList());
    }
}