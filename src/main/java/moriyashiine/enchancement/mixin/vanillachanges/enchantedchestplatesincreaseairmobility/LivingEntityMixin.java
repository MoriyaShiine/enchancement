/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.vanillachanges.enchantedchestplatesincreaseairmobility;

import moriyashiine.enchancement.common.component.entity.AirMobilityComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@ModifyArg(method = "applyMovementInput", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;updateVelocity(FLnet/minecraft/util/math/Vec3d;)V"))
	private float enchancement$enchantedChestplatesIncreaseAirMobility(float value) {
		if (!isOnGround()) {
			AirMobilityComponent airMobilityComponent = ModEntityComponents.AIR_MOBILITY.getNullable(this);
			if (airMobilityComponent != null && airMobilityComponent.getTicksInAir() > 10) {
				return value * 2;
			}
		}
		return value;
	}
}
