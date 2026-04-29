/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.item.maceeffect.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import moriyashiine.enchancement.common.component.entity.internal.UsingMaceComponent;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.world.item.ItemUseAnimation;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemInHandRenderer.class)
public class ItemInHandRendererMixin {
	@ModifyExpressionValue(method = "renderArmWithItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getUseAnimation()Lnet/minecraft/world/item/ItemUseAnimation;"))
	private ItemUseAnimation enchancement$maceEffect(ItemUseAnimation original, AbstractClientPlayer player) {
		for (ComponentKey<?> key : player.asComponentProvider().getComponentContainer().keys()) {
			if (player.getComponent(key) instanceof UsingMaceComponent usingMaceComponent && usingMaceComponent.isUsing()) {
				return ItemUseAnimation.TRIDENT;
			}
		}
		return original;
	}
}
