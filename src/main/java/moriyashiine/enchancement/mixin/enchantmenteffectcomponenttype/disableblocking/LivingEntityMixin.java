/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.disableblocking;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	public LivingEntityMixin(EntityType<?> type, Level level) {
		super(type, level);
	}

	@ModifyReturnValue(method = "getSecondsToDisableBlocking", at = @At("RETURN"))
	private float enchancement$weaponEffectCooldownRequirement(float original, @Local(name = "weaponItem") ItemStack weaponItem) {
		if (EnchancementUtil.shouldApplyWeaponEffect() && level() instanceof ServerLevel level) {
			return original + EnchancementUtil.getValue(ModEnchantmentEffectComponentTypes.DISABLE_BLOCKING, level, weaponItem, 0);
		}
		return original;
	}
}
