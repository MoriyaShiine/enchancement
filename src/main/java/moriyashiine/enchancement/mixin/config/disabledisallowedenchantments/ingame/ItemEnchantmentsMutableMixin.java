/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.disabledisallowedenchantments.ingame;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.core.Holder;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEnchantments.Mutable.class)
public abstract class ItemEnchantmentsMutableMixin {
	@Shadow
	public abstract void set(Holder<Enchantment> enchantment, int level);

	@Shadow
	public abstract void upgrade(Holder<Enchantment> enchantment, int level);

	@Inject(method = "set", at = @At("HEAD"), cancellable = true)
	private void enchancement$disableDisallowedEnchantmentsSet(Holder<Enchantment> enchantment, int level, CallbackInfo ci) {
		if (enchantment == null) {
			Enchancement.LOGGER.warn("Attempted to set a null enchantment");
			ci.cancel();
		} else if (!EnchancementUtil.isEnchantmentAllowed(enchantment)) {
			if (EnchancementUtil.cachedApplyStack != null) {
				@Nullable Holder<Enchantment> randomEnchantment = EnchancementUtil.getRandomEnchantment(EnchancementUtil.cachedApplyStack, EnchantmentTags.ON_RANDOM_LOOT, null);
				if (randomEnchantment != null) {
					set(randomEnchantment, Math.min(level, randomEnchantment.value().getMaxLevel()));
					ci.cancel();
					return;
				}
			}
			Enchancement.LOGGER.warn("Attempted to set a disabled enchantment {}", EnchancementUtil.getTranslationKey(enchantment));
			ci.cancel();
		}
	}

	@Inject(method = "upgrade", at = @At("HEAD"), cancellable = true)
	private void enchancement$disableDisallowedEnchantmentsAdd(Holder<Enchantment> enchantment, int level, CallbackInfo ci) {
		if (enchantment == null) {
			Enchancement.LOGGER.warn("Attempted to add a null enchantment");
			ci.cancel();
		} else if (!EnchancementUtil.isEnchantmentAllowed(enchantment)) {
			if (EnchancementUtil.cachedApplyStack != null) {
				@Nullable Holder<Enchantment> randomEnchantment = EnchancementUtil.getRandomEnchantment(EnchancementUtil.cachedApplyStack, EnchantmentTags.ON_RANDOM_LOOT, null);
				if (randomEnchantment != null) {
					upgrade(randomEnchantment, Math.min(level, randomEnchantment.value().getMaxLevel()));
					ci.cancel();
					return;
				}
			}
			Enchancement.LOGGER.warn("Attempted to add a disabled enchantment {}", EnchancementUtil.getTranslationKey(enchantment));
			ci.cancel();
		}
	}
}
