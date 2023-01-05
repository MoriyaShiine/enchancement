/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.enchancement.mixin.assimilation;

import moriyashiine.enchancement.common.registry.ModEnchantments;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	@Shadow
	protected ItemStack activeItemStack;

	@Shadow
	protected int itemUseTimeLeft;

	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Inject(method = "setCurrentHand", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/item/ItemStack;getMaxUseTime()I", shift = At.Shift.AFTER))
	private void enchancement$assimilation(Hand hand, CallbackInfo ci) {
		modifyTimeLeft();
	}

	@Inject(method = "onTrackedDataSet", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/item/ItemStack;getMaxUseTime()I", shift = At.Shift.AFTER))
	private void enchancement$assimilation(TrackedData<?> data, CallbackInfo ci) {
		modifyTimeLeft();
	}

	@Unique
	private void modifyTimeLeft() {
		if (activeItemStack.isFood() || activeItemStack.getUseAction() == UseAction.DRINK) {
			if (EnchancementUtil.hasEnchantment(ModEnchantments.ASSIMILATION, this)) {
				itemUseTimeLeft *= 0.75F;
			}
		}
	}
}
