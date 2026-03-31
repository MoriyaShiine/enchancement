/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.enhancemobs;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.illager.AbstractIllager;
import net.minecraft.world.entity.monster.illager.Pillager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.providers.EnchantmentProvider;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Pillager.class)
public abstract class PillagerMixin extends AbstractIllager {
	protected PillagerMixin(EntityType<? extends AbstractIllager> type, Level level) {
		super(type, level);
	}

	@Inject(method = "applyRaidBuffs", at = @At("HEAD"), cancellable = true)
	private void enchancement$enhanceMobs(ServerLevel level, int wave, boolean isCaptain, CallbackInfo ci) {
		if (ModConfig.enhanceMobs) {
			if (getRandom().nextFloat() <= getCurrentRaid().getEnchantOdds()) {
				ItemStack stack = new ItemStack(Items.CROSSBOW);
				@Nullable Holder<Enchantment> randomEnchantment = EnchancementUtil.getRandomEnchantment(stack, EnchantmentTags.ON_MOB_SPAWN_EQUIPMENT, getRandom());
				if (randomEnchantment != null) {
					stack.enchant(randomEnchantment, randomEnchantment.value().getMaxLevel());
				}
				setItemSlot(EquipmentSlot.MAINHAND, stack);
			}
			ci.cancel();
		}
	}

	@WrapOperation(method = "enchantSpawnedWeapon", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;enchantItemFromProvider(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/core/RegistryAccess;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/DifficultyInstance;Lnet/minecraft/util/RandomSource;)V"))
	private void enchancement$enhanceMobs(ItemStack itemStack, RegistryAccess registryAccess, ResourceKey<EnchantmentProvider> providerKey, DifficultyInstance difficulty, RandomSource random, Operation<Void> original) {
		if (ModConfig.enhanceMobs) {
			@Nullable Holder<Enchantment> randomEnchantment = EnchancementUtil.getRandomEnchantment(itemStack, EnchantmentTags.ON_MOB_SPAWN_EQUIPMENT, random);
			if (randomEnchantment != null) {
				itemStack.enchant(randomEnchantment, randomEnchantment.value().getMaxLevel());
				return;
			}
		}
		original.call(itemStack, registryAccess, providerKey, difficulty, random);
	}
}
