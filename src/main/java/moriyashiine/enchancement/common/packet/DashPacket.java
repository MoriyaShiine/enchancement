package moriyashiine.enchancement.common.packet;

import io.netty.buffer.Unpooled;
import moriyashiine.enchancement.client.packet.AddDashParticlesPacket;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.component.entity.DashComponent;
import moriyashiine.enchancement.common.registry.ModEnchantments;
import moriyashiine.enchancement.common.registry.ModSoundEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

public class DashPacket {
	public static final Identifier ID = new Identifier(Enchancement.MOD_ID, "dash");

	public static void send(Vec3d velocity) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeDouble(velocity.getX());
		buf.writeDouble(velocity.getY());
		buf.writeDouble(velocity.getZ());
		ClientPlayNetworking.send(ID, buf);
	}

	public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
		double vecX = buf.readDouble();
		double vecY = buf.readDouble();
		double vecZ = buf.readDouble();
		server.execute(() -> {
			if (EnchantmentHelper.getEquipmentLevel(ModEnchantments.DASH, player) > 0) {
				PlayerLookup.tracking(player).forEach(foundPlayer -> AddDashParticlesPacket.send(foundPlayer, player));
				AddDashParticlesPacket.send(player, player);
				player.world.playSoundFromEntity(null, player, ModSoundEvents.ENTITY_GENERIC_DASH, player.getSoundCategory(), 1, 1);
				player.setVelocity(vecX, vecY, vecZ);
				player.velocityModified = true;
				player.fallDistance = 0;
				DashComponent.PACKET_IMMUNITIES.put(player, 5);
			}
		});
	}
}
