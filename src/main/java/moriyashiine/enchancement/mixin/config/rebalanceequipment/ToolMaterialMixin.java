/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.rebalanceequipment;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ToolMaterial.class)
public abstract class ToolMaterialMixin {
	@Unique
	private static final AttributeModifier HOE_INTERACTION_RANGE_MODIFIER = new AttributeModifier(Enchancement.id("hoe_interaction_range"), 0.5, AttributeModifier.Operation.ADD_VALUE);

	@Shadow
	protected abstract ItemAttributeModifiers createToolAttributes(float attackDamageBaseline, float attackSpeedBaseline);

	@ModifyVariable(method = "<init>", at = @At("HEAD"), argsOnly = true, ordinal = 0)
	private static float enchancement$rebalanceEquipment(float speed, TagKey<Block> incorrectBlocksForDrops, int durability, float speed0, float attackDamageBonus, int enchantmentValue, TagKey<Item> repairItems) {
		if (ModConfig.rebalanceEquipment) {
			if (repairItems == ItemTags.GOLD_TOOL_MATERIALS) {
				return speed - 4;
			} else if (repairItems == ItemTags.DIAMOND_TOOL_MATERIALS) {
				return speed + 1;
			} else if (repairItems == ItemTags.NETHERITE_TOOL_MATERIALS) {
				return speed + 3;
			}
		}
		return speed;
	}

	@ModifyReturnValue(method = "applyToolProperties", at = @At("RETURN"))
	private Item.Properties enchancement$rebalanceEquipment(Item.Properties original, Item.Properties properties, TagKey<Block> minesEfficiently, float attackDamageBaseline, float attackSpeedBaseline) {
		if (ModConfig.rebalanceEquipment && minesEfficiently == BlockTags.MINEABLE_WITH_HOE) {
			return original.attributes(createToolAttributes(attackDamageBaseline, attackSpeedBaseline).withModifierAdded(Attributes.ENTITY_INTERACTION_RANGE, HOE_INTERACTION_RANGE_MODIFIER, EquipmentSlotGroup.MAINHAND));
		}
		return original;
	}
}
