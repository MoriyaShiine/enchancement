/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.util;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moriyashiine.enchancement.common.init.ModEnchantments;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.function.SetEnchantmentsLootFunction;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SetEnchantmentsLootFunction.class)
public class SetEnchantmentsLootFunctionMixin {
	@Unique
	private static LootContext context = null;

	@Inject(method = "process", at = @At("HEAD"))
	private void enchancement$generateMaceEnchantments(ItemStack stack, LootContext context, CallbackInfoReturnable<ItemStack> cir) {
		SetEnchantmentsLootFunctionMixin.context = context;
	}

	@WrapOperation(method = "method_60297", at = @At(value = "INVOKE", target = "Lnet/minecraft/component/type/ItemEnchantmentsComponent$Builder;set(Lnet/minecraft/registry/entry/RegistryEntry;I)V"))
	private static void enchancement$generateMaceEnchantments(ItemEnchantmentsComponent.Builder instance, RegistryEntry<Enchantment> enchantment, int level, Operation<Void> original) {
		if (enchantment.matchesKey(Enchantments.WIND_BURST)) {
			Registry<Enchantment> enchantmentLookup = context.getWorld().getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT);
			enchantment = switch (context.getRandom().nextInt(3)) {
				case 0 -> enchantment;
				case 1 -> enchantmentLookup.getOrThrow(ModEnchantments.METEOR);
				case 2 -> enchantmentLookup.getOrThrow(ModEnchantments.THUNDERSTRUCK);
				default -> throw new IllegalStateException();
			};
		}
		original.call(instance, enchantment, level);
	}
}
