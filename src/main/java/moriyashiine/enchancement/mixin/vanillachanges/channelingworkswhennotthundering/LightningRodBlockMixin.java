/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.enchancement.mixin.vanillachanges.channelingworkswhennotthundering;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.block.LightningRodBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LightningRodBlock.class)
public class LightningRodBlockMixin {
	@ModifyExpressionValue(method = "onProjectileHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;isThundering()Z"))
	private boolean enchancement$channelingWorksWhenNotThundering(boolean value) {
		return value || ModConfig.channelingWorksWhenNotThundering;
	}
}
