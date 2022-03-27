package moriyashiine.enchancement.mixin.gale;

import moriyashiine.enchancement.common.registry.ModEnchantments;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
	@ModifyVariable(method = "handleFallDamage", at = @At("HEAD"), ordinal = 0, argsOnly = true)
	private float enchancement$gale(float value) {
		if (EnchantmentHelper.getEquipmentLevel(ModEnchantments.GALE, LivingEntity.class.cast(this)) > 0) {
			value = Math.max(0, value - 2);
		}
		return value;
	}

	@ModifyVariable(method = "jump", at = @At("STORE"))
	private double enchancement$gale(double value) {
		if (EnchantmentHelper.getEquipmentLevel(ModEnchantments.GALE, LivingEntity.class.cast(this)) > 0) {
			value *= 1.5;
		}
		return value;
	}
}
