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

import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;

public class SkinSprite {
    private static final int W = 28, H = 32;
    private static final Identifier ID = Identifier.fromNamespaceAndPath("stun-slam-guider", "sprite/self");
    private final int[] out = new int[W * H];
    private final int[] skin;
    private final Map<SpriteParts, Identifier> partIds = new EnumMap<>(SpriteParts.class);
    private static final int GODDAMN_PADDING_THING = 1;

    private SkinSprite(int[] skin) {
        this.skin = skin;
    }

    public static SkinSprite create(Identifier skinTexture) {
        int[] skinArray;
        skinArray = readSkin(skinTexture);
        if (skinArray == null) {
            return null;
        } else {
            SkinSprite sprite = new SkinSprite(skinArray);
            sprite.head(8, 8, false);
            sprite.copyShaded(16, 5, 24, 21, 16, 8, 24, 16, 0.78, false);
            sprite.copy(6, 21, 17, 25, 20, 20, 28, 32);

            sprite.copy(3, 21, 6, 25, 44, 20, 48, 31);
            sprite.copy(3, 25, 6, 26, 44, 31, 48, 32);
            sprite.copy(17, 21, 24, 25, 36, 52, 40, 63);
            sprite.copy(17, 25, 24, 26, 36, 63, 40, 64);
            sprite.darken(21, 21, 24, 26, 0.88);

            sprite.copy(6, 25, 12, 27, 4, 20, 8, 31);
            sprite.copy(6, 27, 12, 28, 4, 31, 8, 32);
            sprite.copy(12, 25, 17, 27, 20, 52, 24, 63);
            sprite.copy(12, 27, 17, 28, 20, 63, 24, 64);

            sprite.copyShaded(17, 26, 19, 27, 4, 20, 8, 31, 0.82, false);
            sprite.copyShaded(17, 27, 19, 28, 4, 31, 8, 32, 0.82, false);

            sprite.outline(2, 5, 16, 21);
            sprite.outline(16, 5, 24, 21);
            sprite.outline(3, 21, 6, 26);
            sprite.outline(17, 21, 24, 26);
            sprite.outline(6, 25, 12, 28);
            sprite.outline(12, 25, 17, 28);

            sprite.head(40, 8, true);
            sprite.copyShaded(17, 5, 25, 21, 48, 8, 56, 16, 0.78, true);

            sprite.copyOverlay(6, 21, 17, 25, 20, 36, 28, 48);
            sprite.copyOverlay(3, 21, 6, 25, 44, 36, 48, 47);
            sprite.copyOverlay(3, 25, 6, 26, 44, 47, 48, 48);
            sprite.copyOverlay(17, 21, 24, 25, 52, 52, 56, 63);
            sprite.copyOverlay(17, 25, 24, 26, 52, 63, 56, 64);
            sprite.copyOverlay(6, 25, 12, 27, 4, 36, 8, 47);
            sprite.copyOverlay(6, 27, 12, 28, 4, 47, 8, 48);
            sprite.copyOverlay(12, 25, 17, 27, 4, 52, 8, 63);
            sprite.copyOverlay(12, 27, 17, 28, 4, 63, 8, 64);

            sprite.upload();
            sprite.uploadParts();
            return sprite;
        }
    }

    private static int[] toArray(NativeImage img) {
        if (img.getHeight() != 64 || img.getWidth() != 64) {
            warn("toast.stun-slam-guider.skin_size");
            return null;
        }

        int[] skinPixel = new int[4096];

        for (int y = 0; y < 64; y++) {
            for (int x = 0; x < 64; x++) {
                int color = img.getPixel(x, y);
                int slot = y * 64 + x;
                skinPixel[slot] = color;
            }
        }
        return skinPixel;
    }

    private static int[] readSkin(Identifier id) {
        AbstractTexture skinTexture = Minecraft.getInstance().getTextureManager().getTexture(id);

        if (skinTexture instanceof DynamicTexture dyn && dyn.getPixels() != null) {
            return toArray(dyn.getPixels());
        }
        warn("toast.stun-slam-guider.skin_type");
        return null;
    }

    private static void warn(String messageKey) {
        ToastManager toastManager = Minecraft.getInstance().gui.toastManager();
        MutableComponent message = Component.translatable(messageKey);

        SystemToast.addOrUpdate(toastManager, SystemToast.SystemToastId.PACK_COPY_FAILURE, Component.translatable("toast.stun-slam-guider.title"), message);
    }

    private void put(int x, int y, int color) {
        if (x >= 0 && x < W && y >= 0 && y < H) {
            out[y * W + x] = color;
        }
    }

    private int get(int x, int y) {
        if (x >= 0 && x < W && y >= 0 && y < H) {
            return out[y * W + x];
        }
        return 0;
    }

    private void upload() {
        NativeImage imRunningOutOfNamesForVariables = new NativeImage(W, H, true);

        for (int y = 0; y < H; y++) {
            for (int x = 0; x < W; x++) {
                imRunningOutOfNamesForVariables.setPixel(x, y, get(x, y));
            }
        }
        DynamicTexture result = new DynamicTexture(() -> "stun-slam-guider sprite", imRunningOutOfNamesForVariables);
        Minecraft.getInstance().getTextureManager().register(ID, result);
    }

    public void draw(GuiGraphicsExtractor graphics, int x, int y, int width, int height) {
        graphics.blit(RenderPipelines.GUI_TEXTURED, ID, x, y, 0, 0, width, height, W, H, W, H);
    }

