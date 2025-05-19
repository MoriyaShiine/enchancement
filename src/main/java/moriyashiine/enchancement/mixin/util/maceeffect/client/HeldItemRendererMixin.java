/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.util.maceeffect.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import moriyashiine.enchancement.common.component.entity.UsingMaceComponent;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.item.consume.UseAction;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(HeldItemRenderer.class)
public class HeldItemRendererMixin {
	@ModifyExpressionValue(method = "renderFirstPersonItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getUseAction()Lnet/minecraft/item/consume/UseAction;"))
	private UseAction enchancement$maceEffect(UseAction original, AbstractClientPlayerEntity player) {
		for (ComponentKey<?> key : player.asComponentProvider().getComponentContainer().keys()) {
			if (player.getComponent(key) instanceof UsingMaceComponent usingMaceComponent && usingMaceComponent.isUsing()) {
				return UseAction.SPEAR;
			}
		}
		return original;
	}
}
