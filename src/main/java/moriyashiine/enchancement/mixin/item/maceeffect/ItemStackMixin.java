/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.item.maceeffect;

import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.enchancement.common.util.enchantment.effect.MaceEffect;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
	@Shadow
	public abstract Item getItem();

	@Shadow
	public abstract int getUseDuration(LivingEntity user);

	@Inject(method = "use", at = @At("HEAD"), cancellable = true)
	private void enchancement$maceEffect(Level level, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
		for (MaceEffect effect : MaceEffect.EFFECTS) {
			ItemStack stack = player.getItemInHand(hand);
			effect.setUsing(player, false);
			if (hand == InteractionHand.MAIN_HAND && effect.canUse(player.getRandom(), stack)) {
				effect.setUsing(player, true);
				player.startUsingItem(hand);
				cir.setReturnValue(InteractionResult.CONSUME);
				return;
			}
		}
	}

	@Inject(method = "getUseDuration", at = @At("HEAD"), cancellable = true)
	private void enchancement$maceEffect(LivingEntity user, CallbackInfoReturnable<Integer> cir) {
		if (user instanceof Player player) {
			for (MaceEffect effect : MaceEffect.EFFECTS) {
				if (effect.isUsing(player)) {
					cir.setReturnValue(72000);
					return;
				}
			}
		}
	}

	@Inject(method = "onUseTick", at = @At("HEAD"))
	private void enchancement$maceEffectTick(Level level, LivingEntity livingEntity, int ticksRemaining, CallbackInfo ci) {
		if (livingEntity instanceof Player player) {
			for (MaceEffect effect : MaceEffect.EFFECTS) {
				effect.setUsing(player, effect.canUse(livingEntity.getRandom(), (ItemStack) (Object) this));
			}
		}
	}

	@Inject(method = "releaseUsing", at = @At("HEAD"), cancellable = true)
	private void enchancement$maceEffect(Level level, LivingEntity entity, int remainingTime, CallbackInfo ci) {
		if (entity instanceof Player player) {
			ItemStack stack = (ItemStack) (Object) this;
			for (MaceEffect effect : MaceEffect.EFFECTS) {
				if (effect.isUsing(player)) {
					int useTime = getUseDuration(entity) - remainingTime;
					if (useTime >= EnchancementUtil.getMaceOrTridentChargeTime(stack)) {
						player.awardStat(Stats.ITEM_USED.get(getItem()));
						effect.use(level, player, stack);
						ModEntityComponents.GROUNDED_COOLDOWN.get(player).putOnCooldown(stack, 60);
					}
					ci.cancel();
					return;
				}
			}
		}
	}
}
