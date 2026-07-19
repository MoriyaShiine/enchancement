package moriyashiine.enchancement.mixin.config.rebalanceequipment;

import com.llamalad7.mixinextras.sugar.Local;
import moriyashiine.enchancement.common.EnchancementConfig;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.MaceItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MaceItem.class)
public class MaceItemMixin {
	@ModifyArg(method = "createAttributes", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/attributes/AttributeModifier;<init>(Lnet/minecraft/resources/Identifier;DLnet/minecraft/world/entity/ai/attributes/AttributeModifier$Operation;)V", ordinal = 1), index = 1)
	private static double enchancement$rebalanceEquipment(double value) {
		if (EnchancementConfig.rebalanceEquipment) {
			return -3.2;
		}
		return value;
	}

	@Inject(method = "getAttackDamageBonus", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;level()Lnet/minecraft/world/level/Level;"), cancellable = true)
	private void enchancement$rebalanceEquipment(Entity victim, float ignoredDamage, DamageSource damageSource, CallbackInfoReturnable<Float> cir, @Local LivingEntity attacker) {
		if (EnchancementConfig.rebalanceEquipment) {
			float damage = (float) EnchancementUtil.altLog(2.25, attacker.fallDistance, 6);
			if (attacker.level() instanceof ServerLevel level) {
				float bonus = EnchantmentHelper.modifyFallBasedDamage(level, attacker.getWeaponItem(), victim, damageSource, 0);
				damage += bonus * 2;
			}
			cir.setReturnValue(damage);
		}
	}
}
