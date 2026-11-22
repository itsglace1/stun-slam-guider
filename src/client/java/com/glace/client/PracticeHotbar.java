package com.glace.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;

import static com.glace.client.StunSlamConfig.HANDLER;

public class PracticeHotbar {
    private static final int SLOT_X = 14;
    private static final int SLOT_Y = 157;
    private static final int PITCH = 15;
    private static final int SIZE = 13;
    private final Identifier[] slots = new Identifier[9];
    private static final Identifier MACE = Identifier.fromNamespaceAndPath("stun-slam-guider", "textures/gui/mace.png");
    private static final Identifier AXE = Identifier.fromNamespaceAndPath("stun-slam-guider", "textures/gui/axe.png");
    private static final Identifier SELECTOR = Identifier.fromNamespaceAndPath("stun-slam-guider", "textures/gui/hotbar_selector.png");
    private int selected;
    private static final SoundEvent SCROLL = SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath("stun-slam-guider", "hotbar_scroll"));
    private int picked = -1;

    public PracticeHotbar() {
        StunSlamConfig.nukeTheLimitBreakingPlayersHouses();
        int axeSlot = HANDLER.instance().axeSlot - 1;
        int maceSlot = HANDLER.instance().maceSlot - 1;
        slots[maceSlot] = MACE;
        slots[axeSlot] = AXE;
    }

    public void render(GuiGraphicsExtractor graphics, int guiLeft, int guiTop, final int mouseX, final int mouseY) {
        int hoveredSlot = slotAt(mouseX, mouseY, guiLeft, guiTop);
        for (int i = 0; i < 9; i++) {
            if(hoveredSlot == i) {
                graphics.fill(guiLeft + SLOT_X + i * PITCH, guiTop + SLOT_Y, (guiLeft + SLOT_X + i * PITCH) + SIZE, (guiTop + SLOT_Y) + SIZE, 0x80FFFFFF);
            }
            if (slots[i] != null && i != picked) {
                graphics.blit(RenderPipelines.GUI_TEXTURED, slots[i], guiLeft + SLOT_X + i * PITCH, guiTop + SLOT_Y, 0, 0, SIZE, SIZE, SIZE, SIZE);
            }
        }
        graphics.blit(RenderPipelines.GUI_TEXTURED, SELECTOR, guiLeft + SLOT_X + selected * PITCH - 3, guiTop + SLOT_Y - 3, 0, 0, SIZE + 2 * 3, SIZE + 2 * 3, SIZE + 2 * 3, SIZE + 2 * 3);
        if(picked != -1) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, slots[picked], mouseX - SIZE / 2, mouseY - SIZE / 2, 0, 0, SIZE, SIZE, SIZE, SIZE);
        }
    }

    public void select(int slot) {
        if (slot >= 0 && slot <= 8) {
            setSelected(slot);
        }
    }

    private void setSelected(int slot) {
        if (slot != selected) {
            selected = slot;
            if(HANDLER.instance().playScrollSound) {
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SCROLL, 1.0f, 0.1f));
            }
        }
    }

    public void scroll(double amount) {
        int direction = (int) Math.signum(amount);
        if (direction == 0) {
            return;
        }
        setSelected(Math.floorMod(selected - direction, 9));
    }

    private int slotAt(double mouseX, double mouseY, int guiLeft, int guiTop) {
        int relX = ((int) mouseX - (guiLeft -1 + SLOT_X));
        int relY = ((int) mouseY - (guiTop -1 + SLOT_Y));

        if (relX < 0 || relY < 0 || relY >= PITCH || relX / PITCH > 8) {
            return -1;
        }
        return relX / PITCH;
    }

    private void swap(int a, int b) {
        Identifier tempSlot = slots[a];
        slots[a] = slots[b];
        slots[b] = tempSlot;
    }

    public boolean click(double mouseX, double mouseY, int guiLeft, int guiTop) {
        int slot = slotAt(mouseX, mouseY, guiLeft, guiTop);
        if (slot == -1) {
            picked = -1;
            return false;
        }
        if (picked == -1 && slots[slot] != null) {
            picked = slot;

        } else if (picked != -1) {
            swap(picked, slot);
            for(int i = 0; i < 9; i++) {
                if(slots[i] == MACE) {
                    HANDLER.instance().maceSlot = i + 1;
                }
                if(slots[i] == AXE) {
                    HANDLER.instance().axeSlot = i + 1;
                }
            }
            HANDLER.save();
            picked = -1;
        }

        return true;
    }


}
