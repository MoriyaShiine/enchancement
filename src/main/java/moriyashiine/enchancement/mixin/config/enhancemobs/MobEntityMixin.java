/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.enhancemobs;

import com.llamalad7.mixinextras.sugar.Local;
import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.EnchantmentTags;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MobEntity.class)
public class MobEntityMixin {
	@Inject(method = "enchantEquipment(Lnet/minecraft/world/ServerWorldAccess;Lnet/minecraft/entity/EquipmentSlot;Lnet/minecraft/util/math/random/Random;FLnet/minecraft/world/LocalDifficulty;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/MobEntity;equipStack(Lnet/minecraft/entity/EquipmentSlot;Lnet/minecraft/item/ItemStack;)V", shift = At.Shift.AFTER))
	private void enchancement$enhanceMobs(ServerWorldAccess world, EquipmentSlot slot, Random random, float power, LocalDifficulty localDifficulty, CallbackInfo ci, @Local ItemStack stack) {
		if (ModConfig.enhanceMobs) {
			RegistryEntry<Enchantment> randomEnchantment = EnchancementUtil.getRandomEnchantment(stack, EnchantmentTags.ON_MOB_SPAWN_EQUIPMENT, random);
			if (randomEnchantment != null) {
				stack.addEnchantment(randomEnchantment, randomEnchantment.value().getMaxLevel());
			}
		}
	}
}
