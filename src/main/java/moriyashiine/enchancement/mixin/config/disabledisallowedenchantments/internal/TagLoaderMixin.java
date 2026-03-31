/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.disabledisallowedenchantments.internal;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.tags.TagEntry;
import net.minecraft.tags.TagLoader;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Consumer;

@Mixin(TagLoader.class)
public class TagLoaderMixin {
	@Shadow
	@Final
	private String directory;

	@WrapOperation(method = "tryBuildTag", at = @At(value = "INVOKE", target = "Lnet/minecraft/tags/TagEntry;build(Lnet/minecraft/tags/TagEntry$Lookup;Ljava/util/function/Consumer;)Z"))
	private <T> boolean enchancement$disableDisallowedEnchantments(TagEntry instance, TagEntry.Lookup<T> lookup, Consumer<T> output, Operation<Boolean> original) {
		return original.call(instance, lookup, output) || (directory.equals("tags/enchantment") && !EnchancementUtil.isEnchantmentAllowed(instance.id));
	}
}