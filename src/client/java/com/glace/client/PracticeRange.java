package com.glace.client;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;

import static java.lang.Math.round;

public class PracticeRange {
    static float rangeState = 0.5f;
    final float MAX_RANGE = 10;
    private static final Identifier SLIDER_HANDLE = Identifier.fromNamespaceAndPath("stun-slam-guider", "textures/gui/buttons/slider_handle.png");
    private static final Identifier SLIDER_BOX_FULL = Identifier.fromNamespaceAndPath("stun-slam-guider", "textures/gui/buttons/slider_box_full.png");
    private static final Identifier SLIDER_BOX_EMPTY = Identifier.fromNamespaceAndPath("stun-slam-guider", "textures/gui/buttons/slider_box_empty.png");
    boolean dragging = false;

    public PracticeRange() {

    }

    public void render(GuiGraphicsExtractor graphics, int guiLeft, int guiTop, final int mouseX, final int mouseY) {
        int offset = Math.round(rangeState * 103);
        graphics.blit(RenderPipelines.GUI_TEXTURED, SLIDER_BOX_EMPTY, guiLeft + 166, guiTop + 86, 0, 0, 110, 8, 110, 8);
        graphics.blit(RenderPipelines.GUI_TEXTURED, SLIDER_BOX_FULL, guiLeft + 166, guiTop + 86, 0, 0, offset + 3, 8, 110, 8);
        graphics.blit(RenderPipelines.GUI_TEXTURED, SLIDER_HANDLE, (guiLeft + 166) + offset , guiTop + 83, 0, 0, 7, 14, 7, 14);
    }

    public int rangeInTicks() {
        return round(rangeState * MAX_RANGE);
    }

    private void mouseToValue(int guiLeft, int mouseX) {
        rangeState = (mouseX - (guiLeft + 166) - 3) / 103f;
        if(rangeState < 0) {
            rangeState = 0;
        }
        if(rangeState > 1) {
            rangeState = 1;
        }
    }

    public boolean click(double mouseX, double mouseY, int guiLeft, int guiTop) {
        dragging = PracticeButtons.contains(mouseX, mouseY, guiLeft, guiTop, 166, 83, 110, 14);
        if(dragging) {
            mouseToValue(guiLeft, (int)mouseX);
        }
        return dragging;
    }

    public void drag(int guiLeft, int mouseX) {
        if(dragging) {
            mouseToValue(guiLeft, mouseX);
        }
    }

    public void release() {
        dragging = false;
    }

    public void highlight(GuiGraphicsExtractor graphics, int guiLeft, int guiTop, int offset) {
        int highlightCap = guiLeft + 12 + offset + Math.round(138f * rangeInTicks() / PracticeTimeline.TOTAL_TICKS);
        if(highlightCap > guiLeft + 150) {
            highlightCap = guiLeft + 150;
        }
        int backwards = Math.round(138f * backwardsTick() / PracticeTimeline.TOTAL_TICKS);
        int backwardsCap = (guiLeft + 11 + offset) - backwards;
        if (backwardsCap < guiLeft + 11) {
            backwardsCap = guiLeft + 11;
        }
        graphics.fill(guiLeft + 12 + offset, guiTop + 190, highlightCap, guiTop + 193, 0x20FFFFFF);
        graphics.fill( backwardsCap, guiTop + 190, guiLeft + 11 + offset, guiTop + 193, 0x20FFFFFF);
    }

    public int backwardsTick() {
        return Math.round(rangeInTicks() / 2f);
    }
}
