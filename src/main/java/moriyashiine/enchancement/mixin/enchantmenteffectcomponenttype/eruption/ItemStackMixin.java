/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.eruption;

import moriyashiine.enchancement.common.component.entity.EruptionComponent;
import moriyashiine.enchancement.common.enchantment.effect.EruptionEffect;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.init.ModSoundEvents;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.MathHelper;
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
	private void enchancement$eruption(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
		setUsing(user, false);
		if (hand == Hand.MAIN_HAND && !EnchancementUtil.isSufficientlyHigh(user, 0.25) && canUse(user.getRandom())) {
			setUsing(user, true);
			user.setCurrentHand(hand);
			cir.setReturnValue(TypedActionResult.consume((ItemStack) (Object) this));
		}
	}

	@Inject(method = "getMaxUseTime", at = @At("HEAD"), cancellable = true)
	private void enchancement$eruption(LivingEntity user, CallbackInfoReturnable<Integer> cir) {
		if (isUsing(user)) {
			cir.setReturnValue(72000);
		}
	}

	@Inject(method = "usageTick", at = @At("HEAD"))
	private void enchancement$eruptionTick(World world, LivingEntity user, int remainingUseTicks, CallbackInfo ci) {
		setUsing(user, canUse(user.getRandom()));
	}

	@Inject(method = "onStoppedUsing", at = @At("HEAD"), cancellable = true)
	private void enchancement$eruption(World world, LivingEntity user, int remainingUseTicks, CallbackInfo ci) {
		if (isUsing(user)) {
			ItemStack stack = (ItemStack) (Object) this;
			int useTime = getMaxUseTime(user) - remainingUseTicks;
			if (useTime >= EruptionEffect.getChargeTime(user.getRandom(), stack)) {
				if (user instanceof PlayerEntity player) {
					player.incrementStat(Stats.USED.getOrCreateStat(getItem()));
				}
				user.playSound(ModSoundEvents.ENTITY_GENERIC_ERUPT, 1, MathHelper.nextFloat(user.getRandom(), 0.8F, 1.2F));
				if (world.isClient) {
					EruptionComponent.useClient(user);
				} else {
					EruptionComponent.useServer(user);
				}
			}
			ci.cancel();
		}
	}

	@Unique
	private boolean canUse(Random random) {
		return EruptionEffect.getChargeTime(random, (ItemStack) (Object) this) != 0;
	}

	@Unique
	private static boolean isUsing(LivingEntity living) {
		return ModEntityComponents.ERUPTION.get(living).isUsing();
	}

	@Unique
	private static void setUsing(LivingEntity living, boolean using) {
		ModEntityComponents.ERUPTION.get(living).setUsing(using);
	}
}
