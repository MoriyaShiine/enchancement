/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.vanillachanges.rebalancearmor;

import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterials;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ArmorMaterials.class)
public class ArmorMaterialsMixin {
	@Inject(method = "getProtection", at = @At("RETURN"), cancellable = true)
	private void enchancement$rebalanceArmor(ArmorItem.Type type, CallbackInfoReturnable<Integer> cir) {
		if (ModConfig.rebalanceArmor) {
			if (type == ArmorItem.Type.BOOTS) {
				if ((Object) this == ArmorMaterials.IRON) {
					cir.setReturnValue(cir.getReturnValueI() + 1);
				}
			} else if (type == ArmorItem.Type.CHESTPLATE) {
				if ((Object) this == ArmorMaterials.GOLD) {
					cir.setReturnValue(cir.getReturnValueI() - 1);
				}
			}
		}
	}

	@Inject(method = "getToughness", at = @At("RETURN"), cancellable = true)
	private void enchancement$rebalanceArmor(CallbackInfoReturnable<Float> cir) {
		if (ModConfig.rebalanceArmor) {
			if ((Object) this == ArmorMaterials.IRON) {
				cir.setReturnValue(cir.getReturnValueF() + 1);
			}
		}
	}
}
