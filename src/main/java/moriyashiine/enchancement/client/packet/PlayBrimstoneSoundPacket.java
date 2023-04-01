/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.client.packet;

import io.netty.buffer.Unpooled;
import moriyashiine.enchancement.client.sound.BrimstoneSoundInstance;
import moriyashiine.enchancement.common.Enchancement;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.UUID;

public class PlayBrimstoneSoundPacket {
	public static final Identifier ID = Enchancement.id("play_brimstone_sound");

	public static void send(ServerPlayerEntity player, int entityId, UUID uuid) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeInt(entityId);
		buf.writeUuid(uuid);
		ServerPlayNetworking.send(player, ID, buf);
	}

	public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
		int entityId = buf.readInt();
		UUID uuid = buf.readUuid();
		//noinspection Convert2Lambda
		client.execute(new Runnable() {
			@Override
			public void run() {
				Entity entity = handler.getWorld().getEntityById(entityId);
				if (entity != null) {
					client.getSoundManager().play(new BrimstoneSoundInstance(entity, uuid, entity.getSoundCategory()));
				}
			}
		});
	}
}
