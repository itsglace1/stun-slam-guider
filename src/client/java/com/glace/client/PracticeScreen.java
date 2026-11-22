package com.glace.client;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;

public class PracticeScreen extends Screen {
    private static final Identifier GUI_TEXTURE = Identifier.fromNamespaceAndPath("stun-slam-guider", "textures/gui/practicemenu.png");
    private static final Identifier GUI_BACKGROUND = Identifier.fromNamespaceAndPath("stun-slam-guider", "textures/gui/background.png");
    private SkinSprite sprite;
    private final PracticeHotbar hotbar = new PracticeHotbar();

    public PracticeScreen() {
        super(Component.literal("Stun Slam Practice"));
    }

    @Override
    public void extractRenderState(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a) {
        super.extractRenderState(graphics, mouseX, mouseY, a);
        drawTexture(graphics,GUI_BACKGROUND, 10, 10, 141, 166);
        if (this.sprite != null) {
            this.sprite.draw(graphics, 10, 10, 56, 64);
            this.sprite.drawParts(graphics, 80, 10, 2);
        }
        drawTexture(graphics,GUI_TEXTURE, 0, 0, 290, 208);
        hotbar.render(graphics, guiLeft(), guiTop(), mouseX, mouseY);
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

    private int guiLeft() {
        return (this.width - 290) / 2;
    }

    private int guiTop() {
        return (this.height - 208) / 2;
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        for(int i = 0; i < 9; i++) {
            if(this.minecraft.options.keyHotbarSlots[i].matches(event)) {
                hotbar.select(i);

                return true;
            }
        }
        return super.keyPressed(event);
    }

    @Override
    public boolean mouseScrolled(double x, double y, double scrollX, double scrollY) {
        hotbar.scroll(scrollY);
        return true;
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        if(event.button() == 0 && hotbar.click(event.x(), event.y(), guiLeft(), guiTop())) {
            return true;
        } else{
            return false;
        }
    }
}
