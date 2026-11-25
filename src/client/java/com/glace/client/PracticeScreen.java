package com.glace.client;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class PracticeScreen extends Screen {
    private static final Identifier GUI_TEXTURE = Identifier.fromNamespaceAndPath("stun-slam-guider", "textures/gui/practicemenu.png");
    private static final Identifier GUI_BACKGROUND = Identifier.fromNamespaceAndPath("stun-slam-guider", "textures/gui/background.png");
    private SkinSprite sprite;
    private final PracticeHotbar hotbar = new PracticeHotbar();
    private boolean startHeld = false;
    private final PracticeTimeline timeline = new PracticeTimeline();
    private final PracticeRange practiceRange = new PracticeRange();


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
        timeline.render(graphics, guiLeft(), guiTop(), practiceRange);

        PracticeButtons.render(graphics, guiLeft(), guiTop(), PracticeButtons.EASY_X, PracticeButtons.EASY_Y, PracticeButtons.DIFFICULTY_W, PracticeButtons.DIFFICULTY_H, PracticeButtons.EASY_BUTTON, PracticeButtons.EASY_BUTTON_PRESSED, PracticeTimeline.difficulty == 1);
        PracticeButtons.render(graphics, guiLeft(), guiTop(), PracticeButtons.MEDIUM_X, PracticeButtons.MEDIUM_Y, PracticeButtons.DIFFICULTY_W, PracticeButtons.DIFFICULTY_H, PracticeButtons.MEDIUM_BUTTON, PracticeButtons.MEDIUM_BUTTON_PRESSED, PracticeTimeline.difficulty == 2);
        PracticeButtons.render(graphics, guiLeft(), guiTop(), PracticeButtons.HARD_X, PracticeButtons.HARD_Y, PracticeButtons.DIFFICULTY_W, PracticeButtons.DIFFICULTY_H, PracticeButtons.HARD_BUTTON, PracticeButtons.HARD_BUTTON_PRESSED, PracticeTimeline.difficulty == 3);
        PracticeButtons.render(graphics, guiLeft(), guiTop(), PracticeButtons.INSANE_X, PracticeButtons.INSANE_Y, PracticeButtons.DIFFICULTY_W, PracticeButtons.DIFFICULTY_H, PracticeButtons.INSANE_BUTTON, PracticeButtons.INSANE_BUTTON_PRESSED, PracticeTimeline.difficulty == 4);
        PracticeButtons.render(graphics, guiLeft(), guiTop(), PracticeButtons.GUIDE_X, PracticeButtons.GUIDE_Y, PracticeButtons.GUIDE_W, PracticeButtons.GUIDE_H, PracticeButtons.GUIDE_BUTTON, PracticeButtons.GUIDE_BUTTON_PRESSED, PracticeTimeline.guideMode);
        PracticeButtons.render(graphics, guiLeft(), guiTop(), PracticeButtons.LOOP_X, PracticeButtons.LOOP_Y, PracticeButtons.LOOP_W, PracticeButtons.LOOP_H, PracticeButtons.LOOP_BUTTON, PracticeButtons.LOOP_BUTTON_PRESSED, PracticeTimeline.loopMode);
        PracticeButtons.render(graphics, guiLeft(), guiTop(), PracticeButtons.START_X, PracticeButtons.START_Y, PracticeButtons.START_W, PracticeButtons.START_H, PracticeButtons.START_BUTTON, PracticeButtons.START_BUTTON_PRESSED, startHeld);
        practiceRange.render(graphics, guiLeft(), guiTop(), mouseX, mouseY);

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
        if (event.button() == 0 && !timeline.isLocked() && hotbar.click(event.x(), event.y(), guiLeft(), guiTop())) {
            return true;
        }
        if (event.button() == 0 && !timeline.isLocked() && PracticeButtons.contains(event.x(), event.y(), guiLeft(), guiTop(), PracticeButtons.EASY_X, PracticeButtons.EASY_Y, PracticeButtons.DIFFICULTY_W, PracticeButtons.DIFFICULTY_H)) {
            PracticeTimeline.difficulty = 1;
            return true;
        }
        if (event.button() == 0 && !timeline.isLocked() && PracticeButtons.contains(event.x(), event.y(), guiLeft(), guiTop(), PracticeButtons.MEDIUM_X, PracticeButtons.MEDIUM_Y, PracticeButtons.DIFFICULTY_W, PracticeButtons.DIFFICULTY_H)) {
            PracticeTimeline.difficulty = 2;
            return true;
        }
        if (event.button() == 0 && !timeline.isLocked() && PracticeButtons.contains(event.x(), event.y(), guiLeft(), guiTop(), PracticeButtons.HARD_X, PracticeButtons.HARD_Y, PracticeButtons.DIFFICULTY_W, PracticeButtons.DIFFICULTY_H)) {
            PracticeTimeline.difficulty = 3;
            return true;
        }
        if (event.button() == 0 && !timeline.isLocked() && PracticeButtons.contains(event.x(), event.y(), guiLeft(), guiTop(), PracticeButtons.INSANE_X, PracticeButtons.INSANE_Y, PracticeButtons.DIFFICULTY_W, PracticeButtons.DIFFICULTY_H)) {
            PracticeTimeline.difficulty = 4;
            return true;
        }
        if (event.button() == 0 && !timeline.isLocked() && PracticeButtons.contains(event.x(), event.y(), guiLeft(), guiTop(), PracticeButtons.GUIDE_X, PracticeButtons.GUIDE_Y, PracticeButtons.GUIDE_W, PracticeButtons.GUIDE_H)) {
                PracticeTimeline.guideMode = !PracticeTimeline.guideMode;
                return true;
        }
        if (event.button() == 0 && (PracticeTimeline.loopMode || !PracticeTimeline.startClicked) && PracticeButtons.contains(event.x(), event.y(), guiLeft(), guiTop(), PracticeButtons.LOOP_X, PracticeButtons.LOOP_Y, PracticeButtons.LOOP_W, PracticeButtons.LOOP_H)) {
                PracticeTimeline.loopMode = !PracticeTimeline.loopMode;
            return true;
        }

        if(event.button() == 0 && !timeline.isLocked() && PracticeButtons.contains(event.x(), event.y(), guiLeft(), guiTop(), PracticeButtons.START_X, PracticeButtons.START_Y, PracticeButtons.START_W, PracticeButtons.START_H)) {
            startHeld = true;
            timeline.start();
            return true;
        }

        if (event.button() == 0 && !timeline.isLocked() && practiceRange.click(event.x(), event.y(), guiLeft(), guiTop())) {
            return true;
        }

        return false;
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent event) {
        if(event.button() == 0) {
            startHeld = false;
            practiceRange.release();
            return true;
        }
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        timeline.tick();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void removed() {
        super.removed();
        PracticeTimeline.startClicked = false;
        PracticeTimeline.loopMode = false;
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent event, double dx, double dy) {
        practiceRange.drag(guiLeft(), (int)event.x());
        return true;
    }
}
