package moriyashiine.enchancement.mixin.amphibious;

import moriyashiine.enchancement.common.registry.ModEnchantments;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
	@Inject(method = "getDepthStrider", at = @At("HEAD"), cancellable = true)
	private static void enchancement$amphibiousDepthStrider(LivingEntity entity, CallbackInfoReturnable<Integer> cir) {
		int level = EnchantmentHelper.getEquipmentLevel(ModEnchantments.AMPHIBIOUS, entity);
		if (level > 0) {
			cir.setReturnValue(Enchantments.DEPTH_STRIDER.getMaxLevel());
		}
	}

	@Inject(method = "getRespiration", at = @At("HEAD"), cancellable = true)
	private static void enchancement$amphibiousRespiration(LivingEntity entity, CallbackInfoReturnable<Integer> cir) {
		int level = EnchantmentHelper.getEquipmentLevel(ModEnchantments.AMPHIBIOUS, entity);
		if (level > 0) {
			cir.setReturnValue(Enchantments.RESPIRATION.getMaxLevel());
		}
	}

	@Inject(method = "hasAquaAffinity", at = @At("HEAD"), cancellable = true)
	private static void enchancement$amphibiousAquaAffinity(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
		if (EnchantmentHelper.getEquipmentLevel(ModEnchantments.AMPHIBIOUS, entity) > 0) {
			cir.setReturnValue(true);
		}
	}
}
