/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.disabledisallowedenchantments.ingame;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.entry.RegistryEntry;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEnchantmentsComponent.Builder.class)
public abstract class ItemEnchantmentsComponentBuilderMixin {
	@Shadow
	public abstract void set(RegistryEntry<Enchantment> enchantment, int level);

	@Shadow
	public abstract void add(RegistryEntry<Enchantment> enchantment, int level);

	@Inject(method = "set", at = @At("HEAD"), cancellable = true)
	private void enchancement$disableDisallowedEnchantmentsSet(RegistryEntry<Enchantment> enchantment, int level, CallbackInfo ci) {
		if (enchantment == null) {
			Enchancement.LOGGER.warn("Attempted to set a null enchantment");
			ci.cancel();
		} else if (!EnchancementUtil.isEnchantmentAllowed(enchantment)) {
			if (EnchancementUtil.cachedApplyStack != null) {
				@Nullable RegistryEntry<Enchantment> replacement = EnchancementUtil.getReplacement(enchantment, EnchancementUtil.cachedApplyStack);
				if (replacement != null) {
					set(replacement, Math.min(level, replacement.value().getMaxLevel()));
					ci.cancel();
					return;
				}
			}
			Enchancement.LOGGER.warn("Attempted to set a disabled enchantment {}", EnchancementUtil.getTranslationKey(enchantment));
			ci.cancel();
		}
	}

	@Inject(method = "add", at = @At("HEAD"), cancellable = true)
	private void enchancement$disableDisallowedEnchantmentsAdd(RegistryEntry<Enchantment> enchantment, int level, CallbackInfo ci) {
		if (enchantment == null) {
			Enchancement.LOGGER.warn("Attempted to add a null enchantment");
			ci.cancel();
		} else if (!EnchancementUtil.isEnchantmentAllowed(enchantment)) {
			if (EnchancementUtil.cachedApplyStack != null) {
				@Nullable RegistryEntry<Enchantment> replacement = EnchancementUtil.getReplacement(enchantment, EnchancementUtil.cachedApplyStack);
				if (replacement != null) {
					add(replacement, Math.min(level, replacement.value().getMaxLevel()));
					ci.cancel();
					return;
				}
			}
			Enchancement.LOGGER.warn("Attempted to add a disabled enchantment {}", EnchancementUtil.getTranslationKey(enchantment));
			ci.cancel();
		}
	}
}
