package moriyashiine.enchancement.mixin.gale;

import moriyashiine.enchancement.common.registry.ModEnchantments;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
	@Shadow
	public abstract ItemStack getEquippedStack(EquipmentSlot var1);

	@ModifyVariable(method = "handleFallDamage", at = @At("HEAD"), ordinal = 0, argsOnly = true)
	private float enchancement$galeSafeFall(float value) {
		if (EnchantmentHelper.getLevel(ModEnchantments.GALE, getEquippedStack(EquipmentSlot.FEET)) > 0) {
			value = Math.max(0, value - 2);
		}
		return value;
	}

	@ModifyVariable(method = "jump", at = @At("STORE"))
	private double enchancement$galeBoostJumpHeight(double value) {
		if (EnchantmentHelper.getLevel(ModEnchantments.GALE, getEquippedStack(EquipmentSlot.FEET)) > 0) {
			value *= 1.5;
		}
		return value;
	}
}
