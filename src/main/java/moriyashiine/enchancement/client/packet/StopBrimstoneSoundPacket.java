/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.enchancement.client.packet;

import io.netty.buffer.Unpooled;
import moriyashiine.enchancement.client.sound.BrimstoneSoundInstance;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.mixin.brimstone.client.SoundManagerAccessor;
import moriyashiine.enchancement.mixin.brimstone.client.SoundSystemAccessor;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.UUID;

public class StopBrimstoneSoundPacket {
	public static final Identifier ID = Enchancement.id("stop_brimstone_sound");

	public static void send(ServerPlayerEntity player, UUID uuid) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeUuid(uuid);
		ServerPlayNetworking.send(player, ID, buf);
	}

	public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
		UUID uuid = buf.readUuid();
		client.execute(() -> {
			((SoundSystemAccessor) ((SoundManagerAccessor) client.getSoundManager()).enchancement$getSoundSystem()).enchancement$getSounds().values().forEach(sound -> {
				if (sound instanceof BrimstoneSoundInstance brimstoneSoundInstance && brimstoneSoundInstance.getUuid().equals(uuid)) {
					client.getSoundManager().stop(sound);
				}
			});
		});
	}
}
