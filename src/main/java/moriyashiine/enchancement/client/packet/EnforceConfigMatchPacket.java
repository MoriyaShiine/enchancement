/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.client.packet;

import eu.midnightdust.lib.util.PlatformFunctions;
import io.netty.buffer.Unpooled;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.ModConfig;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class EnforceConfigMatchPacket {
	public static final Identifier ID = Enchancement.id("enforce_config_match");

	private static final Text DISCONNECT_TEXT = Text.literal("The server you are attempting to connect to has ")
			.append(Text.literal("Enchancement").formatted(Formatting.GREEN))
			.append(" installed, but your configuration file does not match the server's.\n\n")
			.append(Text.literal("Please make sure your configuration file matches the server's.\n").formatted(Formatting.RED))
			.append(Text.literal("Your configuration file is located at ").formatted(Formatting.RED))
			.append(Text.literal(PlatformFunctions.getConfigDirectory().resolve(Enchancement.MOD_ID + ".json").toString()).formatted(Formatting.BLUE))
			.append(Text.literal(".\n\n").formatted(Formatting.RED))
			.append(Text.literal("This is not a bug, do not report it.").formatted(Formatting.DARK_RED, Formatting.BOLD));

	public static void send(ServerPlayerEntity player, int encoding) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeInt(encoding);
		ServerPlayNetworking.send(player, ID, buf);
	}

	public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
		int encoding = buf.readInt();
		client.execute(() -> {
			if (ModConfig.encode() != encoding) {
				handler.getConnection().disconnect(DISCONNECT_TEXT);
			}
		});
	}
}
