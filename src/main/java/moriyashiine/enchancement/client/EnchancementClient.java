package moriyashiine.enchancement.client;

import moriyashiine.enchancement.client.packet.AddGaleParticlesPacket;
import moriyashiine.enchancement.client.packet.AddIceShardParticlesPacket;
import moriyashiine.enchancement.client.packet.AddMoltenParticlesPacket;
import moriyashiine.enchancement.client.render.entity.IceShardEntityRenderer;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.EnchancementUtil;
import moriyashiine.enchancement.common.packet.AttemptGaleJumpPacket;
import moriyashiine.enchancement.common.packet.SyncMovingForwardPacket;
import moriyashiine.enchancement.common.registry.ModComponents;
import moriyashiine.enchancement.common.registry.ModEnchantments;
import moriyashiine.enchancement.common.registry.ModEntityTypes;
import moriyashiine.enchancement.mixin.client.RenderLayerAccessor;
import moriyashiine.enchancement.mixin.client.RenderPhaseAccessor;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class EnchancementClient implements ClientModInitializer {
	public static final RenderLayer FROZEN = RenderLayerAccessor.enchancement$of("frozen", VertexFormats.POSITION, VertexFormat.DrawMode.QUADS, 256, false, false, RenderLayer.MultiPhaseParameters.builder().shader(RenderPhaseAccessor.enchancement$getTranslucentShader()).texture(RenderPhase.Textures.create().add(new Identifier(Enchancement.MOD_ID, "textures/entity/living/frozen.png"), false, false).build()).build(false));

	@Override
	public void onInitializeClient() {
		ClientPlayNetworking.registerGlobalReceiver(AddIceShardParticlesPacket.ID, AddIceShardParticlesPacket::receive);
		ClientPlayNetworking.registerGlobalReceiver(AddGaleParticlesPacket.ID, AddGaleParticlesPacket::receive);
		ClientPlayNetworking.registerGlobalReceiver(AddMoltenParticlesPacket.ID, AddMoltenParticlesPacket::receive);
		EntityRendererRegistry.register(ModEntityTypes.ICE_SHARD, IceShardEntityRenderer::new);
		initEvents();
	}

	private void initEvents() {
		ClientTickEvents.START_WORLD_TICK.register(new ClientTickEvents.StartWorldTick() {
			private int jumpCooldown = 0, timesJumped = 0, ticksInAir = 0;

			@Override
			public void onStartTick(ClientWorld world) {
				MinecraftClient client = MinecraftClient.getInstance();
				accelerationEffects(client);
				galeEffects(client);
			}

			@SuppressWarnings("ConstantConditions")
			private void accelerationEffects(MinecraftClient client) {
				ModComponents.MOVING_FORWARD.maybeGet(client.player).ifPresent(movingForwardComponent -> {
					if (client.player.forwardSpeed > 0) {
						if (!movingForwardComponent.isMovingForward() && EnchantmentHelper.getEquipmentLevel(ModEnchantments.ACCELERATION, client.player) > 0) {
							SyncMovingForwardPacket.send(true);
						}
					} else if (movingForwardComponent.isMovingForward()) {
						SyncMovingForwardPacket.send(false);
					}
				});
			}

			@SuppressWarnings("ConstantConditions")
			private void galeEffects(MinecraftClient client) {
				boolean onGround = client.player.isOnGround();
				if (!onGround) {
					ticksInAir++;
				} else {
					ticksInAir = 0;
				}
				if (jumpCooldown == 0) {
					if (!onGround) {
						if (ticksInAir >= 10 && timesJumped < 2 && client.options.jumpKey.isPressed() && EnchancementUtil.isGroundedOrJumping(client.player) && EnchantmentHelper.getEquipmentLevel(ModEnchantments.GALE, client.player) > 0) {
							jumpCooldown = 10;
							timesJumped++;
							AttemptGaleJumpPacket.send();
						}
					} else {
						timesJumped = 0;
					}
				} else if (jumpCooldown > 0) {
					jumpCooldown--;
				}
			}
		});
	}
}
