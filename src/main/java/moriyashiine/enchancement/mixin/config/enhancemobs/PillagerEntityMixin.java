/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.enhancemobs;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.provider.EnchantmentProvider;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.IllagerEntity;
import net.minecraft.entity.mob.PillagerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PillagerEntity.class)
public abstract class PillagerEntityMixin extends IllagerEntity {
	protected PillagerEntityMixin(EntityType<? extends IllagerEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "addBonusForWave", at = @At("HEAD"), cancellable = true)
	private void enchancement$enhanceMobs(ServerWorld world, int wave, boolean unused, CallbackInfo ci) {
		if (ModConfig.enhanceMobs) {
			if (getRandom().nextFloat() <= getRaid().getEnchantmentChance()) {
				ItemStack stack = new ItemStack(Items.CROSSBOW);
				@Nullable RegistryEntry<Enchantment> enchantment = EnchancementUtil.getRandomEnchantment(stack, getRandom());
				if (enchantment != null) {
					stack.addEnchantment(enchantment, enchantment.value().getMaxLevel());
				}
				equipStack(EquipmentSlot.MAINHAND, stack);
			}
			ci.cancel();
		}
	}

	@WrapOperation(method = "enchantMainHandItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentHelper;applyEnchantmentProvider(Lnet/minecraft/item/ItemStack;Lnet/minecraft/registry/DynamicRegistryManager;Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/world/LocalDifficulty;Lnet/minecraft/util/math/random/Random;)V"))
	private void enchancement$enhanceMobs(ItemStack stack, DynamicRegistryManager registryManager, RegistryKey<EnchantmentProvider> providerKey, LocalDifficulty localDifficulty, Random random, Operation<Void> original) {
		if (ModConfig.enhanceMobs) {
			@Nullable RegistryEntry<Enchantment> enchantment = EnchancementUtil.getRandomEnchantment(stack, getRandom());
			if (enchantment != null) {
				stack.addEnchantment(enchantment, enchantment.value().getMaxLevel());
				return;
			}
		}
		original.call(stack, registryManager, providerKey, localDifficulty, random);
	}
}
