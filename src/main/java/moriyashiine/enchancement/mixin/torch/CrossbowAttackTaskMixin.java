/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.torch;

import moriyashiine.enchancement.common.init.ModEnchantments;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.task.CrossbowAttackTask;
import net.minecraft.entity.mob.MobEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CrossbowAttackTask.class)
public class CrossbowAttackTaskMixin<E extends MobEntity> {
	@Shadow
	private int chargingCooldown;

	@Inject(method = "tickState", at = @At("HEAD"))
	private void enchancement$torch(E entity, LivingEntity target, CallbackInfo ci) {
		if (EnchancementUtil.hasEnchantment(ModEnchantments.TORCH, entity.getMainHandStack()) && chargingCooldown > 5) {
			chargingCooldown = 5;
		}
	}
}
