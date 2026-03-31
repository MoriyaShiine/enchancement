/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.rage.client.integration.sodium;

import moriyashiine.enchancement.common.world.item.effects.RageEffect;
import net.caffeinemc.mods.sodium.api.vertex.format.common.EntityVertex;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(EntityVertex.class)
public class EntityVertexMixin {
	@ModifyVariable(method = "write", at = @At("HEAD"), argsOnly = true, ordinal = 0)
	private static int enchancement$rage(int color) {
		int rageColor = RageEffect.color;
		if (rageColor != -1) {
			if (color != -1) {
				rageColor *= color;
			}
			return Integer.reverseBytes(rageColor << 8 | 0xFF);
		}
		return color;
	}
}
