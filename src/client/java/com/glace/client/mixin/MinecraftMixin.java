package com.glace.client.mixin;

import com.glace.client.StunSlamGuiderClient;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public class MinecraftMixin {
	@Inject(method = "startAttack", at = @At("HEAD"))
	private void stunslam$onStartAttack(CallbackInfoReturnable<Boolean> cir) {
		StunSlamGuiderClient.onSwing();
	}
}