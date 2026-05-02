/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.enhancemobs;

import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.monster.illager.Pillager;
import net.minecraft.world.entity.monster.illager.Vindicator;
import net.minecraft.world.item.enchantment.providers.EnchantmentProvider;
import net.minecraft.world.item.enchantment.providers.VanillaEnchantmentProviders;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin({Pillager.class, Vindicator.class})
public class RaidBuffMixin {
	@ModifyArg(method = "applyRaidBuffs", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;enchantItemFromProvider(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/core/RegistryAccess;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/DifficultyInstance;Lnet/minecraft/util/RandomSource;)V"), index = 2)
	private ResourceKey<EnchantmentProvider> enchancement$enhanceMobs(ResourceKey<EnchantmentProvider> providerKey) {
		return ModConfig.enhanceMobs ? VanillaEnchantmentProviders.MOB_SPAWN_EQUIPMENT : providerKey;
	}
}
