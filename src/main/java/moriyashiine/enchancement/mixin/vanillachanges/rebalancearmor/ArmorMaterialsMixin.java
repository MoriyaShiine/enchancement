/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.vanillachanges.rebalancearmor;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterials;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.EnumMap;

@Mixin(ArmorMaterials.class)
public class ArmorMaterialsMixin {
	@ModifyVariable(method = "register(Ljava/lang/String;Ljava/util/EnumMap;ILnet/minecraft/registry/entry/RegistryEntry;FFLjava/util/function/Supplier;)Lnet/minecraft/registry/entry/RegistryEntry;", at = @At("HEAD"), argsOnly = true)
	private static EnumMap<ArmorItem.Type, Integer> enchancement$rebalanceArmor(EnumMap<ArmorItem.Type, Integer> value, String id) {
		if (id.equals("iron")) {
			value.put(ArmorItem.Type.BOOTS, value.get(ArmorItem.Type.BOOTS) + 1);
		} else if (id.equals("gold")) {
			value.put(ArmorItem.Type.CHESTPLATE, value.get(ArmorItem.Type.CHESTPLATE) - 1);
		}
		return value;
	}

	@ModifyVariable(method = "register(Ljava/lang/String;Ljava/util/EnumMap;ILnet/minecraft/registry/entry/RegistryEntry;FFLjava/util/function/Supplier;)Lnet/minecraft/registry/entry/RegistryEntry;", at = @At("HEAD"), ordinal = 0, argsOnly = true)
	private static float enchancement$rebalanceArmor(float value, String id) {
		if (id.equals("iron")) {
			return value + 1;
		}
		return value;
	}
}
