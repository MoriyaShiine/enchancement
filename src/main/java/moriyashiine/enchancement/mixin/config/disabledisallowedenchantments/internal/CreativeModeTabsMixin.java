/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.disabledisallowedenchantments.internal;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import moriyashiine.enchancement.common.init.ModEnchantments;
import net.minecraft.core.Holder;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.stream.Stream;

@Mixin(CreativeModeTabs.class)
public class CreativeModeTabsMixin {
	@ModifyExpressionValue(method = {"generateEnchantmentBookTypesAllLevels", "generateEnchantmentBookTypesOnlyMaxLevel"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/core/HolderLookup;listElements()Ljava/util/stream/Stream;"))
	private static Stream<Holder.Reference<Enchantment>> enchancement$disableDisallowedEnchantments(Stream<Holder.Reference<Enchantment>> original) {
		return original.filter(enchantment -> !enchantment.is(ModEnchantments.EMPTY_KEY));
	}
}
