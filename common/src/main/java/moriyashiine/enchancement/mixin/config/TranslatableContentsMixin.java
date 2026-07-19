package moriyashiine.enchancement.mixin.config;

import moriyashiine.enchancement.common.EnchancementConfig;
import net.minecraft.network.chat.contents.TranslatableContents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(TranslatableContents.class)
public class TranslatableContentsMixin {
	@ModifyVariable(method = "<init>", at = @At("HEAD"), argsOnly = true, ordinal = 0)
	private static String enchancement$redirectKey(String key) {
		return switch (key) {
			case "advancements.adventure.two_birds_one_arrow.description" -> !EnchancementConfig.disallowedEnchantments.contains("enchancement:brimstone") ? key + ".redirect" : key;
			case "advancements.adventure.overoverkill.description" -> EnchancementConfig.rebalanceEquipment ? key + ".redirect" : key;
			case "enchantment.minecraft.channeling.desc", "enchantment.minecraft.fire_aspect", "enchantment.minecraft.luck_of_the_sea.desc", "enchantment.minecraft.wind_burst.desc" ->
					EnchancementConfig.rebalanceEnchantments ? key + ".redirect" : key;
			default -> key;
		};
	}
}
