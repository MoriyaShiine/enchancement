/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.enchancement.client.packet;

import io.netty.buffer.Unpooled;
import moriyashiine.enchancement.client.sound.BrimstoneSoundInstance;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.packet.StopBrimstoneSoundsC2SPacket;
import moriyashiine.enchancement.mixin.brimstone.client.SoundManagerAccessor;
import moriyashiine.enchancement.mixin.brimstone.client.SoundSystemAccessor;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.UUID;

public class StopBrimstoneSoundsS2CPacket {
	public static final Identifier ID = Enchancement.id("stop_brimstone_sounds_s2c");

	public static void send(ServerPlayerEntity player, UUID uuid) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeUuid(uuid);
		ServerPlayNetworking.send(player, ID, buf);
	}

	public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
		UUID uuid = buf.readUuid();
		client.execute(() -> stopSounds(client, uuid));
	}

	public static void stopSounds(MinecraftClient client, UUID uuid) {
		((SoundSystemAccessor) ((SoundManagerAccessor) client.getSoundManager()).enchancement$getSoundSystem()).enchancement$getSounds().values().forEach(sound -> {
			if (sound instanceof BrimstoneSoundInstance brimstoneSoundInstance && brimstoneSoundInstance.getUuid().equals(uuid)) {
				client.getSoundManager().stop(sound);
			}
		});
	}

	public static void maybeStopSounds(PlayerEntity player, ItemStack stack) {
		if (player.getItemUseTime() > 0 && stack.hasNbt()) {
			NbtCompound subNbt = stack.getSubNbt(Enchancement.MOD_ID);
			if (subNbt != null && subNbt.contains("BrimstoneUUID")) {
				UUID uuid = subNbt.getUuid("BrimstoneUUID");
				StopBrimstoneSoundsS2CPacket.stopSounds(MinecraftClient.getInstance(), uuid);
				StopBrimstoneSoundsC2SPacket.send(uuid);
			}
		}
	}
}
