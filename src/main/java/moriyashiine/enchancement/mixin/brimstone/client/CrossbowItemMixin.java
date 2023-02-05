/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.enchancement.mixin.brimstone.client;

import moriyashiine.enchancement.client.event.BrimstoneRenderEvent;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.registry.ModEnchantments;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Environment(EnvType.CLIENT)
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
	private void enchancement$brimstone(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context, CallbackInfo ci, List<ItemStack> list, ItemStack projectile) {
		if (ItemStack.areEqual(projectile, EnchancementUtil.BRIMSTONE_STACK)) {
			MutableText hearts = Texts.bracketed(Text.literal("\u2764").append(" x" + stack.getSubNbt(Enchancement.MOD_ID).getInt("BrimstoneDamage") / 2).formatted(Formatting.RED)).formatted(Formatting.DARK_RED);
			tooltip.add(Text.translatable("item.minecraft.crossbow.projectile").append(" ").append(Texts.bracketed(Text.translatable(ModEnchantments.BRIMSTONE.getTranslationKey()).append(" ").append(hearts))));
			ci.cancel();
		}
	}
}
