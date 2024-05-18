/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.client.payload;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.component.entity.GaleComponent;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;

public record AddGaleParticlesPayload(int entityId) implements CustomPayload {
	public static final CustomPayload.Id<AddGaleParticlesPayload> ID = CustomPayload.id(Enchancement.id("add_gale_particles").toString());
	public static final PacketCodec<PacketByteBuf, AddGaleParticlesPayload> CODEC = PacketCodec.tuple(PacketCodecs.VAR_INT, AddGaleParticlesPayload::entityId, AddGaleParticlesPayload::new);

	@Override
	public CustomPayload.Id<? extends CustomPayload> getId() {
		return ID;
	}

	public static void send(ServerPlayerEntity player, int id) {
		ServerPlayNetworking.send(player, new AddGaleParticlesPayload(id));
	}

	public static class Receiver implements ClientPlayNetworking.PlayPayloadHandler<AddGaleParticlesPayload> {
		@Override
		public void receive(AddGaleParticlesPayload payload, ClientPlayNetworking.Context context) {
			Entity entity = context.player().getWorld().getEntityById(payload.entityId());
			if (entity != null) {
				GaleComponent.addGaleParticles(entity);
			}
		}
	}
}
