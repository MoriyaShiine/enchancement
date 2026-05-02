/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.rotationburst;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {
	protected PlayerMixin(EntityType<? extends LivingEntity> type, Level level) {
		super(type, level);
	}

	@ModifyReturnValue(method = "wantsToStopRiding", at = @At("RETURN"))
	private boolean enchancement$rotationBurst(boolean original) {
		if (original && getVehicle() instanceof LivingEntity living && living.getKnownMovement().length() > 0 && ModEntityComponents.ROTATION_BURST.get(living).hasEffect()) {
			return false;
		}
		return original;
	}
}
