/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.rebalanceenchantments;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moriyashiine.enchancement.common.entity.WindBurstHolder;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {
	@WrapOperation(method = "onExplodedBy", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;setIgnoreFallDamageFromCurrentExplosion(Z)V"))
	private void enchancement$rebalanceEnchantments(ServerPlayerEntity instance, boolean ignoreFallDamage, Operation<Void> original, Entity entity) {
		original.call(instance, ignoreFallDamage && (!(entity instanceof WindBurstHolder windBurstHolder) || !windBurstHolder.enchancement$fromWindBurst()));
	}
}
