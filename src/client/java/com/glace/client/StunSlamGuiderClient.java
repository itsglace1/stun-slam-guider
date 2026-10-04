package com.glace.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;

public class StunSlamGuiderClient implements ClientModInitializer {
	private static boolean shieldBroken = false;
	private static  boolean axeSwung = false;
	private  static Component result = null;
	private static SoundEvent resultSound = null;
	private static float resultPitch = 1.0f;
	private static float resultVolume = 1.0f;
	private static int ticksSinceSlam = 0;
	private static final int RESULT_DELAY = 10;

	public static void onSwing() {
		LocalPlayer player = Minecraft.getInstance().player;
		if(player == null) {
			return;
		}
		if(player.fallDistance > 1.3f && player.getItemInHand(InteractionHand.MAIN_HAND).is(ItemTags.AXES)) {
			axeSwung = true;
		}
	}
	private static boolean hasMaceInHotbar(Player player) {
		for (int i = 0; i < 9; i++) {
			if (player.getInventory().getItem(i).is(ItemTags.MACE_ENCHANTABLE)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void onInitializeClient() {
		StunSlamConfig.HANDLER.load();
		AttackEntityCallback.EVENT.register((player, level, hand, entity, hitResult) -> {
			if(!level.isClientSide()) {
				return InteractionResult.PASS;
			}
			ItemStack heldItem =player.getItemInHand(hand);
			if(player.fallDistance > 1.3f) {
				boolean targetBlocking = false;
				boolean isBehind = false;
				if(entity instanceof LivingEntity target) {
					targetBlocking = target.isUsingItem() && target.getUseItem().is(Items.SHIELD);
					Vec3 facing = Vec3.directionFromRotation(0, target.getYHeadRot());
					Vec3 playerPos = player.position();
					Vec3 targetPos = target.position();
					Vec3 toPlayer = playerPos.subtract(targetPos);

					Vec3 flatToPlayer = new Vec3(toPlayer.x, 0, toPlayer.z);

					if (flatToPlayer.length() < 0.1) {
						isBehind = false;
					} else {
						facing = new Vec3(facing.x, 0, facing.z).normalize();
						toPlayer = flatToPlayer.normalize();
						isBehind = facing.dot(toPlayer) < 0;
					}
				}
				boolean shieldFacing = targetBlocking && !isBehind;

				if(heldItem.is(ItemTags.AXES)) {
					axeSwung = true;

					if (shieldBroken && result == null && hasMaceInHotbar(player)) {
						result = Component.literal("You did not switch to the Mace in time.").withStyle(ChatFormatting.RED);
						resultSound = SoundEvents.NOTE_BLOCK_BASS.value();
						resultPitch = 0.6f;
						resultVolume = 2.5f;
					}

					if (shieldFacing) {
						shieldBroken = true;
					}

				} else if (heldItem.is(ItemTags.MACE_ENCHANTABLE) && result == null) {
						if (shieldBroken) {
							result = Component.literal("Successful Stun Slam!").withStyle(ChatFormatting.GREEN);
							resultSound = SoundEvents.PLAYER_LEVELUP;
							resultPitch = 1.0f;
							resultVolume = 1.5f;
						} else if (shieldFacing && axeSwung) {
							result = Component.literal("You clicked too early.").withStyle(ChatFormatting.RED);
							resultSound = SoundEvents.NOTE_BLOCK_BASS.value();
							resultPitch = 0.6f;
							resultVolume = 2.5f;
						} else if (shieldFacing) {
							result = Component.literal("You switched to the Mace too early.").withStyle(ChatFormatting.RED);
							resultSound = SoundEvents.NOTE_BLOCK_BASS.value();
							resultPitch = 0.6f;
							resultVolume = 2.5f;
						} else if (targetBlocking && isBehind) {
							result = Component.literal("You landed a backstab!").withStyle(ChatFormatting.DARK_GREEN);
							resultSound = SoundEvents.ARROW_HIT_PLAYER;
							resultPitch = 0.8f;
							resultVolume = 1.5f;
						}
					}
				}
				return InteractionResult.PASS;
		});

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if(client.player == null) {
				return;
			}

			if(axeSwung || result != null) {
				ticksSinceSlam++;

				if (client.player.onGround() || ticksSinceSlam >= RESULT_DELAY) {

					if (result != null) {
						if (StunSlamConfig.HANDLER.instance().showMessages) {
							client.player.displayClientMessage(result, true);
						}
						if(StunSlamConfig.HANDLER.instance().playSounds) {
							client.player.playSound(resultSound, resultVolume, resultPitch);
						}
					}

					axeSwung = false;
					shieldBroken = false;
					result = null;
					resultSound = null;
					ticksSinceSlam = 0;
				}
			}
		});

	}
}