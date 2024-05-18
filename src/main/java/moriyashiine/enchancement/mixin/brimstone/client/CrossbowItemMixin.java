/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.brimstone.client;

import moriyashiine.enchancement.client.event.BrimstoneRenderEvent;
import moriyashiine.enchancement.common.init.ModDataComponentTypes;
import moriyashiine.enchancement.common.init.ModEnchantments;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.client.item.TooltipType;
import net.minecraft.component.type.ChargedProjectilesComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(CrossbowItem.class)
public abstract class CrossbowItemMixin {
	@Shadow
	public abstract int getMaxUseTime(ItemStack stack);

	@Shadow
	private static float getPullProgress(int useTicks, ItemStack stack) {
		throw new UnsupportedOperationException();
	}

	@Inject(method = "usageTick", at = @At("HEAD"))
	private void enchancement$brimstone(World world, LivingEntity user, ItemStack stack, int remainingUseTicks, CallbackInfo ci) {
		BrimstoneRenderEvent.health = EnchancementUtil.getBrimstoneDamage(getPullProgress(getMaxUseTime(stack) - remainingUseTicks, stack));
	}

	@Inject(method = "appendTooltip", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
	private void enchancement$brimstone(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type, CallbackInfo ci, ChargedProjectilesComponent chargedProjectilesComponent, ItemStack projectile) {
		if (stack.contains(ModDataComponentTypes.BRIMSTONE_DAMAGE)) {
			MutableText hearts = Texts.bracketed(Text.literal("❤").append(" x" + stack.get(ModDataComponentTypes.BRIMSTONE_DAMAGE) / 2).formatted(Formatting.RED)).formatted(Formatting.DARK_RED);
			tooltip.add(Text.translatable("item.minecraft.crossbow.projectile").append(" ").append(Texts.bracketed(Text.translatable(ModEnchantments.BRIMSTONE.getTranslationKey()).append(" ").append(hearts))));
			ci.cancel();
		}
	}
}
