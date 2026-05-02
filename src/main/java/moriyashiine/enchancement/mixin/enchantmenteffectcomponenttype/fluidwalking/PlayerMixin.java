/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.fluidwalking;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.strawberrylib.api.module.SLibUtils;
import moriyashiine.strawberrylib.api.objects.enums.SubmersionGate;
import net.minecraft.core.BlockPos;
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
	private boolean enchancement$fluidWalking(boolean original) {
		if (original && getVehicle() instanceof LivingEntity living && EnchancementUtil.hasAnyEnchantmentsWith(living, ModEnchantmentEffectComponentTypes.FLUID_WALKING) && level().getBlockState(BlockPos.containing(living.getEyePosition())).isAir() && SLibUtils.isSubmerged(living, SubmersionGate.ALL)) {
			return false;
		}
		return original;
	}
}
