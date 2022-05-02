/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.enchancement.mixin.brimstone.client;

import moriyashiine.enchancement.common.registry.ModEnchantments;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Environment(EnvType.CLIENT)
@Mixin(CrossbowItem.class)
public class CrossbowItemMixin {
	@Unique
	private static Text BRIMSTONE_TEXT;

	@Inject(method = "appendTooltip", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
	private void enchancement$brimstone(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context, CallbackInfo ci, List<ItemStack> list, ItemStack projectile) {
		if (ItemStack.areEqual(projectile, EnchancementUtil.BRIMSTONE_STACK)) {
			if (BRIMSTONE_TEXT == null) {
				BRIMSTONE_TEXT = new TranslatableText("item.minecraft.crossbow.projectile").append(" ").append(Texts.bracketed(new TranslatableText(ModEnchantments.BRIMSTONE.getTranslationKey())));
			}
			tooltip.add(BRIMSTONE_TEXT);
			ci.cancel();
		}
	}
}
