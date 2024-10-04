/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.disabledisallowedenchantments.codec;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.serialization.codecs.BaseMapCodec;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BaseMapCodec.class)
public interface BaseMapCodecMixin<V> {
	@ModifyExpressionValue(method = "lambda$decode$3", at = @At(value = "INVOKE", target = "Lit/unimi/dsi/fastutil/objects/Object2ObjectMap;putIfAbsent(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"), remap = false)
	private V enchancement$disableDisallowedEnchantments(V original) {
		return null;
	}
}
