/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.rebalanceequipment;

import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Items.class)
public class ItemsMixin {
	@ModifyVariable(method = "register(Ljava/lang/String;Lnet/minecraft/item/Item$Settings;)Lnet/minecraft/item/Item;", at = @At("HEAD"), argsOnly = true)
	private static Item.Settings enchancement$rebalanceEquipment(Item.Settings value, String id) {
		if (ModConfig.rebalanceEquipment && id.equals("enchanted_book")) {
			return value.maxCount(16);
		}
		return value;
	}
}
