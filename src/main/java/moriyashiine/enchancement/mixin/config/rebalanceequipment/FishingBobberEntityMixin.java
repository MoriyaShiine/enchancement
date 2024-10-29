/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.rebalanceequipment;

import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FishingBobberEntity.class)
public abstract class FishingBobberEntityMixin extends ProjectileEntity {
	public FishingBobberEntityMixin(EntityType<? extends ProjectileEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "<init>(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/world/World;IILnet/minecraft/item/ItemStack;)V", at = @At("TAIL"))
	private void enchancement$rebalanceEquipment(PlayerEntity thrower, World world, int luckBonus, int waitTimeReductionTicks, ItemStack stack, CallbackInfo ci) {
		if (ModConfig.rebalanceEquipment) {
			float speed = EnchancementUtil.getValue(ModEnchantmentEffectComponentTypes.GRAPPLING_FISHING_BOBBER, (ServerWorld) world, stack, 0) != 0 ? 2.75F : 1.25F;
			refreshPositionAndAngles(thrower.getX(), thrower.getEyeY(), thrower.getZ(), thrower.getYaw(), thrower.getPitch());
			setVelocity(thrower, thrower.getPitch(), thrower.getYaw(), 0, speed, 0);
		}
	}
}
