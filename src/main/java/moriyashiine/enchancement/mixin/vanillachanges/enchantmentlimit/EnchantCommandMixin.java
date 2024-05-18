/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.vanillachanges.enchantmentlimit;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.EnchantCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(EnchantCommand.class)
public class EnchantCommandMixin {
	@Unique
	private static ItemStack cachedStack = null;

	@ModifyVariable(method = "execute", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/entity/LivingEntity;getMainHandStack()Lnet/minecraft/item/ItemStack;"))
	private static ItemStack enchancement$enchantmentLimit(ItemStack value) {
		cachedStack = value;
		return value;
	}

	@ModifyExpressionValue(method = "execute", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentHelper;isCompatible(Ljava/util/Collection;Lnet/minecraft/enchantment/Enchantment;)Z"))
	private static boolean enchancement$enchantmentLimit(boolean value) {
		if (EnchancementUtil.limitCheck(false, EnchancementUtil.getNonDefaultEnchantmentsSize(cachedStack, cachedStack.getEnchantments().getSize()) >= ModConfig.enchantmentLimit)) {
			value = false;
		}
		cachedStack = null;
		return value;
	}
}
