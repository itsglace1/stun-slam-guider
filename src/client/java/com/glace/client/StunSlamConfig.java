package com.glace.client;

import com.google.gson.GsonBuilder;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
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
    public boolean playSounds = true;

    public static Screen create(Screen parent) {
        return YetAnotherConfigLib.createBuilder()
                .title(Component.literal("Glace's Stun Slam Guider"))
                .save(() -> HANDLER.save())
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
                                .option(Option.<Boolean>createBuilder()
                                        .name(Component.literal("Play Sounds"))
                                        .description(OptionDescription.of(Component.literal("Plays a different sound based on your stun slam result.")))
                                        .binding(true, () -> HANDLER.instance().playSounds, newVal -> HANDLER.instance().playSounds = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .build())
                        .build())
                .build()
                .generateScreen(parent);
    }
}
