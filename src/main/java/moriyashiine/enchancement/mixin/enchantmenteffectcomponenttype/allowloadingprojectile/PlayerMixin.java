/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.allowloadingprojectile;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import moriyashiine.enchancement.common.world.item.effects.AllowLoadingProjectileEffect;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Set;
import java.util.function.Predicate;

@Mixin(Player.class)
public class PlayerMixin {
	@ModifyVariable(method = "getProjectile", at = @At("STORE"), name = "supportedProjectiles")
	private Predicate<ItemStack> enchancement$allowLoadingProjectileHeld(Predicate<ItemStack> supportedProjectiles, @Local(argsOnly = true) ItemStack heldWeapon) {
		if (AllowLoadingProjectileEffect.onlyAllow(heldWeapon)) {
			return stack -> AllowLoadingProjectileEffect.getItems(heldWeapon).contains(stack.getItem());
		}
		return supportedProjectiles;
	}

	@ModifyReturnValue(method = "getProjectile", at = @At(value = "RETURN", ordinal = 3))
	private ItemStack enchancement$allowLoadingProjectile(ItemStack original, ItemStack heldWeapon) {
		if (!original.isEmpty()) {
			Set<Item> items = AllowLoadingProjectileEffect.getItems(heldWeapon);
			for (Item item : items) {
				return item.getDefaultInstance();
			}
		}
		return original;
	}
}
