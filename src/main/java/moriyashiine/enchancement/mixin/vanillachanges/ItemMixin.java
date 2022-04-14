package moriyashiine.enchancement.mixin.vanillachanges;

import moriyashiine.enchancement.common.Enchancement;
import net.minecraft.item.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Item.class)
public class ItemMixin {
	@Shadow
	private int maxDamage;

	@Inject(method = "<init>", at = @At("TAIL"))
	private void enchancement$durablityChanges(Item.Settings settings, CallbackInfo ci) {
		Item item = Item.class.cast(this);
		if (item instanceof FishingRodItem) {
			maxDamage = Enchancement.getConfig().fishingRodDurability;
		}
		float multiplier = 1;
		if (item instanceof ArmorItem) {
			multiplier = Enchancement.getConfig().armorDurabilityMultiplier;
			if (multiplier <= 0) {
				throw new IllegalArgumentException("Armor durability multiplier cannot be less than or equal to 0");
			}
		} else if (item instanceof ToolItem || item instanceof RangedWeaponItem || item instanceof TridentItem || item instanceof FlintAndSteelItem || item instanceof ShearsItem || item instanceof ShieldItem) {
			multiplier = Enchancement.getConfig().toolDurabilityMultiplier;
			if (multiplier <= 0) {
				throw new IllegalArgumentException("Tool durability multiplier cannot be less than or equal to 0");
			}
		}
		maxDamage *= multiplier;
	}
}
