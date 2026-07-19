package moriyashiine.enchancement.mixin.config.rebalanceequipment;

import moriyashiine.enchancement.common.EnchancementConfig;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Items.class)
public class ItemsMixin {
	@ModifyVariable(method = "registerItem(Lnet/minecraft/resources/ResourceKey;Ljava/util/function/Function;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;", at = @At("HEAD"), argsOnly = true)
	private static Item.Properties enchancement$rebalanceEquipment(Item.Properties properties, ResourceKey<Item> key) {
		if (EnchancementConfig.rebalanceEquipment && key.identifier().getPath().equals("enchanted_book")) {
			properties.stacksTo(16);
		}
		return properties;
	}
}
