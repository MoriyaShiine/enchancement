package moriyashiine.enchancement.mixin.config.enchantedbookgrinding;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moriyashiine.enchancement.common.EnchancementConfig;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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

	@Inject(method = "getExperienceFromItem(Lnet/minecraft/world/item/ItemStack;)I", at = @At("HEAD"))
	private void enchancement$enchantedBookGrinding(ItemStack item, CallbackInfoReturnable<Integer> cir) {
		slotStacks.add(item);
	}

	@ModifyReturnValue(method = "getExperienceAmount(Lnet/minecraft/world/level/Level;)I", at = @At("RETURN"))
	private int enchancement$enchantedBookGrinding(int original) {
		if (EnchancementConfig.enchantedBookGrinding && slotStacks.get(1).is(Items.BOOK)) {
			slotStacks.clear();
			return 0;
		}
		return original;
	}

	@WrapOperation(method = "onTake", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/Container;setItem(ILnet/minecraft/world/item/ItemStack;)V", ordinal = 0))
	private void enchancement$enchantedBookGrindingEnchanted(Container instance, int i, ItemStack stack, Operation<Void> original) {
		ItemStack replacement = EnchancementUtil.replaceTopGrindstoneItem(instance.getItem(0), instance.getItem(1));
		if (replacement != null) {
			stack = replacement;
		}
		original.call(instance, i, stack);
	}

	@WrapOperation(method = "onTake", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/Container;setItem(ILnet/minecraft/world/item/ItemStack;)V", ordinal = 1))
	private void enchancement$enchantedBookGrindingBook(Container instance, int i, ItemStack stack, Operation<Void> original) {
		ItemStack replacement = EnchancementUtil.replaceBottomGrindstoneItem(instance.getItem(1));
		if (replacement != null) {
			stack = replacement;
		}
		original.call(instance, i, stack);
	}
}
