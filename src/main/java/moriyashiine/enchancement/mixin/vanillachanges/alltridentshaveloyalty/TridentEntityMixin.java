/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.enchancement.mixin.vanillachanges.alltridentshaveloyalty;

import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(TridentEntity.class)
public abstract class TridentEntityMixin extends PersistentProjectileEntity {
	protected TridentEntityMixin(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
		super(entityType, world);
	}

	@ModifyVariable(method = "tick", at = @At("STORE"))
	private int enchancement$disableLoyaltyOnNonPlayerTridents(int value) {
		if (ModConfig.allTridentsHaveLoyalty && !(getOwner() instanceof PlayerEntity)) {
			return 0;
		}
		return value;
	}

	@ModifyVariable(method = "age", at = @At("STORE"))
	private int enchancement$ageNonPlayerTridents(int value) {
		if (ModConfig.allTridentsHaveLoyalty && !(getOwner() instanceof PlayerEntity)) {
			return 0;
		}
		return value;
	}
}
