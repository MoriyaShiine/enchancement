/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.felltrees;

import moriyashiine.enchancement.common.event.enchantmenteffectcomponenttype.FellTreesEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {
	protected PlayerMixin(EntityType<? extends LivingEntity> type, Level level) {
		super(type, level);
	}

	@Inject(method = "tick", at = @At("TAIL"))
	private void enchancement$fellTrees(CallbackInfo ci) {
		if (!swinging) {
			FellTreesEvent.ENTRIES.removeIf(entry -> entry.player() == (Object) this);
		}
	}
}
