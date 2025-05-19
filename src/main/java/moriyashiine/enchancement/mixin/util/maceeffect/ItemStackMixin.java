/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.util.maceeffect;

import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.enchancement.common.util.enchantment.MaceEffect;
import moriyashiine.strawberrylib.api.module.SLibUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
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
	public abstract int getMaxUseTime(LivingEntity user);

	@Inject(method = "use", at = @At("HEAD"), cancellable = true)
	private void enchancement$maceEffect(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
		for (MaceEffect effect : MaceEffect.EFFECTS) {
			ItemStack stack = user.getStackInHand(hand);
			effect.setUsing(user, false);
			if (hand == Hand.MAIN_HAND && !SLibUtils.isSufficientlyHigh(user, 0.25) && effect.canUse(user.getRandom(), stack)) {
				effect.setUsing(user, true);
				user.setCurrentHand(hand);
				cir.setReturnValue(ActionResult.CONSUME);
				return;
			}
		}
	}

	@Inject(method = "getMaxUseTime", at = @At("HEAD"), cancellable = true)
	private void enchancement$maceEffect(LivingEntity user, CallbackInfoReturnable<Integer> cir) {
		if (user instanceof PlayerEntity player) {
			for (MaceEffect effect : MaceEffect.EFFECTS) {
				if (effect.isUsing(player)) {
					cir.setReturnValue(72000);
					return;
				}
			}
		}
	}

	@Inject(method = "usageTick", at = @At("HEAD"))
	private void enchancement$maceEffectTick(World world, LivingEntity user, int remainingUseTicks, CallbackInfo ci) {
		if (user instanceof PlayerEntity player) {
			for (MaceEffect effect : MaceEffect.EFFECTS) {
				effect.setUsing(player, effect.canUse(user.getRandom(), (ItemStack) (Object) this));
			}
		}
	}

	@Inject(method = "onStoppedUsing", at = @At("HEAD"), cancellable = true)
	private void enchancement$maceEffect(World world, LivingEntity user, int remainingUseTicks, CallbackInfo ci) {
		if (user instanceof PlayerEntity player) {
			for (MaceEffect effect : MaceEffect.EFFECTS) {
				if (effect.isUsing(player)) {
					int useTime = getMaxUseTime(user) - remainingUseTicks;
					if (useTime >= EnchancementUtil.getTridentChargeTime()) {
						player.incrementStat(Stats.USED.getOrCreateStat(getItem()));
						effect.use(world, player, (ItemStack) (Object) this);
					}
					ci.cancel();
					return;
				}
			}
		}
	}
}
