/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.client.packet;

import io.netty.buffer.Unpooled;
import moriyashiine.enchancement.client.sound.BrimstoneSoundInstance;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.packet.StopBrimstoneSoundsC2SPacket;
import moriyashiine.enchancement.mixin.brimstone.client.SoundManagerAccessor;
import moriyashiine.enchancement.mixin.brimstone.client.SoundSystemAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class StopBrimstoneSoundsS2CPacket {
	public static final Identifier ID = Enchancement.id("stop_brimstone_sounds_s2c");

	public static void send(ServerPlayerEntity player, UUID uuid) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeUuid(uuid);
		ServerPlayNetworking.send(player, ID, buf);
	}

	public static void stopSounds(Entity entity, ItemStack stack) {
		UUID brimstoneUUID = StopBrimstoneSoundsS2CPacket.getBrimstoneUUID(stack);
		if (brimstoneUUID != null) {
			stopSounds(entity, brimstoneUUID);
		}
	}

	public static void stopSounds(Entity entity, UUID brimstoneUUID) {
		PlayerLookup.tracking(entity).forEach(foundPlayer -> StopBrimstoneSoundsS2CPacket.send(foundPlayer, brimstoneUUID));
		if (entity instanceof ServerPlayerEntity serverPlayer) {
			StopBrimstoneSoundsS2CPacket.send(serverPlayer, brimstoneUUID);
		}
	}

	//client
	public static void maybeStopSounds(PlayerEntity player, ItemStack stack) {
		if (player.getItemUseTime() > 0) {
			UUID brimstoneUUID = getBrimstoneUUID(stack);
			if (brimstoneUUID != null) {
				StopBrimstoneSoundsS2CPacket.stopSounds(MinecraftClient.getInstance(), brimstoneUUID);
				StopBrimstoneSoundsC2SPacket.send(brimstoneUUID);
			}
		}
	}

	//client
	public static void stopSounds(MinecraftClient client, UUID uuid) {
		((SoundSystemAccessor) ((SoundManagerAccessor) client.getSoundManager()).enchancement$getSoundSystem()).enchancement$getSounds().values().forEach(sound -> {
			if (sound instanceof BrimstoneSoundInstance brimstoneSoundInstance && brimstoneSoundInstance.getUuid().equals(uuid)) {
				client.getSoundManager().stop(sound);
			}
		});
	}

	@Nullable
	public static UUID getBrimstoneUUID(ItemStack stack) {
		if (stack.hasNbt()) {
			NbtCompound subNbt = stack.getSubNbt(Enchancement.MOD_ID);
			if (subNbt != null && subNbt.contains("BrimstoneUUID")) {
				return subNbt.getUuid("BrimstoneUUID");
			}
		}
		return null;
	}

	@Environment(EnvType.CLIENT)
	public static class Receiver implements ClientPlayNetworking.PlayChannelHandler {
		@Override
		public void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
			UUID uuid = buf.readUuid();
			client.execute(() -> stopSounds(client, uuid));
		}
	}
}
