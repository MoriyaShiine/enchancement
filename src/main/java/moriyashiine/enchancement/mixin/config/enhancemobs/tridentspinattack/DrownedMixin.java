/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.enhancemobs.tridentspinattack;

import com.llamalad7.mixinextras.sugar.Local;
import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.zombie.Drowned;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Drowned.class)
public class DrownedMixin {
	@SuppressWarnings("ConstantValue")
	@Inject(method = "performRangedAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/arrow/ThrownTrident;<init>(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;)V"), cancellable = true)
	private void enchancement$enhanceMobs(LivingEntity target, float power, CallbackInfo ci, @Local(name = "tridentItemStack") ItemStack tridentItemStack) {
		if (ModConfig.enhanceMobs && EnchantmentHelper.getTridentSpinAttackStrength(tridentItemStack, (LivingEntity) (Object) this) > 0) {
			ci.cancel();
		}
	}
}
