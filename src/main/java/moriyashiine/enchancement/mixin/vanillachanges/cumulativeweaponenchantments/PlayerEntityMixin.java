/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.vanillachanges.cumulativeweaponenchantments;

import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
	@Unique
	private int cachedTargetFireTicks = 0;

	@Inject(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;setOnFireFor(I)V", ordinal = 1, shift = At.Shift.BEFORE))
	private void enchancement$cumulativeWeaponEnchantments(Entity target, CallbackInfo ci) {
		cachedTargetFireTicks = target.getFireTicks();
	}

	@ModifyArg(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;setOnFireFor(I)V", ordinal = 1))
	private int enchancement$cumulativeWeaponEnchantments(int value) {
		if (ModConfig.cumulativeWeaponEnchantments) {
			int level = EnchantmentHelper.getFireAspect((LivingEntity) (Object) this);
			int fire = cachedTargetFireTicks / 20;
			int max;
			if (ModConfig.weakerFireAspect) {
				max = level == 1 ? 3 : 6;
			} else {
				max = level == 1 ? 4 : 8;
			}
			return Math.min(max, fire + 2);
		}
		return value;
	}
}
