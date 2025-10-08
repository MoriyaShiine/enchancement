/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.rebalanceequipment;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moriyashiine.enchancement.client.payload.SyncHookedVelocityPayload;
import moriyashiine.enchancement.common.ModConfig;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Set;

@Mixin(FishingBobberEntity.class)
public class FishingBobberEntityMixin {
	@WrapOperation(method = "pullHookedEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;setVelocity(Lnet/minecraft/util/math/Vec3d;)V"))
	private void enchancement$rebalanceEquipment(Entity instance, Vec3d velocity, Operation<Void> original) {
		if (ModConfig.rebalanceEquipment) {
			if (!instance.getEntityWorld().isClient()) {
				Set<Entity> entities = SyncHookedVelocityPayload.getEntities(instance);
				entities.forEach(entity -> original.call(entity, velocity));
				PlayerLookup.tracking(instance).forEach(foundPlayer -> SyncHookedVelocityPayload.send(foundPlayer, instance, velocity));
				if (instance instanceof ServerPlayerEntity player) {
					SyncHookedVelocityPayload.send(player, player, velocity);
				}
			}
		} else {
			original.call(instance, velocity);
		}
	}
}
