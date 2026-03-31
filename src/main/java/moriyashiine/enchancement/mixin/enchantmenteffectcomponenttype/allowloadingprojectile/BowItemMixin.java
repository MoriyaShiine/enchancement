/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.allowloadingprojectile;

import moriyashiine.enchancement.common.world.item.effects.AllowLoadingProjectileEffect;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.BowItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(BowItem.class)
public class BowItemMixin {
	@ModifyArg(method = "releaseUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;playSound(Lnet/minecraft/world/entity/Entity;DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V"))
	private SoundEvent enchancement$allowLoadingProjectile(SoundEvent sound) {
		if (AllowLoadingProjectileEffect.cachedSoundEvent != null) {
			sound = AllowLoadingProjectileEffect.cachedSoundEvent;
			AllowLoadingProjectileEffect.cachedSoundEvent = null;
		}
		return sound;
	}
}
