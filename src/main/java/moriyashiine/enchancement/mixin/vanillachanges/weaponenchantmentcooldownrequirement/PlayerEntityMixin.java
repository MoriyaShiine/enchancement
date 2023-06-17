/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.vanillachanges.weaponenchantmentcooldownrequirement;

import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
	@Unique
	private float attackCooldown = 0;

	@ModifyVariable(method = "attack", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/enchantment/EnchantmentHelper;getFireAspect(Lnet/minecraft/entity/LivingEntity;)I"), ordinal = 1)
	private float enchancement$weaponEnchantmentCooldownRequirement(float value) {
		if (attackCooldown < ModConfig.weaponEnchantmentCooldownRequirement) {
			return 0;
		}
		return value;
	}

	@Inject(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;resetLastAttackedTicks()V"), locals = LocalCapture.CAPTURE_FAILHARD)
	private void enchancement$weaponEnchantmentCooldownRequirement(Entity target, CallbackInfo ci, float attackDamage, float extraDamage, float attackCooldown) {
		this.attackCooldown = attackCooldown;
	}

	@Inject(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentHelper;onTargetDamaged(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/Entity;)V"))
	private void enchancement$weaponEnchantmentCooldownRequirementOnTargetDamaged(Entity target, CallbackInfo ci) {
		if (attackCooldown < ModConfig.weaponEnchantmentCooldownRequirement) {
			EnchancementUtil.shouldCancelTargetDamagedEnchantments = true;
		}
	}

	@Inject(method = "attack", at = @At("TAIL"))
	private void enchancement$weaponEnchantmentCooldownRequirement(Entity target, CallbackInfo ci) {
		attackCooldown = 0;
	}
}
