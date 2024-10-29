/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.util.client;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.enchantment.effect.AllowLoadingProjectileEffect;
import moriyashiine.enchancement.common.init.ModComponentTypes;
import net.minecraft.client.render.item.ItemModels;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ChargedProjectilesComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemModels.class)
public abstract class ItemModelsMixin {
	@Shadow
	public abstract BakedModel getModel(Identifier id);

	@Inject(method = "getModel(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/client/render/model/BakedModel;", at = @At("HEAD"), cancellable = true)
	private void enchancement$chargedModel(ItemStack stack, CallbackInfoReturnable<BakedModel> cir) {
		@Nullable Identifier chargedModel = getChargedModel(stack);
		if (chargedModel != null) {
			cir.setReturnValue(getModel(chargedModel));
		}
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
