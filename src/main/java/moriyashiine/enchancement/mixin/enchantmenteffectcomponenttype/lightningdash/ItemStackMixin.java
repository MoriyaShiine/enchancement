/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.lightningdash;

import moriyashiine.enchancement.common.component.entity.LightningDashComponent;
import moriyashiine.enchancement.common.enchantment.effect.LightningDashEffect;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
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
	private void enchancement$lightningDash(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
		ItemStack stack = user.getStackInHand(hand);
		setUsing(user, false);
		if (hand == Hand.MAIN_HAND && !EnchancementUtil.isSufficientlyHigh(user, 0.25) && canUse(user.getRandom(), stack)) {
			setUsing(user, true);
			user.setCurrentHand(hand);
			cir.setReturnValue(ActionResult.CONSUME);
		}
	}

	@Inject(method = "getMaxUseTime", at = @At("HEAD"), cancellable = true)
	private void enchancement$lightningDash(LivingEntity user, CallbackInfoReturnable<Integer> cir) {
		if (isUsing(user)) {
			cir.setReturnValue(72000);
		}
	}

	@Inject(method = "usageTick", at = @At("HEAD"))
	private void enchancement$lightningDashTick(World world, LivingEntity user, int remainingUseTicks, CallbackInfo ci) {
		setUsing(user, canUse(user.getRandom(), (ItemStack) (Object) this));
	}

	@Inject(method = "onStoppedUsing", at = @At("HEAD"), cancellable = true)
	private void enchancement$lightningDash(World world, LivingEntity user, int remainingUseTicks, CallbackInfo ci) {
		if (isUsing(user)) {
			ItemStack stack = (ItemStack) (Object) this;
			int useTime = getMaxUseTime(user) - remainingUseTicks;
			if (useTime >= LightningDashEffect.getChargeTime(user.getRandom(), stack)) {
				if (user instanceof PlayerEntity player) {
					player.incrementStat(Stats.USED.getOrCreateStat(getItem()));
				}
				Vec3d lungeVelocity = user.getRotationVector().multiply(LightningDashEffect.getLungeStrength(user.getRandom(), stack));
				int floatTicks = LightningDashEffect.getFloatTime(user.getRandom(), stack);
				LightningDashComponent lightningDashComponent = ModEntityComponents.LIGHTNING_DASH.get(user);
				lightningDashComponent.useCommon(lungeVelocity, floatTicks);
				if (world.isClient) {
					lightningDashComponent.useClient();
				} else {
					lightningDashComponent.useServer(lungeVelocity, floatTicks);
				}
			}
			ci.cancel();
		}
	}

	@Unique
	private boolean canUse(Random random, ItemStack stack) {
		return LightningDashEffect.getChargeTime(random, stack) != 0;
	}

	@Unique
	private static boolean isUsing(LivingEntity living) {
		return ModEntityComponents.LIGHTNING_DASH.get(living).isUsing();
	}

	@Unique
	private static void setUsing(LivingEntity living, boolean using) {
		ModEntityComponents.LIGHTNING_DASH.get(living).setUsing(using);
	}
}
