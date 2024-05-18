/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.common.payload;

import moriyashiine.enchancement.client.payload.AddGaleParticlesPayload;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.component.entity.GaleComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record GalePayload() implements CustomPayload {
	public static final CustomPayload.Id<GalePayload> ID = CustomPayload.id(Enchancement.id("gale").toString());
	public static final PacketCodec<PacketByteBuf, GalePayload> CODEC = PacketCodec.unit(new GalePayload());

	@Override
	public CustomPayload.Id<? extends CustomPayload> getId() {
		return ID;
	}

	public static void send() {
		ClientPlayNetworking.send(new GalePayload());
	}

	public static class Receiver implements ServerPlayNetworking.PlayPayloadHandler<GalePayload> {
		@Override
		public void receive(GalePayload payload, ServerPlayNetworking.Context context) {
			GaleComponent galeComponent = ModEntityComponents.GALE.get(context.player());
			if (galeComponent.hasGale() && galeComponent.canUse()) {
				galeComponent.use();
				PlayerLookup.tracking(context.player()).forEach(foundPlayer -> AddGaleParticlesPayload.send(foundPlayer, context.player().getId()));
			}
		}
	}
}
