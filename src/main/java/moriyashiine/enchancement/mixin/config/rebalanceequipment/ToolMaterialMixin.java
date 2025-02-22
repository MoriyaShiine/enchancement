/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.rebalanceequipment;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.block.Block;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ToolMaterial.class)
public abstract class ToolMaterialMixin {
	@Unique
	private static final EntityAttributeModifier HOE_INTERACTION_RANGE_MODIFIER = new EntityAttributeModifier(Enchancement.id("hoe_interaction_range"), 0.5, EntityAttributeModifier.Operation.ADD_VALUE);

	@Shadow
	protected abstract AttributeModifiersComponent createToolAttributeModifiers(float attackDamage, float attackSpeed);

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

	@ModifyReturnValue(method = "applyToolSettings", at = @At("RETURN"))
	private Item.Settings enchancement$rebalanceEquipment(Item.Settings original, Item.Settings settings, TagKey<Block> effectiveBlocks, float attackDamage, float attackSpeed) {
		if (ModConfig.rebalanceEquipment && effectiveBlocks == BlockTags.HOE_MINEABLE) {
			return original.attributeModifiers(createToolAttributeModifiers(attackDamage, attackSpeed).with(EntityAttributes.ENTITY_INTERACTION_RANGE, HOE_INTERACTION_RANGE_MODIFIER, AttributeModifierSlot.MAINHAND));
		}
		return original;
	}
}
