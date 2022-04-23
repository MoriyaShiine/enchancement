package moriyashiine.enchancement.client;

import moriyashiine.enchancement.client.packet.AddMoltenParticlesPacket;
import moriyashiine.enchancement.client.packet.AddStrafeParticlesPacket;
import moriyashiine.enchancement.client.packet.ResetFrozenTicksPacket;
import moriyashiine.enchancement.client.packet.SyncFrozenPlayerSlimStatusS2C;
import moriyashiine.enchancement.client.reloadlisteners.FrozenReloadListener;
import moriyashiine.enchancement.client.render.entity.IceShardEntityRenderer;
import moriyashiine.enchancement.client.render.entity.TorchEntityRenderer;
import moriyashiine.enchancement.client.render.entity.mob.FrozenPlayerEntityRenderer;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.registry.ModEntityTypes;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Items;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class EnchancementClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ClientPlayNetworking.registerGlobalReceiver(AddStrafeParticlesPacket.ID, AddStrafeParticlesPacket::receive);
		ClientPlayNetworking.registerGlobalReceiver(ResetFrozenTicksPacket.ID, ResetFrozenTicksPacket::receive);
		ClientPlayNetworking.registerGlobalReceiver(SyncFrozenPlayerSlimStatusS2C.ID, SyncFrozenPlayerSlimStatusS2C::receive);
		ClientPlayNetworking.registerGlobalReceiver(AddMoltenParticlesPacket.ID, AddMoltenParticlesPacket::receive);
		EntityRendererRegistry.register(ModEntityTypes.FROZEN_PLAYER, FrozenPlayerEntityRenderer::new);
		EntityRendererRegistry.register(ModEntityTypes.ICE_SHARD, IceShardEntityRenderer::new);
		EntityRendererRegistry.register(ModEntityTypes.TORCH, TorchEntityRenderer::new);
		ModelPredicateProviderRegistry.register(Items.CROSSBOW, new Identifier(Enchancement.MOD_ID, "crossbow_torch"), (stack, world, entity, seed) -> CrossbowItem.hasProjectile(stack, Items.TORCH) ? 1 : 0);
		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(FrozenReloadListener.INSTANCE);
	}
}
