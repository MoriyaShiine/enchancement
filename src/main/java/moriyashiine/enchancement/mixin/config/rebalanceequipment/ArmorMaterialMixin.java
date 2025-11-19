/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.rebalanceequipment;

import com.llamalad7.mixinextras.sugar.Local;
import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.item.equipment.ArmorMaterial;
import net.minecraft.item.equipment.EquipmentAsset;
import net.minecraft.item.equipment.EquipmentAssetKeys;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.registry.RegistryKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Map;

@Mixin(ArmorMaterial.class)
public class ArmorMaterialMixin {
	@ModifyVariable(method = "<init>", at = @At("HEAD"), argsOnly = true)
	private static Map<EquipmentType, Integer> enchancement$rebalanceEquipment(Map<EquipmentType, Integer> defense, @Local(argsOnly = true) RegistryKey<EquipmentAsset> assetId) {
		if (ModConfig.rebalanceEquipment) {
			if (assetId.equals(EquipmentAssetKeys.IRON)) {
				defense.put(EquipmentType.BOOTS, defense.get(EquipmentType.BOOTS) + 1);
			}
			if (assetId.equals(EquipmentAssetKeys.GOLD)) {
				defense.put(EquipmentType.CHESTPLATE, defense.get(EquipmentType.CHESTPLATE) - 1);
			}
		}

		return defense;
	}

	@ModifyVariable(method = "<init>", at = @At("HEAD"), ordinal = 0, argsOnly = true)
	private static float enchancement$rebalanceEquipment(float toughness, @Local(argsOnly = true) RegistryKey<EquipmentAsset> assetId) {
		if (ModConfig.rebalanceEquipment && assetId.equals(EquipmentAssetKeys.IRON)) {
			return toughness + 1;
		}
		return toughness;
	}
}
