/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmententityeffecttype.spawnparticleswithcount;

import moriyashiine.enchancement.common.enchantment.effect.entity.SpawnParticlesWithCountEnchantmentEffect;
import net.minecraft.enchantment.effect.entity.SpawnParticlesEnchantmentEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(SpawnParticlesEnchantmentEffect.class)
public class SpawnParticlesEnchantmentEffectMixin {
	@ModifyArg(method = "apply", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;spawnParticles(Lnet/minecraft/particle/ParticleEffect;DDDIDDDD)I"))
	private int enchancement$spawnParticlesWithCount(int value) {
		if (SpawnParticlesWithCountEnchantmentEffect.countOverride != -1) {
			return SpawnParticlesWithCountEnchantmentEffect.countOverride;
		}
		return value;
	}
}
