/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.toggleablepassives;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.init.ModComponentTypes;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.ItemTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
	@WrapOperation(method = "getBlockBreakingSpeed", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getAttributeValue(Lnet/minecraft/registry/entry/RegistryEntry;)D", ordinal = 0))
	private double enchancement$toggleablePassives(PlayerEntity instance, RegistryEntry<EntityAttribute> registryEntry, Operation<Double> original) {
		double speed = original.call(instance, registryEntry);
		if (ModConfig.toggleablePassives) {
			ItemStack stack = instance.getInventory().getStack(instance.getInventory().selectedSlot);
			if (stack.isIn(ItemTags.MINING_ENCHANTABLE) && stack.getOrDefault(ModComponentTypes.TOGGLEABLE_PASSIVE, false)) {
				if (!stack.hasEnchantments()) {
					stack.remove(ModComponentTypes.TOGGLEABLE_PASSIVE);
					return speed;
				}
				speed += Math.pow(EnchancementUtil.getModifiedMaxLevel(stack, 5), 2);
			}
		}
		return speed;
	}
}
