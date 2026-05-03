/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.rebalanceenchantments;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
	@Shadow
	public abstract boolean isAutoSpinAttack();

	@Shadow
	public abstract boolean isUsingItem();

	@Shadow
	public abstract ItemStack getActiveItem();

	@ModifyExpressionValue(method = "hurtServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/damagesource/DamageSource;is(Lnet/minecraft/tags/TagKey;)Z", ordinal = 4))
	private boolean enchancement$rebalanceEnchantments(boolean original, @Local(argsOnly = true) DamageSource source) {
		return original || (source.is(DamageTypes.ON_FIRE) && ModEntityComponents.IGNITE_KNOCKBACK.get(this).isIgnited());
	}

	@SuppressWarnings("ConstantValue")
	@ModifyExpressionValue(method = "travelInWater", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getAttributeValue(Lnet/minecraft/core/Holder;)D"))
	private double enchancement$rebalanceEnchantments(double original) {
		if (ModConfig.rebalanceEnchantments) {
			if (isAutoSpinAttack() || (isUsingItem() && EnchantmentHelper.getTridentSpinAttackStrength(getActiveItem(), (LivingEntity) (Object) this) > 0)) {
				return 0;
			}
		}
		return original;
	}

	@ModifyReturnValue(method = "getSwimAmount", at = @At("RETURN"))
	private float enchancement$rebalanceEnchantments(float original) {
		if (ModConfig.rebalanceEnchantments && isAutoSpinAttack()) {
			return 0;
		}
		return original;
	}
}
