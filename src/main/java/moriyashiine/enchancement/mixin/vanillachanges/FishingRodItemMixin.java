package moriyashiine.enchancement.mixin.vanillachanges;

import moriyashiine.enchancement.common.Enchancement;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(FishingRodItem.class)
public class FishingRodItemMixin {
	@ModifyVariable(method = "<init>", at = @At("HEAD"), argsOnly = true)
	private static Item.Settings enchancement$fishingRodDurability(Item.Settings value) {
		return value.maxDamage(Enchancement.getConfig().fishingRodDurability);
	}
}
