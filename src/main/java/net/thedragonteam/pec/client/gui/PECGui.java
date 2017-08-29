package net.thedragonteam.pec.client.gui;

import com.google.common.collect.Lists;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IInteractionObject;
import net.thedragonteam.pec.network.EnderCrystalData;
import net.thedragonteam.pec.network.PECPacketHandler;
import net.thedragonteam.thedragonlib.util.LogHelper;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.List;

public class PECGui extends GuiScreen implements IInteractionObject {

    private GuiTextField posXEdit, posYEdit, posZEdit;
    private GuiButton doneButton;
    private GuiButton cancelButton;
    private final List<GuiTextField> tabOrder = Lists.newArrayList();

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen() {
        this.posXEdit.updateCursorCounter();
        this.posYEdit.updateCursorCounter();
        this.posZEdit.updateCursorCounter();
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        this.posXEdit = new GuiTextField(0, this.fontRenderer, this.width / 2 - 152, 80, 80, 20);
        this.posXEdit.setMaxStringLength(15);
        this.posXEdit.setText(Integer.toString(0));
        this.tabOrder.add(this.posXEdit);
        this.posYEdit = new GuiTextField(1, this.fontRenderer, this.width / 2 - 72, 80, 80, 20);
        this.posYEdit.setMaxStringLength(15);
        this.posYEdit.setText(Integer.toString(0));
        this.tabOrder.add(this.posYEdit);
        this.posZEdit = new GuiTextField(2, this.fontRenderer, this.width / 2 + 8, 80, 80, 20);
        this.posZEdit.setMaxStringLength(15);
        this.posZEdit.setText(Integer.toString(0));
        this.tabOrder.add(this.posZEdit);
        this.doneButton = this.addButton(new GuiButton(4, this.width / 2 - 4 - 150, 210, 150, 20, "Done"));
        this.cancelButton = this.addButton(new GuiButton(5, this.width / 2 + 4, 210, 150, 20, "Cancel"));
        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawCenteredString(this.fontRenderer, "Ender Crystal Settings", this.width / 2, 10, 16777215);
        this.drawString(this.fontRenderer, "Beam Position Settings", this.width / 2 - 153, 70, 10526880);
        this.posXEdit.drawTextBox();
        this.posYEdit.drawTextBox();
        this.posZEdit.drawTextBox();
        this.drawString(this.fontRenderer, "Show Name", this.width / 2 - 153, 70, 10526880);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.enabled) {
            if (button.id == 4) {
                this.sendToServer();
                this.mc.player.closeScreen();
            } else if (button.id == 5) {
                this.mc.player.closeScreen();
            }
        }
    }

    private int parseCoordinate(String coord) {
        try {
            return Integer.parseInt(coord);
        } catch (NumberFormatException var3) {
            return 0;
        }
    }

    private void sendToServer() {
        try {
            // PacketBuffer packetbuffer = new PacketBuffer(Unpooled.buffer());
            // packetbuffer.writeDouble(this.parseCoordinate(this.posXEdit.getText()));
            // packetbuffer.writeDouble(this.parseCoordinate(this.posYEdit.getText()));
            // packetbuffer.writeDouble(this.parseCoordinate(this.posZEdit.getText()));
            PECPacketHandler.INSTANCE.sendToServer(new EnderCrystalData(
                    this.parseCoordinate(this.posXEdit.getText()),
                    this.parseCoordinate(this.posYEdit.getText()),
                    this.parseCoordinate(this.posZEdit.getText())
            ));
        } catch (Exception exception) {
            LogHelper.warn("Could not send ender crystal info", (Throwable) exception);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.posXEdit.getVisible()) {
            this.posXEdit.mouseClicked(mouseX, mouseY, mouseButton);
        }
        if (this.posYEdit.getVisible()) {
            this.posYEdit.mouseClicked(mouseX, mouseY, mouseButton);
        }
        if (this.posZEdit.getVisible()) {
            this.posZEdit.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {

        if (this.posXEdit.getVisible()) {
            this.posXEdit.textboxKeyTyped(typedChar, keyCode);
        }

        if (this.posYEdit.getVisible()) {
            this.posYEdit.textboxKeyTyped(typedChar, keyCode);
        }

        if (this.posZEdit.getVisible()) {
            this.posZEdit.textboxKeyTyped(typedChar, keyCode);
        }
        if (keyCode == 15) {
            GuiTextField guitextfield = null;
            GuiTextField guitextfield1 = null;

            for (GuiTextField guitextfield2 : this.tabOrder) {
                if (guitextfield != null && guitextfield2.getVisible()) {
                    guitextfield1 = guitextfield2;
                    break;
                }

                if (guitextfield2.isFocused() && guitextfield2.getVisible()) {
                    guitextfield = guitextfield2;
                }
            }

            if (guitextfield != null && guitextfield1 == null) {
                for (GuiTextField guitextfield3 : this.tabOrder) {
                    if (guitextfield3.getVisible() && guitextfield3 != guitextfield) {
                        guitextfield1 = guitextfield3;
                        break;
                    }
                }
            }

            if (guitextfield1 != null && guitextfield1 != guitextfield) {
                guitextfield.setFocused(false);
                guitextfield1.setFocused(true);
            }
        }

        if (keyCode != 28 && keyCode != 156) {
            if (keyCode == 1) {
                this.actionPerformed(this.cancelButton);
            }
        } else {
            this.actionPerformed(this.doneButton);
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
        return null;
    }

    @Override
    public String getGuiID() {
        return "pec:gui";
    }

    @Override
    public String getName() {
        return "PEC Gui";
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public ITextComponent getDisplayName() {
        return null;
    }
}
