/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.vanillachanges.disabledisallowedenchantments.integration.spectrum;

import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.network.packet.s2c.play.SynchronizeRecipesS2CPacket;
import net.minecraft.recipe.RecipeEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Mixin(SynchronizeRecipesS2CPacket.class)
public class SynchronizeRecipesS2CPacketMixin {
	@ModifyVariable(method = "<init>(Ljava/util/Collection;)V", at = @At("HEAD"), argsOnly = true)
	private static Collection<RecipeEntry<?>> enchancement$spectrum$disableDisallowedEnchantments(Collection<RecipeEntry<?>> value) {
		List<RecipeEntry<?>> recipes = new ArrayList<>(value);
		for (int i = recipes.size() - 1; i >= 0; i--) {
			if (EnchancementUtil.ignoreRecipe(recipes.get(i))) {
				recipes.remove(i);
			}
		}
		return recipes;
	}
}
