/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.util.client;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.enchantment.effect.AllowLoadingProjectileEffect;
import moriyashiine.enchancement.common.init.ModComponentTypes;
import net.minecraft.client.item.ItemModelManager;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ChargedProjectilesComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemModelManager.class)
public class ItemModelManagerMixin {
	@ModifyVariable(method = "update(Lnet/minecraft/client/render/item/ItemRenderState;Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ModelTransformationMode;Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;I)V", at = @At("STORE"))
	private Identifier enchancement$chargedModel(Identifier value, ItemRenderState renderState, ItemStack stack) {
		@Nullable Identifier chargedModel = getChargedModel(stack);
		if (chargedModel != null) {
			return chargedModel;
		}
		return value;
	}

	@Unique
	@Nullable
	private static Identifier getChargedModel(ItemStack stack) {
		for (Item item : Registries.ITEM) {
			@Nullable Identifier chargedModel = getChargedModel(stack, item);
			if (chargedModel != null) {
				return chargedModel;
			}
		}
		return null;
	}

	@Unique
	@Nullable
	private static Identifier getChargedModel(ItemStack stack, Item item) {
		if (stack.getOrDefault(DataComponentTypes.CHARGED_PROJECTILES, ChargedProjectilesComponent.DEFAULT).contains(item) && AllowLoadingProjectileEffect.getItems(stack).contains(item)) {
			return AllowLoadingProjectileEffect.getModel(stack, item);
		}
		int brimstoneDamage = stack.getOrDefault(ModComponentTypes.BRIMSTONE_DAMAGE, 0);
		if (brimstoneDamage > 0) {
			return Enchancement.id("crossbow_brimstone_" + (brimstoneDamage / 2 - 1));
		}
		return null;
	}
}
