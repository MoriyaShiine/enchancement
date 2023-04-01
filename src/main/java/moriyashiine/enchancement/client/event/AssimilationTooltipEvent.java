/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.client.event;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.registry.ModEnchantments;
import moriyashiine.enchancement.common.registry.ModTags;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

@Environment(EnvType.CLIENT)
public class AssimilationTooltipEvent implements ItemTooltipCallback {
	@Override
	public void getTooltip(ItemStack stack, TooltipContext context, List<Text> lines) {
		if (MinecraftClient.getInstance().cameraEntity instanceof PlayerEntity player && stack.isIn(ModTags.Items.CANNOT_ASSIMILATE) && !stack.equals(player.getOffHandStack()) && EnchancementUtil.hasEnchantment(ModEnchantments.ASSIMILATION, player)) {
			lines.add(1, Text.translatable("tooltip." + Enchancement.MOD_ID + ".cannot_assimilate").formatted(Formatting.RED));
		}
	}
}
