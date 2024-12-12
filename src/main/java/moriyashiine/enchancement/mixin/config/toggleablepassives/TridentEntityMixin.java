/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.toggleablepassives;

import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.tag.ModEntityTypeTags;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(TridentEntity.class)
public abstract class TridentEntityMixin extends PersistentProjectileEntity {
	protected TridentEntityMixin(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
		super(entityType, world);
	}

	@ModifyVariable(method = "tick", at = @At("STORE"))
	private int enchancement$disableLoyaltyOnNonPlayerTridents(int value) {
		return shouldDisableLoyalty(this) ? 0 : value;
	}

	@ModifyVariable(method = "age", at = @At("STORE"))
	private int enchancement$ageNonPlayerTridents(int value) {
		return shouldDisableLoyalty(this) ? 0 : value;
	}

	@Unique
	private static boolean shouldDisableLoyalty(PersistentProjectileEntity entity) {
		if (ModConfig.toggleablePassives) {
			if (entity.getType().isIn(ModEntityTypeTags.NO_LOYALTY)) {
				return true;
			}
			return !(entity.getOwner() instanceof PlayerEntity);
		}
		return false;
	}
}
