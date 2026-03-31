/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.enhancemobs;

import com.llamalad7.mixinextras.sugar.Local;
import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.core.Holder;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mob.class)
public class MobMixin {
	@Inject(method = "enchantSpawnedEquipment(Lnet/minecraft/world/level/ServerLevelAccessor;Lnet/minecraft/world/entity/EquipmentSlot;Lnet/minecraft/util/RandomSource;FLnet/minecraft/world/DifficultyInstance;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Mob;setItemSlot(Lnet/minecraft/world/entity/EquipmentSlot;Lnet/minecraft/world/item/ItemStack;)V"))
	private void enchancement$enhanceMobs(ServerLevelAccessor level, EquipmentSlot slot, RandomSource random, float chance, DifficultyInstance difficulty, CallbackInfo ci, @Local(name = "itemStack") ItemStack itemStack) {
		if (ModConfig.enhanceMobs) {
			@Nullable Holder<Enchantment> randomEnchantment = EnchancementUtil.getRandomEnchantment(itemStack, EnchantmentTags.ON_MOB_SPAWN_EQUIPMENT, random);
			if (randomEnchantment != null) {
				itemStack.enchant(randomEnchantment, randomEnchantment.value().getMaxLevel());
			}
		}
	}
}
