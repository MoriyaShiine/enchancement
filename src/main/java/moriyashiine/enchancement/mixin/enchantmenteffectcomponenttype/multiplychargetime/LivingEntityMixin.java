/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.multiplychargetime;

import moriyashiine.enchancement.common.entity.UseTimeDeltaHolder;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements UseTimeDeltaHolder {
	@Unique
	private float useTimeDelta = 0;

	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Override
	public float enchancement$getUseTimeDelta() {
		return useTimeDelta;
	}

	@Inject(method = "tickItemStackUsage", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;usageTick(Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;I)V", shift = At.Shift.AFTER), cancellable = true)
	private void enchancement$multiplyChargeTime(ItemStack stack, CallbackInfo ci) {
		float multiplier = EnchancementUtil.getValue(ModEnchantmentEffectComponentTypes.MULTIPLY_CHARGE_TIME, getRandom(), stack, 1);
		if (multiplier != 1) {
			if (useTimeDelta < 1) {
				useTimeDelta = Math.min(1, useTimeDelta + 1 / multiplier);
				ci.cancel();
			} else {
				useTimeDelta = 0;
			}
		}
	}

	@Inject(method = "clearActiveItem", at = @At("TAIL"))
	private void enchancement$multiplyChargeTime(CallbackInfo ci) {
		useTimeDelta = 0;
	}
}
