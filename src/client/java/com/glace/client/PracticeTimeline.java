package com.glace.client;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;

public class PracticeTimeline {
    public static boolean guideMode = false;
    public static boolean loopMode = false;
    public static int difficulty = 1; //1 = easy, 2 = medium, 3 = hard, 4 = insane
    static final int TOTAL_TICKS = 40;
    public static boolean startClicked = false;
    long minecraftTicksSuck = 0;
    float time = 0;
    int axeTick = 0;
    int maceTick = 0;


    public static final Identifier TIMEHAND = Identifier.fromNamespaceAndPath("stun-slam-guider", "textures/gui/timeline/playhead.png");
    public static final Identifier AXE_DOT = Identifier.fromNamespaceAndPath("stun-slam-guider", "textures/gui/timeline/axe_dot.png");
    public static final Identifier MACE_DOT = Identifier.fromNamespaceAndPath("stun-slam-guider", "textures/gui/timeline/mace_dot.png");
    public static final Identifier INSANE_DOT = Identifier.fromNamespaceAndPath("stun-slam-guider", "textures/gui/timeline/insane_dot.png");

    public PracticeTimeline() {

    }

    public void tick() {
        if(loopMode && !startClicked) {
            start();
        }
        if (startClicked) {
            if(time >= TOTAL_TICKS) {
                startClicked = false;
            }
        }
    }

    public void render(GuiGraphicsExtractor graphics, int guiLeft, int guiTop, PracticeRange practiceRange) {
        if(startClicked) {
            time = (Util.getMillis() - minecraftTicksSuck) / 50f;
            if(time > TOTAL_TICKS) {
                time = TOTAL_TICKS;
            }
        } else {
            time = 0;
        }
        float progress = time / TOTAL_TICKS;
        int offset = Math.round(progress * 138);
        practiceRange.highlight(graphics, guiLeft, guiTop, offset);
        graphics.blit(RenderPipelines.GUI_TEXTURED, TIMEHAND, guiLeft + 11 + offset, guiTop + 185, 0, 0, 1, 13, 1, 13);
    }

    public void start() {
        time = 0;
        minecraftTicksSuck = Util.getMillis();
        startClicked = true;
    }

    public boolean isLocked() {
        return startClicked || loopMode;
    }

    public int dotGap() {
        if(difficulty == 4) {
            return 0;
        } else if(difficulty == 2) {
            return 4;
        } else if (difficulty == 3) {
            return 2;
        } else {
            return 6;
        }
    }

}
