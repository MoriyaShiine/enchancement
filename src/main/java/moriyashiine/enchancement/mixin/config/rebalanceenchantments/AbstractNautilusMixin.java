/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.rebalanceenchantments;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.nautilus.AbstractNautilus;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractNautilus.class)
public abstract class AbstractNautilusMixin extends LivingEntity {
	protected AbstractNautilusMixin(EntityType<? extends LivingEntity> type, Level level) {
		super(type, level);
	}

	@ModifyExpressionValue(method = "travelInWater", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/nautilus/AbstractNautilus;getSpeed()F"))
	private float enchancement$rebalanceEnchantments(float original) {
		if (ModConfig.rebalanceEnchantments) {
			original *= (float) (1 + getAttributeValue(Attributes.WATER_MOVEMENT_EFFICIENCY));
		}
		return original;
	}
}