    private void copyShaded(int destLeft, int destTop, int destRight, int destBottom, int srcLeft, int srcTop, int srcRight, int srcBottom, double brightness, boolean secondLayer) {
        for (int y = destTop; y < destBottom; y++) {
            for (int x = destLeft; x < destRight; x++) {
                int srcX = srcLeft + (x - destLeft) * (srcRight - srcLeft) / (destRight - destLeft);
                int srcY = srcTop + (y - destTop) * (srcBottom - srcTop) / (destBottom - destTop);
                int color = skin[srcY * 64 + srcX];
                if(secondLayer && ((color >> 24) & 0xFF) == 0) { continue; }
                put(x, y, shade(color, brightness));
            }
        }
    }

    private void copy(int destLeft, int destTop, int destRight, int destBottom, int srcLeft, int srcTop, int srcRight, int srcBottom) {
        copyShaded(destLeft, destTop, destRight, destBottom, srcLeft, srcTop, srcRight, srcBottom, 1.0, false);
    }

    private void head(int srcLeft, int srcTop, boolean overlay) {
        int[] widths = {2, 2, 2, 1, 1, 2, 2, 2};
        int x = 2;
        for (int i = 0; i <= 7; i++) {
            copyShaded(x, 5, x + widths[i], 21, srcLeft + i, srcTop, srcLeft + i + 1, srcTop + 8, 1.0, overlay);
            x += widths[i];
        }
    }

    private int shade(int color, double howMuch) {
        int alpha = (color >> 24) & 0xFF;
        int red = (color >> 16) & 0xFF;
        int green = (color >> 8) & 0xFF;
        int blue = color & 0xFF;

        red = (int) Math.round(red * howMuch);
        green = (int) Math.round(green * howMuch);
        blue = (int) Math.round(blue * howMuch);

        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    }

    private void darken(int left, int top, int right, int bottom, double factor) {
        for (int y = top; y < bottom; y++) {
            for (int x = left; x < right; x++) {
                int color = get(x,y);
                put(x, y, shade(color, factor));

            }
        }
    }

    private void outline(int left, int top, int right, int bottom) {
        darken(left, top, right, top + 1, 0.7);
        darken(left, bottom - 1, right, bottom, 0.7);
        darken(left, top + 1, left + 1, bottom - 1, 0.7);
        darken(right - 1, top + 1, right, bottom - 1, 0.7);

    }

    private void copyOverlay(int destLeft, int destTop, int destRight, int destBottom, int srcLeft, int srcTop, int srcRight, int srcBottom) {
        copyShaded(destLeft, destTop, destRight, destBottom, srcLeft, srcTop, srcRight, srcBottom, 1.0, true);
    }

    private boolean inside(SpriteParts part, int x, int y) {
        return x >= part.x && x < (part.x + part.width) && y >= part.y && y < (part.y + part.height);
    }

    private boolean owns(SpriteParts part, int x, int y) {

        for(SpriteParts spriteValue: SpriteParts.values()) {
            if(spriteValue == part) {
                break;
            } else {
                if(inside(spriteValue, x, y)) {
                    return false;
                }
            }
        }
        return inside(part,x,y);
    }

    private void uploadParts() {
        for(SpriteParts spriteParts: SpriteParts.values()) {
            NativeImage image = new NativeImage(spriteParts.width + 2 * GODDAMN_PADDING_THING, spriteParts.height + 2 * GODDAMN_PADDING_THING, true);
            for(int y = 0; y < spriteParts.height; y++) {
                for (int x = 0; x < spriteParts.width; x++) {
                    int canvasX = spriteParts.x + x;
                    int canvasY = spriteParts.y + y;
                    if (owns(spriteParts, canvasX, canvasY)) {
                        image.setPixel(x + GODDAMN_PADDING_THING, y + GODDAMN_PADDING_THING, get(canvasX, canvasY));
                    }
                }
            }
            DynamicTexture texture = new DynamicTexture(() -> "stun-slam-guider part", image);
            Identifier imSeriouslyRunningOutOfVariableNames = Identifier.fromNamespaceAndPath("stun-slam-guider", "sprite/self/" + spriteParts.name().toLowerCase(Locale.ROOT));
            Minecraft.getInstance().getTextureManager().register(imSeriouslyRunningOutOfVariableNames, texture);
            partIds.put(spriteParts, imSeriouslyRunningOutOfVariableNames);
        }
    }

    public void drawPart(GuiGraphicsExtractor graphics, SpriteParts part, int x, int y, int scale) {
        graphics.blit(RenderPipelines.GUI_TEXTURED, partIds.get(part), x - GODDAMN_PADDING_THING * scale, y - GODDAMN_PADDING_THING * scale, 0,0, (part.width + 2 * GODDAMN_PADDING_THING) * scale, (part.height + 2 * GODDAMN_PADDING_THING) * scale, part.width + 2 * GODDAMN_PADDING_THING, part.height + 2 * GODDAMN_PADDING_THING,part.width + 2 * GODDAMN_PADDING_THING, part.height + 2 * GODDAMN_PADDING_THING);
    }

    public void drawParts(GuiGraphicsExtractor graphics, int x, int y, int scale) {
        for(SpriteParts parts: SpriteParts.values()) {
            drawPart(graphics, parts, x + parts.x * scale, y + parts.y * scale, scale);
        }
    }
}