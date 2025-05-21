/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.rebalanceequipment;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.init.ModEnchantments;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BundleContentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.BundleItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.MutableText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Mixin(BundleItem.class)
public class BundleItemMixin {
	@WrapOperation(method = "getTooltipData", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;get(Lnet/minecraft/component/ComponentType;)Ljava/lang/Object;"))
	private Object enchancement$rebalanceEquipment(ItemStack instance, ComponentType<BundleContentsComponent> componentType, Operation<Object> original) {
		Object component = original.call(instance, componentType);
		if (ModConfig.rebalanceEquipment && component instanceof BundleContentsComponent bundleContentsComponent) {
			BundleContentsComponent.Builder builder = new BundleContentsComponent.Builder(bundleContentsComponent);
			for (int i = 0; i < bundleContentsComponent.size(); i++) {
				ItemStack stack = bundleContentsComponent.get(i);
				if (stack.isOf(Items.ENCHANTED_BOOK)) {
					ItemStack copy = stack.copy();
					MutableText name = stack.getName().copy().append(" (");

					List<RegistryEntry<Enchantment>> enchantments = new ArrayList<>(EnchantmentHelper.getEnchantments(copy).getEnchantments());
					enchantments.sort(Comparator.comparing(e -> e.getKey().orElse(ModEnchantments.EMPTY_KEY).getValue().getPath()));
					for (int j = 0; j < enchantments.size(); j++) {
						name.append(enchantments.get(j).value().description());
						if (j < enchantments.size() - 1) {
							name.append(", ");
						}
					}

					copy.set(DataComponentTypes.ITEM_NAME, name.append(")"));
					builder.stacks.set(i, copy);
				}
			}
			return builder.build();
		}
		return component;
	}
}
