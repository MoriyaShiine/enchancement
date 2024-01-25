/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.perception.client;

import moriyashiine.enchancement.common.init.ModEnchantments;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LightmapTextureManager.class)
public class LightmapTextureManagerMixin {
	@Shadow
	@Final
	private MinecraftClient client;

	@ModifyVariable(method = "update", at = @At("STORE"), ordinal = 6)
	private float enchancement$perception(float value) {
		int level = EnchantmentHelper.getEquipmentLevel(ModEnchantments.PERCEPTION, client.player);
		if (level > 0) {
			float strength = 1;
			if (level == 1) {
				strength = 0.25F;
			}
			return Math.max(strength, value);
		}
		return value;
	}
}
