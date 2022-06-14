/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.enchancement.mixin.vanillachanges.singlelevelmode;

import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.loot.function.SetEnchantmentsLootFunction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(SetEnchantmentsLootFunction.class)
public class SetEnchantmentsLootFunctionMixin {
	@ModifyVariable(method = "addEnchantmentToMap", at = @At("HEAD"), argsOnly = true)
	private static int enchancement$singleLevelMode(int value) {
		if (ModConfig.singleLevelMode) {
			return 1;
		}
		return value;
	}
}
