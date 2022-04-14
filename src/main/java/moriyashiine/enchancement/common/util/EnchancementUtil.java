package moriyashiine.enchancement.common.util;

import moriyashiine.enchancement.common.registry.ModEnchantments;
import moriyashiine.enchancement.mixin.util.ItemEntityAccessor;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import java.util.List;

public class EnchancementUtil {
	public static List<ItemEntity> mergeItemEntities(List<ItemEntity> drops) {
		for (int i = drops.size() - 1; i >= 0; i--) {
			if (i < drops.size() - 1) {
				ItemEntity itemEntity = drops.get(i);
				ItemEntity other = drops.get(i + 1);
				((ItemEntityAccessor) itemEntity).enchancement$tryMerge(other);
				if (itemEntity.getStack().isEmpty()) {
					drops.remove(i);
				}
				if (other.getStack().isEmpty()) {
					drops.remove(i + 1);
				}
			}
		}
		return drops;
	}

	public static boolean isGroundedOrJumping(LivingEntity living) {
		if (living instanceof PlayerEntity player && player.getAbilities().flying) {
			return false;
		}
		return !living.isTouchingWater() && !living.isSwimming();
	}

	public static float getMaxBonusBerserkDamage(ItemStack stack) {
		float maxBonus = 1;
		for (EntityAttributeModifier modifier : stack.getAttributeModifiers(EquipmentSlot.MAINHAND).get(EntityAttributes.GENERIC_ATTACK_DAMAGE)) {
			maxBonus += modifier.getValue();
		}
		return maxBonus;
	}

	public static float getBonusBerserkDamage(LivingEntity living, ItemStack stack) {
		if (living != null && EnchantmentHelper.getLevel(ModEnchantments.BERSERK, stack) > 0) {
			float health = living.getMaxHealth();
			float bonus = 0;
			while (health > (int) living.getHealth()) {
				health -= 1;
				bonus += 0.5F;
			}
			return Math.min(bonus, getMaxBonusBerserkDamage(stack));
		}
		return 0;
	}
}
