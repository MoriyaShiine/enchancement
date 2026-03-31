/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.util.enchantment.effect;

import moriyashiine.enchancement.client.payload.UseLightningDashPayload;
import moriyashiine.enchancement.client.resources.sound.SparkSoundInstance;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.world.item.effects.LightningDashEffect;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.client.Minecraft;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

public class LightningDashMaceEffect extends MaceEffect {
	@Override
	public boolean canUse(RandomSource random, ItemStack stack) {
		return LightningDashEffect.getFloatTime(random, stack) != 0;
	}

	@Override
	public boolean isUsing(Player player) {
		return ModEntityComponents.LIGHTNING_DASH.get(player).isUsing();
	}

	@Override
	public void setUsing(Player player, boolean using) {
		ModEntityComponents.LIGHTNING_DASH.get(player).setUsing(using);
	}

	@Override
	public void use(Level level, Player player, ItemStack stack) {
		Vec3 lungeVelocity = player.getLookAngle().scale(LightningDashEffect.getLungeStrength(player.getRandom(), stack));
		int floatTicks = LightningDashEffect.getFloatTime(player.getRandom(), stack);
		LightningDashMaceEffect.useCommon(player, lungeVelocity, floatTicks);
		if (level.isClientSide()) {
			LightningDashMaceEffect.useClient(player);
		} else {
			LightningDashMaceEffect.useServer(player, lungeVelocity, floatTicks);
		}
	}

	public static void useCommon(Player player, Vec3 lungeVelocity, int floatTicks) {
		player.setDeltaMovement(lungeVelocity);
		player.gameEvent(GameEvent.ENTITY_ACTION);
		ModEntityComponents.LIGHTNING_DASH.get(player).setFloatTicks(floatTicks);
	}

	@Environment(EnvType.CLIENT)
	public static void useClient(Player player) {
		Minecraft.getInstance().getSoundManager().play(new SparkSoundInstance(player));
	}

	public static void useServer(Player player, Vec3 lungeVelocity, int floatTicks) {
		PlayerLookup.tracking(player).forEach(receiver -> UseLightningDashPayload.send(receiver, player, lungeVelocity, floatTicks));
	}
}
