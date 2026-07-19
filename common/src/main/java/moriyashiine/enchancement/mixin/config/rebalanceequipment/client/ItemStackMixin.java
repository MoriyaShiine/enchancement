package moriyashiine.enchancement.mixin.config.rebalanceequipment.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.init.EnchancementEntityComponents;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemStack.class)
public class ItemStackMixin {
	@ModifyReturnValue(method = "isBarVisible", at = @At("RETURN"))
	private boolean enchancement$rebalanceEquipment(boolean original) {
		return original || getChargeBarWidth() < 13;
	}

	@ModifyReturnValue(method = "getBarWidth", at = @At("RETURN"))
	private int enchancement$rebalanceEquipment(int original) {
		int chargeBarWidth = getChargeBarWidth();
		if (chargeBarWidth < 13) {
			return chargeBarWidth;
		}
		return original;
	}

	@ModifyReturnValue(method = "getBarColor", at = @At("RETURN"))
	private int enchancement$rebalanceEquipmentColor(int original) {
		if (getChargeBarWidth() < 13) {
			return 0x00C0FF;
		}
		return original;
	}

	@Unique
	private int getChargeBarWidth() {
		Player player = Minecraft.getInstance().player;
		return player == null ? 0 : EnchancementEntityComponents.LIMIT_BLOCKS_ATTACKS.get(player).getBarWidth((ItemStack) (Object) this);
	}
}
