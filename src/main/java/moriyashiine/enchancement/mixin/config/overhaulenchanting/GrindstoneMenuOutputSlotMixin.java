/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.overhaulenchanting;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.util.config.OverhaulMode;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(targets = "net.minecraft.world.inventory.GrindstoneMenu$4")
public class GrindstoneMenuOutputSlotMixin {
	@Unique
	private final List<ItemStack> slotStacks = new ArrayList<>();

	@ModifyReturnValue(method = "getExperienceAmount(Lnet/minecraft/world/level/Level;)I", at = @At("RETURN"))
	private int enchancement$overhaulEnchantingStore(int original) {
		if (ModConfig.overhaulEnchanting == OverhaulMode.CHISELED && slotStacks.get(1).is(Items.BOOK)) {
			slotStacks.clear();
			return 0;
		}
		return original;
	}

	@Inject(method = "getExperienceFromItem(Lnet/minecraft/world/item/ItemStack;)I", at = @At("HEAD"))
	private void enchancement$overhaulEnchanting(ItemStack item, CallbackInfoReturnable<Integer> cir) {
		slotStacks.add(item);
	}

	@WrapOperation(method = "onTake", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/Container;setItem(ILnet/minecraft/world/item/ItemStack;)V", ordinal = 0))
	private void enchancement$overhaulEnchantingEnchanted(Container instance, int i, ItemStack stack, Operation<Void> original) {
		if (ModConfig.overhaulEnchanting == OverhaulMode.CHISELED && instance.getItem(1).is(Items.BOOK)) {
			stack = instance.getItem(i);
			EnchantmentHelper.setEnchantments(stack, ItemEnchantments.EMPTY);
		}
		original.call(instance, i, stack);
	}

	@WrapOperation(method = "onTake", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/Container;setItem(ILnet/minecraft/world/item/ItemStack;)V", ordinal = 1))
	private void enchancement$overhaulEnchantingBook(Container instance, int i, ItemStack stack, Operation<Void> original) {
		if (ModConfig.overhaulEnchanting == OverhaulMode.CHISELED) {
			ItemStack book = instance.getItem(i);
			if (book.getCount() > 1 && book.is(Items.BOOK)) {
				book.shrink(1);
				stack = book;
			}
		}
		original.call(instance, i, stack);
	}
}
