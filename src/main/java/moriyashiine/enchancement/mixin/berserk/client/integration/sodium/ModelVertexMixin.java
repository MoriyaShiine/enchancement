/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.berserk.client.integration.sodium;

import moriyashiine.enchancement.client.util.EnchancementClientUtil;
import net.caffeinemc.mods.sodium.api.vertex.format.common.ModelVertex;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ModelVertex.class)
public class ModelVertexMixin {
	@ModifyVariable(method = "write", at = @At("HEAD"), ordinal = 0, argsOnly = true)
	private static int enchancement$berserk(int value) {
		int color = EnchancementClientUtil.berserkColor;
		if (color != -1) {
			if (value != -1) {
				color *= value;
			}
			return Integer.reverseBytes(color << 8 | 0xFF);
		}
		return value;
	}
}
