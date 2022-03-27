package moriyashiine.enchancement.client;

import moriyashiine.enchancement.client.packet.AddGaleParticlesPacket;
import moriyashiine.enchancement.client.packet.AddIceShardParticlesPacket;
import moriyashiine.enchancement.client.packet.AddMoltenParticlesPacket;
import moriyashiine.enchancement.client.render.entity.IceShardEntityRenderer;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.registry.ModEntityTypes;
import moriyashiine.enchancement.mixin.client.RenderLayerAccessor;
import moriyashiine.enchancement.mixin.client.RenderPhaseAccessor;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
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
	}
}
