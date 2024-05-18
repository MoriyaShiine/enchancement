/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.scatter;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CrossbowItem.class)
public class CrossbowItemMixin {
	@WrapWithCondition(method = "shoot", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;playSound(Lnet/minecraft/entity/player/PlayerEntity;DDDLnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FF)V"))
	private boolean enchancement$scatterPlaySound(World instance, PlayerEntity except, double x, double y, double z, SoundEvent sound, SoundCategory category, float volume, float pitch) {
		return !EnchancementUtil.hasScatterShot;
	}
}
