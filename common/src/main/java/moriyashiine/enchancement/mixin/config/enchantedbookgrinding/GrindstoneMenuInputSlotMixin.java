package moriyashiine.enchancement.mixin.config.enchantedbookgrinding;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.EnchancementConfig;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "net.minecraft.world.inventory.GrindstoneMenu$3")
public class GrindstoneMenuInputSlotMixin {
	@ModifyReturnValue(method = "mayPlace", at = @At("RETURN"))
	private boolean enchancement$enchantedBookGrinding(boolean original, ItemStack itemStack) {
		return original || (EnchancementConfig.enchantedBookGrinding && itemStack.is(Items.BOOK));
	}
}
