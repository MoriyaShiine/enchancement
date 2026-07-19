package moriyashiine.enchancement.common.payload;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.init.EnchancementEntityComponents;
import moriyashiine.strawberrylib.api.module.SLibUtils;
import moriyashiine.strawberrylib.api.objects.enums.ParticleAnchor;
import moriyashiine.strawberrylib.api.objects.enums.PayloadTarget;
import moriyashiine.strawberrylib.api.objects.records.ParticleVelocity;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;

public record AirJumpPayload(int entityId) implements CustomPacketPayload {
	public static final Type<AirJumpPayload> TYPE = new Type<>(Enchancement.id("air_jump"));
	public static final StreamCodec<FriendlyByteBuf, AirJumpPayload> CODEC = StreamCodec.composite(
			ByteBufCodecs.VAR_INT, AirJumpPayload::entityId,
			AirJumpPayload::new
	);

	@Override
	public Type<AirJumpPayload> type() {
		return TYPE;
	}

	public static void send(Entity entity) {
		ClientPlayNetworking.send(new AirJumpPayload(entity.getId()));
	}

	public static class Receiver implements ServerPlayNetworking.PlayPayloadHandler<AirJumpPayload> {
		@Override
		public void receive(AirJumpPayload payload, ServerPlayNetworking.Context context) {
			Entity entity = context.player().level().getEntity(payload.entityId());
			EnchancementEntityComponents.AIR_JUMP.maybeGet(entity).ifPresent(airJump -> {
				if (airJump.hasEffect() && airJump.canUse()) {
					airJump.use();
					SLibUtils.addParticles(entity, ParticleTypes.CLOUD, 8, ParticleAnchor.BASE, PayloadTarget.OTHERS, ParticleVelocity.ZERO);
				}
			});
		}
	}
}
