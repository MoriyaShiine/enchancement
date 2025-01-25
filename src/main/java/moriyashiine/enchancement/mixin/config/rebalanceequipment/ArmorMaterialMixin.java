/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.rebalanceequipment;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.equipment.ArmorMaterial;
import net.minecraft.item.equipment.ArmorMaterials;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ArmorMaterial.class)
public class ArmorMaterialMixin {
	@WrapOperation(method = "createAttributeModifiers", at = @At(value = "INVOKE", target = "Lnet/minecraft/component/type/AttributeModifiersComponent$Builder;add(Lnet/minecraft/registry/entry/RegistryEntry;Lnet/minecraft/entity/attribute/EntityAttributeModifier;Lnet/minecraft/component/type/AttributeModifierSlot;)Lnet/minecraft/component/type/AttributeModifiersComponent$Builder;", ordinal = 0))
	private AttributeModifiersComponent.Builder enchancement$rebalanceEquipment(AttributeModifiersComponent.Builder instance, RegistryEntry<EntityAttribute> attribute, EntityAttributeModifier modifier, AttributeModifierSlot slot, Operation<AttributeModifiersComponent.Builder> original) {
		if (ModConfig.rebalanceEquipment) {
			int change = 0;
			if ((Object) this == ArmorMaterials.IRON && slot == AttributeModifierSlot.FEET) {
				change++;
			}
			if ((Object) this == ArmorMaterials.GOLD && slot == AttributeModifierSlot.CHEST) {
				change--;
			}
			if (change != 0) {
				modifier = new EntityAttributeModifier(modifier.id(), modifier.value() + change, modifier.operation());
			}
		}
		return original.call(instance, attribute, modifier, slot);
	}

	@WrapOperation(method = "createAttributeModifiers", at = @At(value = "INVOKE", target = "Lnet/minecraft/component/type/AttributeModifiersComponent$Builder;add(Lnet/minecraft/registry/entry/RegistryEntry;Lnet/minecraft/entity/attribute/EntityAttributeModifier;Lnet/minecraft/component/type/AttributeModifierSlot;)Lnet/minecraft/component/type/AttributeModifiersComponent$Builder;", ordinal = 1))
	private AttributeModifiersComponent.Builder enchancement$rebalanceEquipmentToughness(AttributeModifiersComponent.Builder instance, RegistryEntry<EntityAttribute> attribute, EntityAttributeModifier modifier, AttributeModifierSlot slot, Operation<AttributeModifiersComponent.Builder> original) {
		if (ModConfig.rebalanceEquipment && (Object) this == ArmorMaterials.IRON) {
			modifier = new EntityAttributeModifier(modifier.id(), modifier.value() + 1, modifier.operation());
		}
		return original.call(instance, attribute, modifier, slot);
	}
}
