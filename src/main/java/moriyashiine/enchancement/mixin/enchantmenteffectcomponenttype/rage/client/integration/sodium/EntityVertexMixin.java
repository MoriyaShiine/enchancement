/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.rage.client.integration.sodium;

import moriyashiine.enchancement.common.enchantment.effect.RageEffect;
import net.caffeinemc.mods.sodium.api.vertex.format.common.EntityVertex;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(value = EntityVertex.class, remap = false)
public class EntityVertexMixin {
	@ModifyVariable(method = "write", at = @At("HEAD"), ordinal = 0, argsOnly = true)
	private static int enchancement$rage(int value) {
		int color = RageEffect.color;
		if (color != -1) {
			if (value != -1) {
				color *= value;
			}
			return Integer.reverseBytes(color << 8 | 0xFF);
		}
		return value;
	}
}
