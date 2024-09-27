/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.vanillachanges.enchantedtoolshaveefficiency;

import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.init.ModComponentTypes;
import moriyashiine.enchancement.common.tag.ModEnchantmentTags;
import net.minecraft.component.ComponentHolder;
import net.minecraft.component.ComponentType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements ComponentHolder {
	@Shadow
	public abstract boolean hasEnchantments();

	@Shadow
	public abstract boolean isIn(TagKey<Item> tag);

	@Shadow
	@Nullable
	public abstract <T> T set(ComponentType<? super T> type, @Nullable T value);

	@Inject(method = "addEnchantment", at = @At("TAIL"))
	private void enchancement$enchantedToolsHaveEfficiency(RegistryEntry<Enchantment> enchantment, int level, CallbackInfo ci) {
		if (ModConfig.enchantedToolsHaveEfficiency && hasEnchantments() && isIn(ItemTags.MINING_ENCHANTABLE) && !enchantment.isIn(ModEnchantmentTags.DISALLOWS_TOGGLEABLE_PASSIVE) && !contains(ModComponentTypes.TOGGLEABLE_PASSIVE)) {
			set(ModComponentTypes.TOGGLEABLE_PASSIVE, true);
		}
	}
}
