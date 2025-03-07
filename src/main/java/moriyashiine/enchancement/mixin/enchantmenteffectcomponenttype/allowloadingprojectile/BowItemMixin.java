/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.allowloadingprojectile;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moriyashiine.enchancement.common.enchantment.effect.AllowLoadingProjectileEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BowItem;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BowItem.class)
public class BowItemMixin {
	@WrapOperation(method = "onStoppedUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;playSound(Lnet/minecraft/entity/player/PlayerEntity;DDDLnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FF)V"))
	private void enchancement$allowLoadingProjectile(World instance, PlayerEntity source, double x, double y, double z, SoundEvent sound, SoundCategory category, float volume, float pitch, Operation<Void> original) {
		if (AllowLoadingProjectileEffect.cachedSoundEvent != null) {
			sound = AllowLoadingProjectileEffect.cachedSoundEvent;
			AllowLoadingProjectileEffect.cachedSoundEvent = null;
		}
		original.call(instance, source, x, y, z, sound, category, volume, pitch);
	}
}
