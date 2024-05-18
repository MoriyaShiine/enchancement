/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.common.payload;

import moriyashiine.enchancement.client.payload.AddStrafeParticlesPayload;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.component.entity.StrafeComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.Vec3d;

public record StrafePayload(float velocityX, float velocityZ) implements CustomPayload {
	public static final CustomPayload.Id<StrafePayload> ID = CustomPayload.id(Enchancement.id("strafe").toString());
	public static final PacketCodec<PacketByteBuf, StrafePayload> CODEC = PacketCodec.tuple(PacketCodecs.FLOAT, StrafePayload::velocityX, PacketCodecs.FLOAT, StrafePayload::velocityZ, StrafePayload::new);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}

	public static void send(Vec3d velocity) {
		ClientPlayNetworking.send(new StrafePayload((float) velocity.getX(), (float) velocity.getZ()));
	}

	public static class Receiver implements ServerPlayNetworking.PlayPayloadHandler<StrafePayload> {
		@Override
		public void receive(StrafePayload payload, ServerPlayNetworking.Context context) {
			StrafeComponent strafeComponent = ModEntityComponents.STRAFE.get(context.player());
			if (strafeComponent.hasStrafe() && strafeComponent.canUse()) {
				strafeComponent.use(payload.velocityX(), payload.velocityZ());
				PlayerLookup.tracking(context.player()).forEach(foundPlayer -> AddStrafeParticlesPayload.send(foundPlayer, context.player().getId()));
			}
		}
	}
}
