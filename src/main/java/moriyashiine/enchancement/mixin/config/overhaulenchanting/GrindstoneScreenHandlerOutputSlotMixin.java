/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.overhaulenchanting;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.util.config.OverhaulMode;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(targets = "net/minecraft/screen/GrindstoneScreenHandler$4")
public class GrindstoneScreenHandlerOutputSlotMixin {
	@Unique
	private final List<ItemStack> slotStacks = new ArrayList<>();

	@ModifyReturnValue(method = "getExperience(Lnet/minecraft/world/World;)I", at = @At("RETURN"))
	private int enchancement$overhaulEnchantingStore(int original) {
		if (ModConfig.overhaulEnchanting == OverhaulMode.CHISELED && slotStacks.get(1).isOf(Items.BOOK)) {
			slotStacks.clear();
			return 0;
		}
		return original;
	}

	@Inject(method = "getExperience(Lnet/minecraft/item/ItemStack;)I", at = @At("HEAD"))
	private void enchancement$overhaulEnchanting(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
		slotStacks.add(stack);
	}

	@WrapOperation(method = "onTakeItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/inventory/Inventory;setStack(ILnet/minecraft/item/ItemStack;)V", ordinal = 0))
	private void enchancement$overhaulEnchantingEnchanted(Inventory instance, int i, ItemStack stack, Operation<Void> original) {
		if (ModConfig.overhaulEnchanting == OverhaulMode.CHISELED && instance.getStack(1).isOf(Items.BOOK)) {
			stack = instance.getStack(i);
			EnchantmentHelper.set(stack, ItemEnchantmentsComponent.DEFAULT);
		}
		original.call(instance, i, stack);
	}

	@WrapOperation(method = "onTakeItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/inventory/Inventory;setStack(ILnet/minecraft/item/ItemStack;)V", ordinal = 1))
	private void enchancement$overhaulEnchantingBook(Inventory instance, int i, ItemStack stack, Operation<Void> original) {
		if (ModConfig.overhaulEnchanting == OverhaulMode.CHISELED) {
			ItemStack book = instance.getStack(i);
			if (book.getCount() > 1 && book.isOf(Items.BOOK)) {
				book.decrement(1);
				stack = book;
			}
		}
		original.call(instance, i, stack);
	}
}
