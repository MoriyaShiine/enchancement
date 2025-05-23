/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.entityxray;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
	@ModifyReturnValue(method = "getAttributeValue", at = @At("RETURN"))
	private double enchancement$entityXray(double original, RegistryEntry<EntityAttribute> attribute) {
		if (attribute == EntityAttributes.FOLLOW_RANGE) {
			original += EnchancementUtil.getValue(ModEnchantmentEffectComponentTypes.ENTITY_XRAY, (LivingEntity) (Object) this, 0);
		}
		return original;
	}
}
