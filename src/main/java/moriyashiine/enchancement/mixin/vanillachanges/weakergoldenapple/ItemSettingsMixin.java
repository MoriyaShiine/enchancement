/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.vanillachanges.weakergoldenapple;

import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.FoodComponents;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Item.Settings.class)
public class ItemSettingsMixin {
	@Unique
	private static final FoodComponent WEAKER_GOLDEN_APPLE = new FoodComponent.Builder().hunger(4).saturationModifier(0.75F).statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 100), 1).statusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 2400), 1).alwaysEdible().build();

	@ModifyVariable(method = "food", at = @At("HEAD"), argsOnly = true)
	private FoodComponent enchancement$weakerGoldenApple(FoodComponent value) {
		if (ModConfig.weakerGoldenApple && value == FoodComponents.GOLDEN_APPLE) {
			return WEAKER_GOLDEN_APPLE;
		}
		return value;
	}
}
