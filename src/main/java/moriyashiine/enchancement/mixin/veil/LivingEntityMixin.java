/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.veil;

import moriyashiine.enchancement.common.init.ModEnchantments;
import moriyashiine.enchancement.common.init.ModTags;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
	@ModifyVariable(method = "getAttackDistanceScalingFactor", at = @At(value = "STORE", ordinal = 0))
	private double enchancement$veil(double value, Entity entity) {
		if (entity != null && entity.getType().isIn(ModTags.EntityTypes.VEIL_IMMUNE)) {
			return value;
		}
		int level = EnchantmentHelper.getEquipmentLevel(ModEnchantments.VEIL, (LivingEntity) (Object) this);
		if (level > 0) {
			return value / (level * 2);
		}
		return value;
	}
}
