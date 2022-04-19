package moriyashiine.enchancement.client;

import moriyashiine.enchancement.client.packet.AddMoltenParticlesPacket;
import moriyashiine.enchancement.client.packet.SyncFrozenPlayerSlimStatusS2C;
import moriyashiine.enchancement.client.reloadlisteners.FrozenReloadListener;
import moriyashiine.enchancement.client.render.entity.IceShardEntityRenderer;
import moriyashiine.enchancement.client.render.entity.mob.FrozenPlayerEntityRenderer;
import moriyashiine.enchancement.common.registry.ModEntityTypes;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;

@Environment(EnvType.CLIENT)
public class EnchancementClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ClientPlayNetworking.registerGlobalReceiver(SyncFrozenPlayerSlimStatusS2C.ID, SyncFrozenPlayerSlimStatusS2C::receive);
		ClientPlayNetworking.registerGlobalReceiver(AddMoltenParticlesPacket.ID, AddMoltenParticlesPacket::receive);
		EntityRendererRegistry.register(ModEntityTypes.FROZEN_PLAYER, FrozenPlayerEntityRenderer::new);
		EntityRendererRegistry.register(ModEntityTypes.ICE_SHARD, IceShardEntityRenderer::new);
		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(FrozenReloadListener.INSTANCE);
	}
}
