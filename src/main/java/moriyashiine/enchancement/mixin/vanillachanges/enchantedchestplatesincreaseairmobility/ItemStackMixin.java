/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.vanillachanges.enchantedchestplatesincreaseairmobility;

import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.init.ModDataComponentTypes;
import moriyashiine.enchancement.common.tag.ModEnchantmentTags;
import net.minecraft.component.ComponentHolder;
import net.minecraft.component.DataComponentType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
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
	public abstract <T> T set(DataComponentType<? super T> type, @Nullable T value);

	@Inject(method = "addEnchantment", at = @At("TAIL"))
	private void enchancement$enchantedChestplatesIncreaseAirMobility(Enchantment enchantment, int level, CallbackInfo ci) {
		if (ModConfig.enchantedChestplatesIncreaseAirMobility && hasEnchantments() && isIn(ItemTags.CHEST_ARMOR_ENCHANTABLE) && !Registries.ENCHANTMENT.getEntry(enchantment).isIn(ModEnchantmentTags.DISALLOWS_TOGGLEABLE_PASSIVE) && !contains(ModDataComponentTypes.TOGGLEABLE_PASSIVE)) {
			set(ModDataComponentTypes.TOGGLEABLE_PASSIVE, true);
		}
	}
}
