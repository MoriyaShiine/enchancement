/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.fluidwalking;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Entity.class)
public class EntityMixin {
	@ModifyArg(method = "lavaIgnite", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;igniteForSeconds(F)V"))
	private float enchancement$fluidWalking(float seconds) {
		if (EnchancementUtil.hasAnyEnchantmentsWith((Entity) (Object) this, ModEnchantmentEffectComponentTypes.FLUID_WALKING)) {
			seconds /= 6;
		}
		return seconds;
	}

	@SuppressWarnings("ConstantValue")
	@ModifyReturnValue(method = "dismountsUnderwater", at = @At("RETURN"))
	private boolean enchancement$fluidWalking(boolean original) {
		return original && !EnchancementUtil.hasAnyEnchantmentsWith((Entity) (Object) this, ModEnchantmentEffectComponentTypes.FLUID_WALKING);
	}
}
