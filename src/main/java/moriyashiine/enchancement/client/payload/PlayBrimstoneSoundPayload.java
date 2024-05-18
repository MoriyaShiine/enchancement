/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.client.payload;

import moriyashiine.enchancement.client.sound.BrimstoneSoundInstance;
import moriyashiine.enchancement.common.Enchancement;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Uuids;

import java.util.UUID;

public record PlayBrimstoneSoundPayload(int entityId, UUID uuid) implements CustomPayload {
	public static final CustomPayload.Id<PlayBrimstoneSoundPayload> ID = CustomPayload.id(Enchancement.id("play_brimstone_sound").toString());
	public static final PacketCodec<PacketByteBuf, PlayBrimstoneSoundPayload> CODEC = PacketCodec.tuple(PacketCodecs.VAR_INT, PlayBrimstoneSoundPayload::entityId, Uuids.PACKET_CODEC, PlayBrimstoneSoundPayload::uuid, PlayBrimstoneSoundPayload::new);

	@Override
	public CustomPayload.Id<? extends CustomPayload> getId() {
		return ID;
	}

	public static void send(ServerPlayerEntity player, int entityId, UUID uuid) {
		ServerPlayNetworking.send(player, new PlayBrimstoneSoundPayload(entityId, uuid));
	}

	public static class Receiver implements ClientPlayNetworking.PlayPayloadHandler<PlayBrimstoneSoundPayload> {
		@Override
		public void receive(PlayBrimstoneSoundPayload payload, ClientPlayNetworking.Context context) {
			Entity entity = context.player().getWorld().getEntityById(payload.entityId());
			if (entity != null) {
				context.client().getSoundManager().play(new BrimstoneSoundInstance(entity, payload.uuid(), entity.getSoundCategory()));
			}
		}
	}
}
