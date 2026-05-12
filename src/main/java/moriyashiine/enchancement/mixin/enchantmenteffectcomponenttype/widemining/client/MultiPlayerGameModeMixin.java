/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.widemining.client;

import com.llamalad7.mixinextras.sugar.Local;
import moriyashiine.enchancement.client.event.enchantmenteffectcomponenttype.WideMiningClientEvent;
import moriyashiine.enchancement.common.component.level.WideMiningComponent;
import moriyashiine.enchancement.common.event.enchantmenteffectcomponenttype.WideMiningEvent;
import moriyashiine.enchancement.common.init.ModLevelComponents;
import moriyashiine.enchancement.common.payload.UpdateWideMiningEntryPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MultiPlayerGameMode.class)
public abstract class MultiPlayerGameModeMixin {
	@Unique
	private boolean callingAgain = false;

	@Shadow
	@Final
	private Minecraft minecraft;

	@Shadow
	public abstract InteractionResult useItemOn(LocalPlayer player, InteractionHand hand, BlockHitResult blockHit);

	@Inject(method = "startDestroyBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;startPrediction(Lnet/minecraft/client/multiplayer/ClientLevel;Lnet/minecraft/client/multiplayer/prediction/PredictiveAction;)V"))
	private void enchancement$wideMining(BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> cir, @Local(name = "state") BlockState state) {
		if (minecraft.player != null && WideMiningEvent.canActivate(minecraft.player, minecraft.player.getMainHandItem(), state)) {
			WideMiningComponent.Entry entry = WideMiningClientEvent.createEntry(minecraft.player, direction, minecraft.player.getMainHandItem(), minecraft.level, pos);
			if (WideMiningEvent.isValid(entry.blocks(), minecraft.player.getMainHandItem()) && ModLevelComponents.WIDE_MINING.get(minecraft.level).addEntry(entry)) {
				UpdateWideMiningEntryPayload.send(entry, true);
			}
		}
	}

	@Inject(method = "stopDestroyBlock", at = @At("TAIL"))
	private void enchancement$wideMining(CallbackInfo ci) {
		WideMiningComponent wideMiningComponent = ModLevelComponents.WIDE_MINING.get(minecraft.level);
		WideMiningComponent.Entry entry = wideMiningComponent.getEntry(minecraft.player);
		if (entry != null && wideMiningComponent.removeEntry(entry.player())) {
			UpdateWideMiningEntryPayload.send(entry, false);
		}
	}

	@Inject(method = "useItemOn", at = @At(value = "RETURN", ordinal = 1))
	private void enchancement$wideMining(LocalPlayer player, InteractionHand hand, BlockHitResult blockHit, CallbackInfoReturnable<InteractionResult> cir) {
		if (!callingAgain && cir.getReturnValue().consumesAction()) {
			ItemStack stack = player.getItemInHand(hand);
			if (WideMiningEvent.canActivate(player, stack, player.level().getBlockState(blockHit.getBlockPos()))) {
				callingAgain = true;
				WideMiningComponent.Entry entry = WideMiningClientEvent.createEntry(player, blockHit.getDirection(), player.getMainHandItem(), player.level(), blockHit.getBlockPos());
				if (WideMiningEvent.isValid(entry.blocks(), stack)) {
					entry.blocks().forEach(pos -> useItemOn(player, hand, new BlockHitResult(pos.getCenter(), blockHit.getDirection(), pos, blockHit.isInside(), blockHit.isWorldBorderHit())));
				}
				callingAgain = false;
			}
		}
	}
}
