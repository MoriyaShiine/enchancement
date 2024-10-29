/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.disabledurability;

import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
	@Shadow
	public abstract int getDamage();

	@Inject(method = "calculateDamage", at = @At("HEAD"))
	private void enchancement$disableDurability(int baseDamage, ServerWorld world, ServerPlayerEntity player, CallbackInfoReturnable<Integer> cir) {
		ItemStack stack = (ItemStack) (Object) this;
		if (player != null && EnchancementUtil.isUnbreakable(stack)) {
			Criteria.ITEM_DURABILITY_CHANGED.trigger(player, stack, getDamage());
		}
	}

	@Inject(method = "isDamageable", at = @At("HEAD"), cancellable = true)
	private void enchancement$disableDurability(CallbackInfoReturnable<Boolean> cir) {
		if (EnchancementUtil.isUnbreakable((ItemStack) (Object) this)) {
			cir.setReturnValue(false);
		}
	}
}
