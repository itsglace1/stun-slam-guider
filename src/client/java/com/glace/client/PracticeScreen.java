package com.glace.client;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class PracticeScreen extends Screen {
    private static final Identifier GUI_TEXTURE = Identifier.fromNamespaceAndPath("stun-slam-guider", "textures/gui/practicemenu.png");
    private static final Identifier GUI_BACKGROUND = Identifier.fromNamespaceAndPath("stun-slam-guider", "textures/gui/background.png");
    private SkinSprite sprite;

    public PracticeScreen() {
        super(Component.literal("Stun Slam Practice"));
    }

    @Override
    public void extractRenderState(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a) {
        super.extractRenderState(graphics, mouseX, mouseY, a);
        drawTexture(graphics,GUI_BACKGROUND, 10, 10, 141, 166);
        if (this.sprite != null) {
            this.sprite.draw(graphics, this.width / 2 - 28, this.height / 2 - 32, 56, 64);
        }
        drawTexture(graphics,GUI_TEXTURE, 0, 0, 290, 208);
    }

    private void drawTexture(GuiGraphicsExtractor graphics, Identifier texture, int xOffset, int yOffset, int width, int height) {
        int left = (this.width - 290) / 2; // THE 290 AND 208 ARE THE SIZE OF THE MENU I JUST DONT WANNA DO A VARIABLE FOR THEM BC IM A BUM
        int top = (this.height - 208) / 2;

        graphics.blit(RenderPipelines.GUI_TEXTURED, texture, left + xOffset, top + yOffset, 0, 0, width, height, width, height);
    }

    @Override
    protected void init() {
        this.sprite = SkinSprite.create(this.minecraft.player.getSkin().body().texturePath());
    }
}
