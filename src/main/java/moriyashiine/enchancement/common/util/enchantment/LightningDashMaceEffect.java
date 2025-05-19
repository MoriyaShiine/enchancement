/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.util.enchantment;

import moriyashiine.enchancement.client.payload.UseLightningDashPayload;
import moriyashiine.enchancement.client.sound.SparkSoundInstance;
import moriyashiine.enchancement.common.enchantment.effect.LightningDashEffect;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class LightningDashMaceEffect extends MaceEffect {
	@Override
	public boolean canUse(Random random, ItemStack stack) {
		return LightningDashEffect.getFloatTime(random, stack) != 0;
	}

	@Override
	public boolean isUsing(PlayerEntity player) {
		return ModEntityComponents.LIGHTNING_DASH.get(player).isUsing();
	}

	@Override
	public void setUsing(PlayerEntity player, boolean using) {
		ModEntityComponents.LIGHTNING_DASH.get(player).setUsing(using);
	}

	@Override
	public void use(World world, PlayerEntity player, ItemStack stack) {
		Vec3d lungeVelocity = player.getRotationVector().multiply(LightningDashEffect.getLungeStrength(player.getRandom(), stack));
		int floatTicks = LightningDashEffect.getFloatTime(player.getRandom(), stack);
		LightningDashMaceEffect.useCommon(player, lungeVelocity, floatTicks);
		if (world.isClient) {
			LightningDashMaceEffect.useClient(player);
		} else {
			LightningDashMaceEffect.useServer(player, lungeVelocity, floatTicks);
		}
	}

	public static void useCommon(PlayerEntity player, Vec3d lungeVelocity, int floatTicks) {
		player.setVelocity(lungeVelocity);
		player.emitGameEvent(GameEvent.ENTITY_ACTION);
		ModEntityComponents.LIGHTNING_DASH.get(player).setFloatTicks(floatTicks);
	}

	@Environment(EnvType.CLIENT)
	public static void useClient(PlayerEntity player) {
		MinecraftClient.getInstance().getSoundManager().play(new SparkSoundInstance(player));
	}

	public static void useServer(PlayerEntity player, Vec3d lungeVelocity, int floatTicks) {
		PlayerLookup.tracking(player).forEach(foundPlayer -> UseLightningDashPayload.send(foundPlayer, player, lungeVelocity, floatTicks));
	}
}
