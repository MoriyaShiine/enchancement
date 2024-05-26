/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.buoy;

import moriyashiine.enchancement.common.init.ModEnchantments;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntityMixin {
	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@ModifyArg(method = "updatePose", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;setPose(Lnet/minecraft/entity/EntityPose;)V"))
	private EntityPose enchancement$buoy(EntityPose value) {
		if (value == EntityPose.SWIMMING && isTouchingWater() && EnchancementUtil.hasEnchantment(ModEnchantments.BUOY, this)) {
			return EntityPose.STANDING;
		}
		return value;
	}

	@Override
	protected boolean enchancement$buoy(boolean original) {
		return super.enchancement$buoy(original) || (!isSneaking() && EnchancementUtil.hasEnchantment(ModEnchantments.BUOY, this));
	}
}
