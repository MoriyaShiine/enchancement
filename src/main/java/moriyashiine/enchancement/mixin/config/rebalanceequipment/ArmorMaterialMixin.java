/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.rebalanceequipment;

import com.llamalad7.mixinextras.sugar.Local;
import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Map;

@Mixin(ArmorMaterial.class)
public class ArmorMaterialMixin {
	@ModifyVariable(method = "<init>", at = @At("HEAD"), argsOnly = true)
	private static Map<ArmorType, Integer> enchancement$rebalanceEquipment(Map<ArmorType, Integer> defense, @Local(argsOnly = true) ResourceKey<EquipmentAsset> assetId) {
		if (ModConfig.rebalanceEquipment) {
			if (assetId.equals(EquipmentAssets.IRON)) {
				defense.put(ArmorType.BOOTS, defense.get(ArmorType.BOOTS) + 1);
			}
			if (assetId.equals(EquipmentAssets.GOLD)) {
				defense.put(ArmorType.CHESTPLATE, defense.get(ArmorType.CHESTPLATE) - 1);
			}
		}

		return defense;
	}

	@ModifyVariable(method = "<init>", at = @At("HEAD"), argsOnly = true, ordinal = 0)
	private static float enchancement$rebalanceEquipment(float toughness, @Local(argsOnly = true) ResourceKey<EquipmentAsset> assetId) {
		if (ModConfig.rebalanceEquipment && assetId.equals(EquipmentAssets.IRON)) {
			return toughness + 1;
		}
		return toughness;
	}
}
