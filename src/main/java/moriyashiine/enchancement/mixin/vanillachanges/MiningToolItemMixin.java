package moriyashiine.enchancement.mixin.vanillachanges;

import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MiningToolItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MiningToolItem.class)
public class MiningToolItemMixin {
	@Inject(method = "postHit", at = @At("HEAD"), cancellable = true)
	private void enchancement$reduceDurabilityTaken(ItemStack stack, LivingEntity target, LivingEntity attacker, CallbackInfoReturnable<Boolean> cir) {
		if (EnchancementUtil.shouldReduceDurabilityTaken(stack)) {
			stack.damage(1, attacker, user -> user.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
			cir.setReturnValue(true);
		}
	}
}
