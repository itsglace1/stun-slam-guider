package com.glace.client;

import com.google.gson.GsonBuilder;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.components.toasts.ToastManager;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;

public class StunSlamConfig {

    public static ConfigClassHandler<StunSlamConfig> HANDLER = ConfigClassHandler.createBuilder(StunSlamConfig.class)
            .id(Identifier.fromNamespaceAndPath("stun-slam-guider", "config"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve("stun-slam-guider.json5"))
                    .appendGsonBuilder(GsonBuilder::setPrettyPrinting)
                    .setJson5(true)
                    .build())
            .build();

    @SerialEntry
    public boolean showMessages = true;
    @SerialEntry
    public int soundVolume = 50;

    @SerialEntry
    public int maceSlot = 1;
    @SerialEntry
    public int axeSlot = 2;

    @SerialEntry
    public boolean playScrollSound = true;

    public static Screen create(Screen parent) {

        Option<Integer> maceVariable = (Option.<Integer>createBuilder()
                .name(Component.literal("Mace Slot"))
                .description(OptionDescription.of(Component.literal("Sets where the mace will be on the hotbar.")))
                .binding(1, () -> HANDLER.instance().maceSlot, newVal -> HANDLER.instance().maceSlot = newVal)
                .controller(opt -> IntegerSliderControllerBuilder.create(opt).range(1, 9).step(1).formatValue(value -> Component.literal("Slot: " + value)))
                .build());

        Option<Integer> axeVariable = (Option.<Integer>createBuilder()
                .name(Component.literal("Axe Slot"))
                .description(OptionDescription.of(Component.literal("Sets where the axe will be on the hotbar.")))
                .binding(2, () -> HANDLER.instance().axeSlot, newVal -> HANDLER.instance().axeSlot = newVal)
                .controller(opt -> IntegerSliderControllerBuilder.create(opt).range(1, 9).step(1).formatValue(value -> Component.literal("Slot: " + value)))
                .build());

        return YetAnotherConfigLib.createBuilder()
                .title(Component.literal("Glace's Stun Slam Guider"))
                .save(() -> {
                    int limitCheckResult = nukeTheLimitBreakingPlayersHouses();
                    if (limitCheckResult == 1) {
                        warn("toast.stun-slam-guider.tools_same_slot");
                    } else if (limitCheckResult == 2) {
                        warn("toast.stun-slam-guider.out_of_range");
                    }

                    if (limitCheckResult != 0) {
                        maceVariable.requestSet(HANDLER.instance().maceSlot);
                        axeVariable.requestSet(HANDLER.instance().axeSlot);
                    }

                    HANDLER.save();
                })
                .category(ConfigCategory.createBuilder()
                        .name(Component.literal("General"))
                        .group(OptionGroup.createBuilder()
                                .name(Component.literal("Feedback"))
                                .option(Option.<Boolean>createBuilder()
                                        .name(Component.literal("Show Messages"))
                                        .description(OptionDescription.of(Component.literal("Displays a message above your hotbar to inform you about your stun slam result.")))
                                        .binding(true, () -> HANDLER.instance().showMessages, newVal -> HANDLER.instance().showMessages = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<Integer>createBuilder()
                                        .name(Component.literal("Sound Effect Volume"))
                                        .description(OptionDescription.of(Component.literal("Changes how loud the sound effects of stun slams are.")))
                                        .binding(50, () -> HANDLER.instance().soundVolume, newVal -> HANDLER.instance().soundVolume = newVal)
                                        .controller(opt -> IntegerSliderControllerBuilder.create(opt).range(0, 100).step(1).formatValue(value -> Component.literal(value + "%")))
                                        .build())
                                .build())
                        .build())
                .category(ConfigCategory.createBuilder()
                        .name(Component.literal("Practice Screen"))
                        .group(OptionGroup.createBuilder()
                                .name(Component.literal("Tool Slots"))
                                .option(maceVariable)
                                .option(axeVariable)
                                .build())
                        .group(OptionGroup.createBuilder()
                                .name(Component.literal("Sound Options"))
                                .option(Option.<Boolean>createBuilder()
                                        .name(Component.literal("Hotbar Scrolling Sound Effect"))
                                        .description(OptionDescription.of(Component.literal("Plays a sound every time you scroll")))
                                        .binding(true, () -> HANDLER.instance().playScrollSound, newVal -> HANDLER.instance().playScrollSound = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .build())
                        .build())

                .build()
                .generateScreen(parent);
    }

    public static int nukeTheLimitBreakingPlayersHouses() { //i hate naming things. this fixes stuff when a bum tries to put numbers out of range or sets both tools to the same slot
        if (HANDLER.instance().maceSlot > 9 || HANDLER.instance().maceSlot < 1 || HANDLER.instance().axeSlot > 9 || HANDLER.instance().axeSlot < 1) {
            HANDLER.instance().maceSlot = 1;
            HANDLER.instance().axeSlot = 2;
            return 2; //out of range
        } else if (HANDLER.instance().maceSlot == HANDLER.instance().axeSlot) {
            HANDLER.instance().maceSlot = 1;
            HANDLER.instance().axeSlot = 2;
            return 1; //same slot
        }
        return 0; //everythings chill
    }

    private static void warn(String messageKey) {
        ToastManager toastManager = Minecraft.getInstance().gui.toastManager();
        MutableComponent message = Component.translatable(messageKey);

        SystemToast.addOrUpdate(toastManager, SystemToast.SystemToastId.PACK_COPY_FAILURE, Component.translatable("toast.stun-slam-guider.title"), message);
    }

}
