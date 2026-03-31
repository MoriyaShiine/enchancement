/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.client.payload;

import eu.midnightdust.lib.util.PlatformFunctions;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.ModConfig;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

public record EnforceConfigMatchPayload(int encoding) implements CustomPacketPayload {
	public static final Type<EnforceConfigMatchPayload> TYPE = new Type<>(Enchancement.id("enforce_config_match"));
	public static final StreamCodec<FriendlyByteBuf, EnforceConfigMatchPayload> CODEC = StreamCodec.composite(
			ByteBufCodecs.VAR_INT, EnforceConfigMatchPayload::encoding,
			EnforceConfigMatchPayload::new);

	private static final Component DISCONNECT_TEXT = Component.literal("The server you are attempting to connect to has ")
			.append(Component.literal("Enchancement").withStyle(ChatFormatting.GREEN))
			.append(" installed, but your configuration file does not match the server's.\n\n")
			.append(Component.literal("Please make sure your configuration file matches the server's.\n").withStyle(ChatFormatting.RED))
			.append(Component.literal("Your configuration file is located at ").withStyle(ChatFormatting.RED))
			.append(Component.literal(PlatformFunctions.getConfigDirectory().resolve(Enchancement.MOD_ID + ".json").toString()).withStyle(ChatFormatting.BLUE))
			.append(Component.literal(".\n\n").withStyle(ChatFormatting.RED))
			.append(Component.literal("This is not a bug, do not report it.").withStyle(ChatFormatting.DARK_RED, ChatFormatting.BOLD));

	@Override
	public Type<EnforceConfigMatchPayload> type() {
		return TYPE;
	}

	public static void send(ServerPlayer player, int encoding) {
		ServerPlayNetworking.send(player, new EnforceConfigMatchPayload(encoding));
	}

	public static class Receiver implements ClientPlayNetworking.PlayPayloadHandler<EnforceConfigMatchPayload> {
		@Override
		public void receive(EnforceConfigMatchPayload payload, ClientPlayNetworking.Context context) {
			if (ModConfig.encode() != payload.encoding()) {
				context.player().connection.getConnection().disconnect(DISCONNECT_TEXT);
			}
		}
	}
}
