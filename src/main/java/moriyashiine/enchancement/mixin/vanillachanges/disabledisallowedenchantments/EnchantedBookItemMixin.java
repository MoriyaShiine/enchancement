package moriyashiine.enchancement.mixin.vanillachanges.disabledisallowedenchantments;

import moriyashiine.enchancement.common.Enchancement;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mixin(EnchantedBookItem.class)
public class EnchantedBookItemMixin {
	@Inject(method = "addEnchantment", at = @At("HEAD"), cancellable = true)
	private static void enchancement$disableDisallowedEnchantments(ItemStack stack, EnchantmentLevelEntry entry, CallbackInfo ci) {
		if (Enchancement.getConfig().isEnchantmentDisallowed(entry.enchantment)) {
			ci.cancel();
		}
	}

	@Inject(method = "appendStacks", at = @At("TAIL"))
	private void enchancement$disableDisallowedEnchantments(ItemGroup group, DefaultedList<ItemStack> stacks, CallbackInfo ci) {
		if (!Enchancement.getConfig().allowedEnchantmentIdentifiers.isEmpty()) {
			List<ItemStack> newStacks = new ArrayList<>();
			for (ItemStack stack : stacks) {
				for (Map.Entry<Enchantment, Integer> entry : EnchantmentHelper.get(stack).entrySet()) {
					if (!Enchancement.getConfig().isEnchantmentDisallowed(entry.getKey())) {
						newStacks.add(EnchantedBookItem.forEnchantment(new EnchantmentLevelEntry(entry.getKey(), entry.getValue())));
					}
				}
			}
			stacks.removeIf(stack -> stack.isOf(Items.ENCHANTED_BOOK));
			stacks.addAll(newStacks);
		}
	}
}
