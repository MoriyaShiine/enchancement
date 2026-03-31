/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.rebalanceequipment;

import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Items.class)
public class ItemsMixin {
	@ModifyVariable(method = "registerItem(Ljava/lang/String;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;", at = @At("HEAD"), argsOnly = true)
	private static Item.Properties enchancement$rebalanceEquipment(Item.Properties properties, String name) {
		if (ModConfig.rebalanceEquipment && name.equals("enchanted_book")) {
			return properties.stacksTo(16);
		}
		return properties;
	}
}
