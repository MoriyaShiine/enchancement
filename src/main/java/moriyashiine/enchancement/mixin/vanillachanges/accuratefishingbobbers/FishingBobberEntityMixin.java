/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.vanillachanges.accuratefishingbobbers;

import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.entity.projectile.GrappleFishingBobberEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FishingBobberEntity.class)
public class FishingBobberEntityMixin {
	@Inject(method = "<init>(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/world/World;II)V", at = @At("TAIL"))
	private void enchancement$accurateFishingBobbers(PlayerEntity thrower, World world, int luckOfTheSeaLevel, int lureLevel, CallbackInfo ci) {
		if (ModConfig.accurateFishingBobbers) {
			GrappleFishingBobberEntity.accurateFishingBobbers((FishingBobberEntity) (Object) this, thrower, 1.25F);
		}
	}
}
