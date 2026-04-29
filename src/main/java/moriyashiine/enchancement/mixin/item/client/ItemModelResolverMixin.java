/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.item.client;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.init.ModComponentTypes;
import moriyashiine.enchancement.common.world.item.effects.AllowLoadingProjectileEffect;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.component.ChargedProjectiles;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemModelResolver.class)
public class ItemModelResolverMixin {
	@ModifyVariable(method = "appendItemLayers", at = @At("STORE"), name = "modelId")
	private Identifier enchancement$chargedModel(Identifier modelId, ItemStackRenderState output, ItemStack item) {
		@Nullable Identifier chargedModel = getChargedModel(item);
		if (chargedModel != null) {
			return chargedModel;
		}
		return modelId;
	}

	@Unique
	private static @Nullable Identifier getChargedModel(ItemStack stack) {
		for (ItemStackTemplate projectile : stack.getOrDefault(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.EMPTY).items()) {
			@Nullable Identifier chargedModel = getChargedModel(stack, projectile.item().value());
			if (chargedModel != null) {
				return chargedModel;
			}
		}
		return null;
	}

	@Unique
	private static @Nullable Identifier getChargedModel(ItemStack stack, Item item) {
		int brimstoneDamage = stack.getOrDefault(ModComponentTypes.BRIMSTONE_DAMAGE, 0);
		if (brimstoneDamage > 0) {
			return Enchancement.id("crossbow_brimstone_" + (brimstoneDamage / 2 - 1));
		}
		if (AllowLoadingProjectileEffect.getItems(stack).contains(item)) {
			return AllowLoadingProjectileEffect.getModel(stack, item);
		}
		return null;
	}
}
