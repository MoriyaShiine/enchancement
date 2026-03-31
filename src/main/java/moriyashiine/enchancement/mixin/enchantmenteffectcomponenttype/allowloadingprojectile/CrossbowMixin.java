/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.allowloadingprojectile;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import moriyashiine.enchancement.common.world.item.effects.AllowLoadingProjectileEffect;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Predicate;

@Mixin({Player.class, Monster.class})
public class CrossbowMixin {
	@WrapOperation(method = "getProjectile", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ProjectileWeaponItem;getSupportedHeldProjectiles()Ljava/util/function/Predicate;"))
	private Predicate<ItemStack> enchancement$allowLoadingProjectile(ProjectileWeaponItem instance, Operation<Predicate<ItemStack>> original, @Local(argsOnly = true) ItemStack heldWeapon) {
		return original.call(instance).or(stack -> AllowLoadingProjectileEffect.getItems(heldWeapon).contains(stack.getItem()));
	}
}
