package com.glace.client;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;

public class PracticeButtons {
       //Lyrics comin' at you at supersonic speed (J.J. Fad)
    public static final int EASY_X = 166;
    public static final int EASY_Y = 35;
    public static final int DIFFICULTY_W = 20;
    public static final int DIFFICULTY_H = 20;

    public static final int MEDIUM_X = 196;
    public static final int MEDIUM_Y = 35;

    public static final int HARD_X = 226;
    public static final int HARD_Y = 35;

    public static final int INSANE_X = 256;
    public static final int INSANE_Y = 35;

    public static final int START_X = 165;
    public static final int START_Y = 180;
    public static final int START_W = 90;
    public static final int START_H = 18;

    public static final int GUIDE_X = 174;
    public static final int GUIDE_Y = 62;
    public static final int GUIDE_W = 94;
    public static final int GUIDE_H = 16;

    public static final int LOOP_X = 259;
    public static final int LOOP_Y = 180;
    public static final int LOOP_W = 18;
    public static final int LOOP_H = 18;

    public static final Identifier EASY_BUTTON = Identifier.fromNamespaceAndPath("stun-slam-guider", "textures/gui/buttons/easy_mode.png");
    public static final Identifier EASY_BUTTON_PRESSED = Identifier.fromNamespaceAndPath("stun-slam-guider", "textures/gui/buttons/easy_mode_pressed.png");

    public static final Identifier MEDIUM_BUTTON = Identifier.fromNamespaceAndPath("stun-slam-guider", "textures/gui/buttons/medium_mode.png");
    public static final Identifier MEDIUM_BUTTON_PRESSED = Identifier.fromNamespaceAndPath("stun-slam-guider", "textures/gui/buttons/medium_mode_pressed.png");

    public static final Identifier HARD_BUTTON = Identifier.fromNamespaceAndPath("stun-slam-guider", "textures/gui/buttons/hard_mode.png");
    public static final Identifier HARD_BUTTON_PRESSED = Identifier.fromNamespaceAndPath("stun-slam-guider", "textures/gui/buttons/hard_mode_pressed.png");

    public static final Identifier INSANE_BUTTON = Identifier.fromNamespaceAndPath("stun-slam-guider", "textures/gui/buttons/insane_mode.png");
    public static final Identifier INSANE_BUTTON_PRESSED = Identifier.fromNamespaceAndPath("stun-slam-guider", "textures/gui/buttons/insane_mode_pressed.png");

    public static final Identifier GUIDE_BUTTON = Identifier.fromNamespaceAndPath("stun-slam-guider", "textures/gui/buttons/guide_button.png");
    public static final Identifier GUIDE_BUTTON_PRESSED = Identifier.fromNamespaceAndPath("stun-slam-guider", "textures/gui/buttons/guide_button_pressed.png");

    public static final Identifier LOOP_BUTTON = Identifier.fromNamespaceAndPath("stun-slam-guider", "textures/gui/buttons/loop.png");
    public static final Identifier LOOP_BUTTON_PRESSED = Identifier.fromNamespaceAndPath("stun-slam-guider", "textures/gui/buttons/loop_pressed.png");

    public static final Identifier START_BUTTON = Identifier.fromNamespaceAndPath("stun-slam-guider", "textures/gui/buttons/start_button.png");
    public static final Identifier START_BUTTON_PRESSED = Identifier.fromNamespaceAndPath("stun-slam-guider", "textures/gui/buttons/start_button_pressed.png");
        //I make elevating music, you make elevator music
        //"Oh, he's too mainstream."
        //Well, that's what they do when they get jealous, they confuse it


    public PracticeButtons() {

    }

    static boolean contains(double mouseX, double mouseY, int guiLeft, int guiTop, int buttonX, int buttonY, int buttonWidth, int buttonHeight) {
        return mouseX >= guiLeft + buttonX && mouseX < guiLeft + buttonWidth + buttonX && mouseY >= guiTop + buttonY && mouseY < guiTop + buttonHeight + buttonY;
    }

    static void render(GuiGraphicsExtractor graphics, int guiLeft, int guiTop, int buttonX, int buttonY, int buttonWidth, int buttonHeight, Identifier unpressed, Identifier pressed, boolean isPressed) {
        if(isPressed) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, pressed, guiLeft + buttonX, guiTop + buttonY, 0, 0, buttonWidth, buttonHeight, buttonWidth, buttonHeight);
        } else {
            graphics.blit(RenderPipelines.GUI_TEXTURED, unpressed, guiLeft + buttonX, guiTop + buttonY, 0, 0, buttonWidth, buttonHeight, buttonWidth, buttonHeight);
        }

    }
}
