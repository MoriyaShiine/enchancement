/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.fixvanillabugs;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.ModConfig;
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

	@SuppressWarnings("ConstantValue")
	@ModifyExpressionValue(method = "travelInWater", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getAttributeValue(Lnet/minecraft/core/Holder;)D"))
	private double enchancement$fixVanillaBugs(double original) {
		if (ModConfig.fixVanillaBugs) {
			if (isAutoSpinAttack() || (isUsingItem() && EnchantmentHelper.getTridentSpinAttackStrength(getActiveItem(), (LivingEntity) (Object) this) > 0)) {
				return 0;
			}
		}
		return original;
	}

	@ModifyReturnValue(method = "getSwimAmount", at = @At("RETURN"))
	private float enchancement$fixVanillaBugs(float original) {
		if (ModConfig.fixVanillaBugs && isAutoSpinAttack()) {
			return 0;
		}
		return original;
	}
}
