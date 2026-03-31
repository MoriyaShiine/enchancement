/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.entityxray;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
	@ModifyReturnValue(method = "getAttributeValue", at = @At("RETURN"))
	private double enchancement$entityXray(double original, Holder<Attribute> attribute) {
		if (attribute == Attributes.FOLLOW_RANGE) {
			original += EnchancementUtil.getValue(ModEnchantmentEffectComponentTypes.ENTITY_XRAY, (LivingEntity) (Object) this, 0);
		}
		return original;
	}
}
