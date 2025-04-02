/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.enhancemobs;

import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.IllagerEntity;
import net.minecraft.entity.mob.VindicatorEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.EnchantmentTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VindicatorEntity.class)
public abstract class VindicatorEntityMixin extends IllagerEntity {
	protected VindicatorEntityMixin(EntityType<? extends IllagerEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "addBonusForWave", at = @At("HEAD"), cancellable = true)
	private void enchancement$enhanceMobs(ServerWorld world, int wave, boolean unused, CallbackInfo ci) {
		if (ModConfig.enhanceMobs) {
			ItemStack stack = new ItemStack(Items.IRON_AXE);
			if (getRandom().nextFloat() <= getRaid().getEnchantmentChance()) {
				@Nullable RegistryEntry<Enchantment> randomEnchantment = EnchancementUtil.getRandomEnchantment(stack, EnchantmentTags.ON_MOB_SPAWN_EQUIPMENT, getRandom());
				if (randomEnchantment != null) {
					stack.addEnchantment(randomEnchantment, randomEnchantment.value().getMaxLevel());
				}
			}
			equipStack(EquipmentSlot.MAINHAND, stack);
			ci.cancel();
		}
	}
}
