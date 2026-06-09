/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.rapidcrossbowfire;

import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ChargedProjectiles;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStack.class)
public class ItemStackMixin {
	@Inject(method = "inventoryTick", at = @At("TAIL"))
	private void enchancement$rapidCrossbowFire(Level level, Entity owner, EquipmentSlot slot, CallbackInfo ci) {
		ItemStack stack = (ItemStack) (Object) this;
		if (stack.getItem() instanceof CrossbowItem && owner instanceof LivingEntity living && living.getUseItem() != stack && EnchantmentHelper.has(stack, ModEnchantmentEffectComponentTypes.RAPID_CROSSBOW_FIRE)) {
			ChargedProjectiles chargedProjectiles = stack.get(DataComponents.CHARGED_PROJECTILES);
			if (chargedProjectiles != null && !chargedProjectiles.isEmpty()) {
				stack.set(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.EMPTY);
			}
		}
	}
}
