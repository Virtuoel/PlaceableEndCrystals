package net.thedragonteam.pec.events;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemEndCrystal;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.thedragonteam.pec.Reference;

import java.util.Arrays;
import java.util.List;

@EventBusSubscriber(value = Side.CLIENT, modid = Reference.MODID)
@SideOnly(Side.CLIENT)
public class ClientEventHandler {
    private static final KeyBinding keyBindSneak = Minecraft.getMinecraft().gameSettings.keyBindSneak;
    private static final KeyBinding keyBindSprint = Minecraft.getMinecraft().gameSettings.keyBindSprint;

    @SubscribeEvent
    public static void onEntityInteraction(ItemTooltipEvent e) {
        if (e.getItemStack().getItem() instanceof ItemEndCrystal) {
            List<String> tooltip = e.getToolTip();

            if (isKeyDown()) {
                addToolTip(tooltip,
                        "Right-Click when placed to toggle names off",
                        "Formats available: <x y z> or x:<> y:<> z:<>",
                        "~ also works",
                        "Ender Crystals will not explode if debugMode is activated"
                );
            } else {
                addToolTip(tooltip, "Press " + keyBindSneak.getDisplayName() + " for more information");
                addToolTip(tooltip,
                        "Press " + keyBindSneak.getDisplayName() + " + " + keyBindSprint.getDisplayName() + " to get a detailed description"
                );
            }
        }
    }

    private static void addToolTip(List<String> tooltip, String line, ClickEvent event) {
        TextComponentString text = new TextComponentString(line);
        text.getStyle().setClickEvent(event);
        tooltip.add(text.getFormattedText());
    }

    private static ITextComponent addEvent(String line, ClickEvent event) {
        TextComponentString text = new TextComponentString(line);
        text.getStyle().setClickEvent(event);
        return text;
    }

    private static void addToolTip(List<String> tooltip, String line) {
        addToolTip(tooltip, line, null);
    }

    private static void addToolTip(List<String> tooltip, String... lines) {
        Arrays.stream(lines).forEachOrdered(line -> addToolTip(tooltip, line));
    }

    public static boolean isKeyDown() {
        final KeyBinding keyBindSneak = Minecraft.getMinecraft().gameSettings.keyBindSneak;
        return GameSettings.isKeyDown(keyBindSneak);
    }

    public static boolean isKeyDown(KeyBinding keyBinding) {
        return GameSettings.isKeyDown(keyBinding);
    }
}
