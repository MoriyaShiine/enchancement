/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.vanillachanges.disabledurability;

import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
	@Shadow
	public abstract int getDamage();

	@Inject(method = "damage(ILnet/minecraft/util/math/random/Random;Lnet/minecraft/server/network/ServerPlayerEntity;Ljava/lang/Runnable;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isDamageable()Z"))
	private <T extends LivingEntity> void enchancement$fixUnbreakableItemsNotGrantingAdvancements(int amount, Random random, @Nullable ServerPlayerEntity player, Runnable breakCallback, CallbackInfo ci) {
		if (player != null && EnchancementUtil.shouldBeUnbreakable((ItemStack) (Object) this)) {
			Criteria.ITEM_DURABILITY_CHANGED.trigger(player, (ItemStack) (Object) this, getDamage());
		}
	}

	@Inject(method = "isDamageable", at = @At("HEAD"), cancellable = true)
	private void enchancement$unbreakingChanges(CallbackInfoReturnable<Boolean> cir) {
		if (EnchancementUtil.shouldBeUnbreakable((ItemStack) (Object) this)) {
			cir.setReturnValue(false);
		}
	}
}
