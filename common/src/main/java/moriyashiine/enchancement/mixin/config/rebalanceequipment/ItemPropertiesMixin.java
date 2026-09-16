package moriyashiine.enchancement.mixin.config.rebalanceequipment;

import moriyashiine.enchancement.common.EnchancementConfig;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Item.Properties.class)
public class ItemPropertiesMixin {
	@ModifyVariable(method = "hoe", at = @At("HEAD"), argsOnly = true, index = 2)
	private static float enchancement$rebalanceEquipmentDamage(float attackDamageBaseline) {
		return EnchancementConfig.rebalanceEquipment ? Math.max(0, attackDamageBaseline) + 1 : attackDamageBaseline;
	}

	@ModifyVariable(method = "hoe", at = @At("HEAD"), argsOnly = true, index = 3)
	private static float enchancement$rebalanceEquipmentSpeed(float attackSpeedBaseline) {
		return EnchancementConfig.rebalanceEquipment ? -2 : attackSpeedBaseline;
	}
}
