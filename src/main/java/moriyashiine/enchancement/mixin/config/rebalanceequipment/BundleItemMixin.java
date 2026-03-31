/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.rebalanceequipment;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.init.ModEnchantments;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.BundleContents;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Mixin(BundleItem.class)
public class BundleItemMixin {
	@WrapOperation(method = "getTooltipImage", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;get(Lnet/minecraft/core/component/DataComponentType;)Ljava/lang/Object;"))
	private Object enchancement$rebalanceEquipment(ItemStack instance, DataComponentType<BundleContents> componentType, Operation<Object> original) {
		Object component = original.call(instance, componentType);
		if (ModConfig.rebalanceEquipment && component instanceof BundleContents bundleContentsComponent) {
			BundleContents.Mutable mutable = new BundleContents.Mutable(bundleContentsComponent);
			for (int i = 0; i < bundleContentsComponent.size(); i++) {
				ItemStackTemplate template = bundleContentsComponent.items().get(i);
				if (template.is(Items.ENCHANTED_BOOK)) {
					ItemStack stack = template.create();
					MutableComponent name = stack.getHoverName().copy().append(" (");

					List<Holder<Enchantment>> enchantments = new ArrayList<>(EnchantmentHelper.getEnchantmentsForCrafting(stack).keySet());
					enchantments.sort(Comparator.comparing(e -> e.unwrapKey().orElse(ModEnchantments.EMPTY_KEY).identifier().getPath()));
					for (int j = 0; j < enchantments.size(); j++) {
						name.append(enchantments.get(j).value().description());
						if (j < enchantments.size() - 1) {
							name.append(", ");
						}
					}

					stack.set(DataComponents.ITEM_NAME, name.append(")"));
					mutable.items.set(i, stack);
				}
			}
			return mutable.toImmutable();
		}
		return component;
	}
}
