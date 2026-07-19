package moriyashiine.enchancement.mixin.config.disabledurability;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AnvilMenu.class)
public class AnvilMenuMixin {
	@ModifyExpressionValue(method = "lambda$onTake$0", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/RandomSource;nextFloat()F"))
	private static float enchancement$disableDurability(float original) {
		if (EnchancementUtil.isUnbreakable(Items.ANVIL.getDefaultInstance())) {
			return 1;
		}
		return original;
	}
}
