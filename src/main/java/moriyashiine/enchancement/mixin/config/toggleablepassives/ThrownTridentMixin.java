/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.toggleablepassives;

import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.tag.ModEntityTypeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.entity.projectile.arrow.ThrownTrident;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ThrownTrident.class)
public abstract class ThrownTridentMixin extends AbstractArrow {
	protected ThrownTridentMixin(EntityType<? extends AbstractArrow> type, Level level) {
		super(type, level);
	}

	@ModifyVariable(method = "tick", at = @At("STORE"), name = "loyalty")
	private int enchancement$disableLoyaltyOnNonPlayerTridents(int loyalty) {
		return shouldDisableLoyalty(this) ? 0 : loyalty;
	}

	@ModifyVariable(method = "tickDespawn", at = @At("STORE"), name = "loyalty")
	private int enchancement$ageNonPlayerTridents(int loyalty) {
		return shouldDisableLoyalty(this) ? 0 : loyalty;
	}

	@Unique
	private static boolean shouldDisableLoyalty(AbstractArrow arrow) {
		if (ModConfig.toggleablePassives) {
			if (arrow.is(ModEntityTypeTags.NO_LOYALTY)) {
				return true;
			}
			return !(arrow.getOwner() instanceof Player);
		}
		return false;
	}
}
