/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.enchancement.mixin.vanillachanges.weakerfireaspect;

import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
	@ModifyArg(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;setOnFireFor(I)V", ordinal = 1))
	private int enchancement$weakerFireAspect(int value) {
		if (ModConfig.weakerFireAspect) {
			return value * 3 / 4;
		}
		return value;
	}
}
