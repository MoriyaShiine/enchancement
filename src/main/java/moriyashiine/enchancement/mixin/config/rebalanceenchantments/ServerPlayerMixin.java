/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.rebalanceenchantments;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moriyashiine.enchancement.common.world.entity.WindBurstHolder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {
	@WrapOperation(method = "onExplosionHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;setIgnoreFallDamageFromCurrentImpulse(ZLnet/minecraft/world/phys/Vec3;)V"))
	private void enchancement$rebalanceEnchantments(ServerPlayer instance, boolean b, Vec3 delta, Operation<Void> original, Entity explosionCausedBy) {
		original.call(instance, b && (!(explosionCausedBy instanceof WindBurstHolder windBurstHolder) || !windBurstHolder.enchancement$fromWindBurst()), delta);
	}
}
