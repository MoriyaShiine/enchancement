/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.event.enchantmenteffectcomponenttype;

import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.strawberrylib.api.event.ModifyStackDamageEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class ArmorDentingEvent implements ModifyStackDamageEvent {
	public static void init() {
		ModifyStackDamageEvent.ADD.register(new ArmorDentingEvent());
	}

	@Override
	public float modify(ServerLevel level, ItemStack stack, Entity victim, DamageSource source, float damage) {
		if (victim instanceof LivingEntity living) {
			float modifier = EnchancementUtil.getValue(ModEnchantmentEffectComponentTypes.ARMOR_DENTING, level, stack, 0);
			if (modifier != 0) {
				return living.getArmorValue() * modifier;
			}
		}
		return 0;
	}
}
