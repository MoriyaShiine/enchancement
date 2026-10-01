package moriyashiine.enchancement.mixin.config.enchantedbookgrinding;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.EnchancementConfig;
import net.minecraft.world.inventory.GrindstoneMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GrindstoneMenu.class)
public class GrindstoneMenuMixin {
	@ModifyReturnValue(method = "computeResult", at = @At("RETURN"))
	private ItemStack enchancement$enchantedBookGrinding(ItemStack original, ItemStack input, ItemStack additional) {
		if (EnchancementConfig.enchantedBookGrinding && input.isEnchanted() && additional.is(Items.BOOK)) {
			ItemStack book = Items.ENCHANTED_BOOK.getDefaultInstance();
			EnchantmentHelper.setEnchantments(book, input.getEnchantments());
			return book;
		}
		return original;
	}
}
