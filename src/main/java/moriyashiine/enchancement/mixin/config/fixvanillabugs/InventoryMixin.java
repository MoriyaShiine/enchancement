/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.fixvanillabugs;

import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Inventory.class)
public abstract class InventoryMixin {
	@Unique
	private int lastSelected = -1;

	@Shadow
	@Final
	public Player player;

	@Shadow
	public abstract int getSelectedSlot();

	@Inject(method = "setSelectedSlot", at = @At("HEAD"))
	private void enchancement$fixVanillaBugs(CallbackInfo ci) {
		lastSelected = getSelectedSlot();
	}

	@Inject(method = "setSelectedSlot", at = @At("TAIL"))
	private void enchancement$fixVanillaBugs(int selected, CallbackInfo ci) {
		if (ModConfig.fixVanillaBugs && selected != lastSelected) {
			EnchancementUtil.refreshAttributesAndCooldown(player);
		}
	}
}
