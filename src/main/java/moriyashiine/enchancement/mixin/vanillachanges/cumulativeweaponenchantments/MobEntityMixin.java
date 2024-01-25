/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.vanillachanges.cumulativeweaponenchantments;

import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobEntity.class)
public class MobEntityMixin {
	@Unique
	private int cachedTargetFireTicks = 0;

	@Inject(method = "tryAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;setOnFireFor(I)V", shift = At.Shift.BEFORE))
	private void enchancement$cumulativeWeaponEnchantments(Entity target, CallbackInfoReturnable<Boolean> cir) {
		cachedTargetFireTicks = target.getFireTicks();
	}

	@ModifyArg(method = "tryAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;setOnFireFor(I)V"))
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
