/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmententityeffecttype.spawnparticleswithcount;

import moriyashiine.enchancement.common.world.item.effects.entity.SpawnParticlesWithCountEnchantmentEffect;
import net.minecraft.world.item.enchantment.effects.SpawnParticlesEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(SpawnParticlesEffect.class)
public class SpawnParticlesEffectMixin {
	@ModifyArg(method = "apply", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;sendParticles(Lnet/minecraft/core/particles/ParticleOptions;DDDIDDDD)I"))
	private int enchancement$spawnParticlesWithCount(int value) {
		if (SpawnParticlesWithCountEnchantmentEffect.countOverride != -1) {
			return SpawnParticlesWithCountEnchantmentEffect.countOverride;
		}
		return value;
	}
}
