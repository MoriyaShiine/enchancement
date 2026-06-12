/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.fixvanillabugs;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.EnchancementConfig;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
	@Shadow
	public abstract boolean isAutoSpinAttack();

	@Shadow
	public abstract boolean isUsingItem();

	@Shadow
	public abstract ItemStack getActiveItem();

	@Inject(method = "onEquipItem", at = @At("TAIL"))
	private void enchancement$fixVanillaBugs(EquipmentSlot slot, ItemStack oldStack, ItemStack stack, CallbackInfo ci) {
		if (EnchancementConfig.fixVanillaBugs && slot == EquipmentSlot.MAINHAND) {
			EnchancementUtil.refreshAttributesAndCooldown((LivingEntity) (Object) this);
		}
	}

	@ModifyReturnValue(method = "getSwimAmount", at = @At("RETURN"))
	private float enchancement$fixVanillaBugs(float original) {
		if (EnchancementConfig.fixVanillaBugs && isAutoSpinAttack()) {
			return 0;
		}
		return original;
	}

	@SuppressWarnings("ConstantValue")
	@ModifyExpressionValue(method = "travelInWater", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getAttributeValue(Lnet/minecraft/core/Holder;)D"))
	private double enchancement$fixVanillaBugs(double original) {
		if (EnchancementConfig.fixVanillaBugs) {
			if (isAutoSpinAttack() || (isUsingItem() && EnchantmentHelper.getTridentSpinAttackStrength(getActiveItem(), (LivingEntity) (Object) this) > 0)) {
				return 0;
			}
		}
		return original;
	}
}
