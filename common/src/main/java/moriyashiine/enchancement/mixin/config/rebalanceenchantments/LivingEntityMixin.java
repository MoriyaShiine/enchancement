package moriyashiine.enchancement.mixin.config.rebalanceenchantments;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import moriyashiine.enchancement.common.EnchancementConfig;
import moriyashiine.enchancement.common.init.EnchancementEntityComponents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
	@ModifyExpressionValue(method = "hurtServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;hasEffect(Lnet/minecraft/core/Holder;)Z"))
	private boolean enchancement$rebalanceEnchantmentsBypass(boolean original, @Local(argsOnly = true) DamageSource source) {
		if (original && source.is(DamageTypes.ON_FIRE) && EnchancementEntityComponents.IGNITED.get(this).ignoreFireResistance()) {
			return false;
		}
		return original;
	}

	@ModifyExpressionValue(method = "hurtServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/damagesource/DamageSource;is(Lnet/minecraft/tags/TagKey;)Z", ordinal = 4))
	private boolean enchancement$rebalanceEnchantmentsKnockback(boolean original, @Local(argsOnly = true) DamageSource source) {
		if (EnchancementConfig.rebalanceEnchantments && !original) {
			if (source.is(DamageTypes.ON_FIRE) && EnchancementEntityComponents.IGNITED.get(this).isIgnited()) {
				return true;
			}
			if (source.is(DamageTypes.FREEZE) && EnchancementEntityComponents.FROZEN.get(this).getFreezeTicks() > 0) {
				return true;
			}
		}
		return original;
	}
}
