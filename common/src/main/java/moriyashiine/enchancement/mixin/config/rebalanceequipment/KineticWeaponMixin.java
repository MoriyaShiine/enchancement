package moriyashiine.enchancement.mixin.config.rebalanceequipment;

import moriyashiine.enchancement.common.EnchancementConfig;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.world.item.component.KineticWeapon;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(KineticWeapon.class)
public class KineticWeaponMixin {
	@ModifyArg(method = "damageEntities", at = @At(value = "INVOKE", target = "Ljava/lang/Math;max(DD)D"), index = 1)
	private double enchancement$rebalanceEquipment(double value) {
		if (EnchancementConfig.rebalanceEquipment) {
			return EnchancementUtil.logistic(20, value);
		}
		return value;
	}
}
