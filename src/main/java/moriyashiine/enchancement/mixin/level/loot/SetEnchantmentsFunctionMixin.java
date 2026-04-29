/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.level.loot;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moriyashiine.enchancement.common.init.ModEnchantments;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.SetEnchantmentsFunction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SetEnchantmentsFunction.class)
public class SetEnchantmentsFunctionMixin {
	@Unique
	private static LootContext context = null;

	@Inject(method = "run", at = @At("HEAD"))
	private void enchancement$generateMaceEnchantments(ItemStack itemStack, LootContext context, CallbackInfoReturnable<ItemStack> cir) {
		SetEnchantmentsFunctionMixin.context = context;
	}

	@WrapOperation(method = "lambda$run$2", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/ItemEnchantments$Mutable;set(Lnet/minecraft/core/Holder;I)V"))
	private static void enchancement$generateMaceEnchantments(ItemEnchantments.Mutable instance, Holder<Enchantment> enchantment, int level, Operation<Void> original) {
		if (enchantment.is(Enchantments.WIND_BURST)) {
			Registry<Enchantment> enchantmentRegistry = context.getLevel().registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
			enchantment = switch (context.getRandom().nextInt(3)) {
				case 0 -> enchantment;
				case 1 -> enchantmentRegistry.getOrThrow(ModEnchantments.METEOR);
				case 2 -> enchantmentRegistry.getOrThrow(ModEnchantments.THUNDERSTRUCK);
				default -> throw new IllegalStateException();
			};
		}
		original.call(instance, enchantment, level);
	}
}
