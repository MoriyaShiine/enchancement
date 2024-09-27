/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.event;

import moriyashiine.enchancement.common.ModConfig;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.enchantment.effect.TargetedEnchantmentEffect;
import net.minecraft.enchantment.effect.entity.IgniteEnchantmentEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;

public class RebalanceFireAspectEvent implements UseBlockCallback {
	@Override
	public ActionResult interact(PlayerEntity player, World world, Hand hand, BlockHitResult hitResult) {
		if (ModConfig.rebalanceFireAspect && player.isSneaking() && hasIgnite(player.getStackInHand(hand))) {
			ActionResult result = Items.FLINT_AND_STEEL.useOnBlock(new ItemUsageContext(player, hand, hitResult));
			if (result != ActionResult.FAIL) {
				return result;
			}
		}
		return ActionResult.PASS;
	}

	private static boolean hasIgnite(ItemStack stack) {
		for (RegistryEntry<Enchantment> enchantment : EnchantmentHelper.getEnchantments(stack).getEnchantments()) {
			for (TargetedEnchantmentEffect<EnchantmentEntityEffect> targetedEnchantmentEffect : enchantment.value().getEffect(EnchantmentEffectComponentTypes.POST_ATTACK)) {
				if (targetedEnchantmentEffect.effect() instanceof IgniteEnchantmentEffect) {
					return true;
				}
			}
		}
		return false;
	}
}
