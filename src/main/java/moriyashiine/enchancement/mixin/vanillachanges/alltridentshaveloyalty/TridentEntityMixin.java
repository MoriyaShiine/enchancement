/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.vanillachanges.alltridentshaveloyalty;

import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.entity.EntityType;
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
		return EnchancementUtil.shouldDisableLoyalty(this) ? 0 : value;
	}

	@ModifyVariable(method = "age", at = @At("STORE"))
	private int enchancement$ageNonPlayerTridents(int value) {
		return EnchancementUtil.shouldDisableLoyalty(this) ? 0 : value;
	}
}
