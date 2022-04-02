package moriyashiine.enchancement.mixin.vanillachanges;

import moriyashiine.enchancement.common.Enchancement;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Item.class)
public class ItemMixin {
	@Shadow
	private int maxDamage;

	@SuppressWarnings("ConstantConditions")
	@Inject(method = "<init>", at = @At("TAIL"))
	private void enchancement$durablityChanges(Item.Settings settings, CallbackInfo ci) {
		if (Item.class.cast(this) instanceof FishingRodItem) {
			maxDamage = Enchancement.getConfig().fishingRodDurability;
		}
		float multiplier;
		if (Item.class.cast(this) instanceof ArmorItem) {
			multiplier = Enchancement.getConfig().armorDurabilityMultiplier;
			if (multiplier <= 0) {
				throw new IllegalArgumentException("Armor durability multiplier cannot be less than or equal to 0");
			}
		} else {
			multiplier = Enchancement.getConfig().toolDurabilityMultiplier;
			if (multiplier <= 0) {
				throw new IllegalArgumentException("Tool durability multiplier cannot be less than or equal to 0");
			}
		}
		maxDamage *= multiplier;
	}
}
