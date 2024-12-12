/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.rebalanceequipment;

import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ToolMaterial.class)
public class ToolMaterialsMixin {
	@ModifyVariable(method = "<init>", at = @At("HEAD"), ordinal = 0, argsOnly = true)
	private static float enchancement$rebalanceEquipment(float value, TagKey<Block> incorrectBlocksForDrops, int durability, float speed, float attackDamageBonus, int enchantmentValue, TagKey<Item> repairItems) {
		if (ModConfig.rebalanceEquipment) {
			if (repairItems == ItemTags.GOLD_TOOL_MATERIALS) {
				return value - 4;
			} else if (repairItems == ItemTags.DIAMOND_TOOL_MATERIALS) {
				return value + 1;
			} else if (repairItems == ItemTags.NETHERITE_TOOL_MATERIALS) {
				return value + 3;
			}
		}
		return value;
	}
}
