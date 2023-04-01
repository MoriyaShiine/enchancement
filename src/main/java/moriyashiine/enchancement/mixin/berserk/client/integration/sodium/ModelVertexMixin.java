/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.berserk.client.integration.sodium;

import me.jellysquid.mods.sodium.client.render.vertex.formats.ModelVertex;
import moriyashiine.enchancement.client.util.EnchancementClientUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Environment(EnvType.CLIENT)
@Mixin(ModelVertex.class)
public class ModelVertexMixin {
	@ModifyVariable(method = "writeQuadVertices", at = @At("HEAD"), ordinal = 2, argsOnly = true)
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
