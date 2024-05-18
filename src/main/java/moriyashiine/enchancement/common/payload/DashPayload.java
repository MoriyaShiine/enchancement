/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.common.payload;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.component.entity.DashComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record DashPayload() implements CustomPayload {
	public static final CustomPayload.Id<DashPayload> ID = CustomPayload.id(Enchancement.id("dash").toString());
	public static final PacketCodec<PacketByteBuf, DashPayload> CODEC = PacketCodec.unit(new DashPayload());

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}

	public static void send() {
		ClientPlayNetworking.send(new DashPayload());
	}

	public static class Receiver implements ServerPlayNetworking.PlayPayloadHandler<DashPayload> {
		@Override
		public void receive(DashPayload payload, ServerPlayNetworking.Context context) {
			DashComponent dashComponent = ModEntityComponents.DASH.get(context.player());
			if (dashComponent.hasDash() && dashComponent.canUse()) {
				EnchancementUtil.PACKET_IMMUNITIES.put(context.player(), 20);
				dashComponent.use();
			}
		}
	}
}
