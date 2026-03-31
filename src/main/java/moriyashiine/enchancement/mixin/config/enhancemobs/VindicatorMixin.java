/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.enhancemobs;

import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.illager.AbstractIllager;
import net.minecraft.world.entity.monster.illager.Vindicator;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Vindicator.class)
public abstract class VindicatorMixin extends AbstractIllager {
	protected VindicatorMixin(EntityType<? extends AbstractIllager> type, Level level) {
		super(type, level);
	}

	@Inject(method = "applyRaidBuffs", at = @At("HEAD"), cancellable = true)
	private void enchancement$enhanceMobs(ServerLevel level, int wave, boolean isCaptain, CallbackInfo ci) {
		if (ModConfig.enhanceMobs) {
			ItemStack stack = new ItemStack(Items.IRON_AXE);
			if (getRandom().nextFloat() <= getCurrentRaid().getEnchantOdds()) {
				@Nullable Holder<Enchantment> randomEnchantment = EnchancementUtil.getRandomEnchantment(stack, EnchantmentTags.ON_MOB_SPAWN_EQUIPMENT, getRandom());
				if (randomEnchantment != null) {
					stack.enchant(randomEnchantment, randomEnchantment.value().getMaxLevel());
				}
			}
			setItemSlot(EquipmentSlot.MAINHAND, stack);
			ci.cancel();
		}
	}
}
