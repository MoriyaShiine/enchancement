/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.brimstone.client;

import moriyashiine.enchancement.client.event.enchantmenteffectcomponenttype.BrimstoneClientEvent;
import moriyashiine.enchancement.common.enchantment.effect.BrimstoneEffect;
import moriyashiine.enchancement.common.init.ModComponentTypes;
import moriyashiine.strawberrylib.api.module.SLibClientUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(CrossbowItem.class)
public abstract class CrossbowItemMixin {
	@Shadow
	public abstract int getMaxUseTime(ItemStack stack, LivingEntity user);

	@Inject(method = "usageTick", at = @At("HEAD"))
	private void enchancement$brimstone(World world, LivingEntity user, ItemStack stack, int remainingUseTicks, CallbackInfo ci) {
		if (SLibClientUtils.isHost(user)) {
			BrimstoneClientEvent.health = BrimstoneEffect.getBrimstoneDamage(CrossbowItem.getPullProgress(getMaxUseTime(stack, user) - remainingUseTicks, stack, user));
		}
	}

	@Inject(method = "appendTooltip", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"), cancellable = true)
	private void enchancement$brimstone(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type, CallbackInfo ci) {
		if (stack.contains(ModComponentTypes.BRIMSTONE_DAMAGE)) {
			MutableText hearts = Texts.bracketed(Text.literal("❤").append(" x" + stack.get(ModComponentTypes.BRIMSTONE_DAMAGE) / 2).formatted(Formatting.RED)).formatted(Formatting.DARK_RED);
			tooltip.add(Text.translatable("item.minecraft.crossbow.projectile").append(" ").append(Texts.bracketed(Text.translatable("enchantment.enchancement.brimstone").append(" ").append(hearts))));
			ci.cancel();
		}
	}
}
