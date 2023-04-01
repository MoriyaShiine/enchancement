/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.vanillachanges.unbreakingchanges;

import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Consumer;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
	@Shadow
	public abstract int getDamage();

	@Inject(method = "damage(ILnet/minecraft/entity/LivingEntity;Ljava/util/function/Consumer;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isDamageable()Z"))
	private <T extends LivingEntity> void enchancement$fixUnbreakableItemsNotGrantingAdvancements(int amount, T entity, Consumer<T> breakCallback, CallbackInfo ci) {
		if (entity instanceof ServerPlayerEntity player && EnchancementUtil.shouldBeUnbreakable(ItemStack.class.cast(this))) {
			Criteria.ITEM_DURABILITY_CHANGED.trigger(player, ItemStack.class.cast(this), getDamage());
		}
	}

	@Inject(method = "isDamageable", at = @At("HEAD"), cancellable = true)
	private void enchancement$unbreakingChanges(CallbackInfoReturnable<Boolean> cir) {
		if (EnchancementUtil.shouldBeUnbreakable(ItemStack.class.cast(this))) {
			cir.setReturnValue(false);
		}
	}
}
