/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.rebalanceequipment;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moriyashiine.enchancement.client.payload.SyncHookedMovementDeltaPayload;
import moriyashiine.enchancement.common.ModConfig;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Set;

@Mixin(FishingHook.class)
public class FishingHookMixin {
	@WrapOperation(method = "pullEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V"))
	private void enchancement$rebalanceEquipment(Entity instance, Vec3 deltaMovement, Operation<Void> original) {
		if (ModConfig.rebalanceEquipment) {
			if (!instance.level().isClientSide()) {
				Set<Entity> entities = SyncHookedMovementDeltaPayload.getEntities(instance);
				entities.forEach(entity -> original.call(entity, deltaMovement));
				PlayerLookup.tracking(instance).forEach(foundPlayer -> SyncHookedMovementDeltaPayload.send(foundPlayer, instance, deltaMovement));
				if (instance instanceof ServerPlayer player) {
					SyncHookedMovementDeltaPayload.send(player, player, deltaMovement);
				}
			}
		} else {
			original.call(instance, deltaMovement);
		}
	}
}
