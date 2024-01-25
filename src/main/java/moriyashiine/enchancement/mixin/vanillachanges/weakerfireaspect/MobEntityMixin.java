/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.vanillachanges.weakerfireaspect;

import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.entity.mob.MobEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(MobEntity.class)
public class MobEntityMixin {
	@ModifyArg(method = "tryAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;setOnFireFor(I)V"))
	private int enchancement$weakerFireAspect(int value) {
		if (ModConfig.weakerFireAspect && !ModConfig.cumulativeWeaponEnchantments) {
			return value * 3 / 4;
		}
		return value;
	}
}
