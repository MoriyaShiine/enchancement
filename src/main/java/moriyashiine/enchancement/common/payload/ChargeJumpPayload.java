/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.payload;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record ChargeJumpPayload(boolean pressingChargeJump) implements CustomPacketPayload {
	public static final Type<ChargeJumpPayload> TYPE = new Type<>(Enchancement.id("charge_jump"));
	public static final StreamCodec<FriendlyByteBuf, ChargeJumpPayload> CODEC = StreamCodec.composite(
			ByteBufCodecs.BOOL, ChargeJumpPayload::pressingChargeJump,
			ChargeJumpPayload::new);

	@Override
	public Type<ChargeJumpPayload> type() {
		return TYPE;
	}

	public static void send(boolean pressingChargeJump) {
		ClientPlayNetworking.send(new ChargeJumpPayload(pressingChargeJump));
	}

	public static class Receiver implements ServerPlayNetworking.PlayPayloadHandler<ChargeJumpPayload> {
		@Override
		public void receive(ChargeJumpPayload payload, ServerPlayNetworking.Context context) {
			ModEntityComponents.CHARGE_JUMP.get(context.player()).setPressingChargeJump(payload.pressingChargeJump());
		}
	}
}
