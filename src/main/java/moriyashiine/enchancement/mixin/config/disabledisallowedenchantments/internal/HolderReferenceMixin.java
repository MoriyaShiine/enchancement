/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.disabledisallowedenchantments.internal;

import moriyashiine.enchancement.common.init.ModEnchantments;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
import net.minecraft.resources.ResourceKey;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("unchecked")
@Mixin(Holder.Reference.class)
public abstract class HolderReferenceMixin<T> {
	@Shadow
	private @Nullable ResourceKey<T> key;

	@Shadow
	private @Nullable T value;

	@Shadow
	public abstract boolean canSerializeIn(HolderOwner<T> context);

	@Inject(method = "isBound", at = @At("HEAD"))
	private void enchancement$disableDisallowedEnchantments(CallbackInfoReturnable<Boolean> cir) {
		validate();
	}

	@Inject(method = "key", at = @At("HEAD"))
	private void enchancement$disableDisallowedEnchantmentsKey(CallbackInfoReturnable<ResourceKey<T>> cir) {
		validate();
	}

	@Inject(method = "value", at = @At("HEAD"))
	private void enchancement$disableDisallowedEnchantmentsValue(CallbackInfoReturnable<T> cir) {
		validate();
	}

	@Unique
	private void validate() {
		if (EnchancementUtil.ENCHANTMENT_HOLDER_OWNER != null && canSerializeIn((HolderOwner<T>) EnchancementUtil.ENCHANTMENT_HOLDER_OWNER)) {
			if (key == null || value == null) {
				key = (ResourceKey<T>) ModEnchantments.EMPTY_KEY;
				value = (T) ModEnchantments.EMPTY;
			}
		}
	}
}
