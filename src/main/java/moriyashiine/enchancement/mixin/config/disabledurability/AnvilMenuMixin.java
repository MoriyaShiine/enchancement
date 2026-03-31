/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.disabledurability;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.tag.ModItemTags;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AnvilMenu.class)
public class AnvilMenuMixin {
	@ModifyExpressionValue(method = "lambda$onTake$0", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/RandomSource;nextFloat()F"))
	private static float enchancement$disableDurability(float original) {
		if (ModConfig.disableDurability && !Items.ANVIL.getDefaultInstance().is(ModItemTags.RETAINS_DURABILITY)) {
			return 1;
		}
		return original;
	}

	@WrapOperation(method = "createResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isDamageableItem()Z"))
	private boolean enchancement$disableDurability(ItemStack instance, Operation<Boolean> original) {
		return original.call(instance) || EnchancementUtil.isUnbreakable(instance);
	}
}
