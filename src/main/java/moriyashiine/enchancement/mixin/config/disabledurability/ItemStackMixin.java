/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.disabledurability;

import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
	@Shadow
	public abstract int getDamageValue();

	@Inject(method = "processDurabilityChange", at = @At("HEAD"))
	private void enchancement$disableDurability(int amount, ServerLevel level, ServerPlayer player, CallbackInfoReturnable<Integer> cir) {
		ItemStack stack = (ItemStack) (Object) this;
		if (player != null && EnchancementUtil.isUnbreakable(stack)) {
			CriteriaTriggers.ITEM_DURABILITY_CHANGED.trigger(player, stack, getDamageValue());
		}
	}

	@Inject(method = "isDamageableItem", at = @At("HEAD"), cancellable = true)
	private void enchancement$disableDurability(CallbackInfoReturnable<Boolean> cir) {
		if (EnchancementUtil.isUnbreakable((ItemStack) (Object) this)) {
			cir.setReturnValue(false);
		}
	}
}
