package moriyashiine.enchancement.neoforge.mixin.config.rebalanceequipment;

import moriyashiine.enchancement.common.EnchancementConfig;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
	@SuppressWarnings("ConstantValue")
	@ModifyArg(method = "canGlide(Z)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;canGlideUsing(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/EquipmentSlot;)Z"))
	private ItemStack enchancement$rebalanceEquipment(ItemStack itemStack) {
		if (EnchancementConfig.rebalanceEquipment && (Object) this instanceof Player player && player.getCooldowns().isOnCooldown(itemStack)) {
			return ItemStack.EMPTY;
		}
		return itemStack;
	}
}
