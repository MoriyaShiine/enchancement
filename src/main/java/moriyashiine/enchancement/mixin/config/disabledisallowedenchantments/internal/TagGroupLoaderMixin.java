/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.disabledisallowedenchantments.internal;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.registry.tag.TagEntry;
import net.minecraft.registry.tag.TagGroupLoader;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Consumer;

@Mixin(TagGroupLoader.class)
public class TagGroupLoaderMixin {
	@Shadow
	@Final
	private String dataType;

	@WrapOperation(method = "resolveAll", at = @At(value = "INVOKE", target = "Lnet/minecraft/registry/tag/TagEntry;resolve(Lnet/minecraft/registry/tag/TagEntry$ValueGetter;Ljava/util/function/Consumer;)Z"))
	private <T> boolean enchancement$disableDisallowedEnchantments(TagEntry instance, TagEntry.ValueGetter<T> valueGetter, Consumer<T> idConsumer, Operation<Boolean> original) {
		return original.call(instance, valueGetter, idConsumer) || (dataType.equals("tags/enchantment") && !EnchancementUtil.isEnchantmentAllowed(instance.id));
	}
}