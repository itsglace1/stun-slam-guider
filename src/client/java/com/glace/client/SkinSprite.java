package com.glace.client;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.components.toasts.ToastManager;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;

public class SkinSprite {
    private static final int W = 28, H = 32;
    private static final Identifier ID = Identifier.fromNamespaceAndPath("stun-slam-guider", "sprite/self");
    private final int[] out = new int[W * H];
    private final int[] skin;

    private SkinSprite(int[] skin) {
        this.skin = skin;
    }

    public static SkinSprite create(Identifier skinTexture) {
        int[] skinArray;
        skinArray = readSkin(skinTexture);
        if(skinArray == null) {
            return null;
        } else {
            SkinSprite sprite = new SkinSprite(skinArray);
            for(int y = 0; y < H; y++) {
                for(int x = 0; x < W; x++) {
                    sprite.put(x, y, 0xFF00FF00);
                }
            }
            sprite.upload();
            return sprite;
        }
    }

    private static int[] toArray(NativeImage img) {
        if(img.getHeight() != 64 || img.getWidth() != 64) {
            warn("toast.stun-slam-guider.skin_size");
            return null;
        }

        int[] skinPixel = new int[4096];

        for(int y = 0; y < 64; y++) {
            for(int x = 0; x < 64; x++) {
                int color = img.getPixel(x,y);
                int slot = y * 64 + x;
                skinPixel[slot] = color;
            }
        }
        return skinPixel;
    }

    private static int[] readSkin(Identifier id) {
        AbstractTexture skinTexture = Minecraft.getInstance().getTextureManager().getTexture(id);

        if(skinTexture instanceof DynamicTexture dyn && dyn.getPixels() != null) {
            return toArray(dyn.getPixels());
        }
        warn("toast.stun-slam-guider.skin_type");
        return null;
    }

    private static void warn(String messageKey) {
        ToastManager toastManager = Minecraft.getInstance().gui.toastManager();
        MutableComponent message = Component.translatable(messageKey);

        SystemToast.addOrUpdate(toastManager,SystemToast.SystemToastId.PACK_COPY_FAILURE, Component.translatable("toast.stun-slam-guider.title"), message);
    }

    private void put(int x, int y, int color) {
        if(x >= 0 && x < W && y >= 0 && y < H) {
            out[y * W + x] = color;
        }
    }

    private int get(int x, int y) {
        if(x >= 0 && x < W && y >= 0 && y < H) {
            return out[y * W + x];
        }
        return 0;
    }

    private void upload() {
        NativeImage imRunningOutOfNamesForVariables = new NativeImage(W, H, true);

        for(int y = 0; y < H; y++) {
            for(int x = 0; x < W; x++) {
                imRunningOutOfNamesForVariables.setPixel(x,y, get(x,y));
            }
        }
        DynamicTexture result = new DynamicTexture(() -> "stun-slam-guider sprite", imRunningOutOfNamesForVariables);
        Minecraft.getInstance().getTextureManager().register(ID, result);
    }

    public void draw(GuiGraphicsExtractor graphics, int x, int y, int width, int height) {
        graphics.blit(RenderPipelines.GUI_TEXTURED, ID, x, y, 0, 0, width, height, W, H, W, H);
    }

    private void copy(int destLeft, int destTop, int destRight, int destBottom, int srcLeft, int srcTop, int srcRight, int srcBottom) {
        for(int y = destTop; y < destBottom; y++) {
            for(int x = destLeft; x < destRight; x++) {
                int srcX = srcLeft + (x - destLeft) * (srcRight - srcLeft) / (destRight - destLeft);
                int srcY = srcTop + (y - destTop) * (srcBottom - srcTop) / (destBottom - destTop);
                int color = skin[srcY * 64 + srcX];
                put(srcX, srcX, color);

            }
        }
    }

}