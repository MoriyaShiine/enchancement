/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.client;

import moriyashiine.enchancement.client.event.*;
import moriyashiine.enchancement.client.packet.*;
import moriyashiine.enchancement.client.reloadlisteners.FrozenReloadListener;
import moriyashiine.enchancement.client.render.entity.AmethystShardEntityRenderer;
import moriyashiine.enchancement.client.render.entity.BrimstoneEntityRenderer;
import moriyashiine.enchancement.client.render.entity.IceShardEntityRenderer;
import moriyashiine.enchancement.client.render.entity.TorchEntityRenderer;
import moriyashiine.enchancement.client.render.entity.mob.FrozenPlayerEntityRenderer;
import moriyashiine.enchancement.client.screen.EnchantingTableScreen;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.init.ModEnchantments;
import moriyashiine.enchancement.common.init.ModEntityTypes;
import moriyashiine.enchancement.common.init.ModScreenHandlerTypes;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.enchancement.mixin.brimstone.CrossbowItemAccessor;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.entity.FishingBobberEntityRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.resource.ResourceType;
import org.lwjgl.glfw.GLFW;

public class EnchancementClient implements ClientModInitializer {
	public static final KeyBinding STRAFE_KEYBINDING = KeyBindingHelper.registerKeyBinding(new KeyBinding("key." + Enchancement.MOD_ID + ".strafe", GLFW.GLFW_KEY_UNKNOWN, "key.categories." + Enchancement.MOD_ID));
	public static final KeyBinding SLIDE_KEYBINDING = KeyBindingHelper.registerKeyBinding(new KeyBinding("key." + Enchancement.MOD_ID + ".slide", GLFW.GLFW_KEY_UNKNOWN, "key.categories." + Enchancement.MOD_ID));
	public static final KeyBinding SLAM_KEYBINDING = KeyBindingHelper.registerKeyBinding(new KeyBinding("key." + Enchancement.MOD_ID + ".slam", GLFW.GLFW_KEY_UNKNOWN, "key.categories." + Enchancement.MOD_ID));

	@Override
	public void onInitializeClient() {
		ClientPlayNetworking.registerGlobalReceiver(EnforceConfigMatchPacket.ID, new EnforceConfigMatchPacket.Receiver());
		ClientPlayNetworking.registerGlobalReceiver(SyncEnchantingTableCostPacket.ID, new SyncEnchantingTableCostPacket.Receiver());
		ClientPlayNetworking.registerGlobalReceiver(AddStrafeParticlesPacket.ID, new AddStrafeParticlesPacket.Receiver());
		ClientPlayNetworking.registerGlobalReceiver(AddGaleParticlesPacket.ID, new AddGaleParticlesPacket.Receiver());
		ClientPlayNetworking.registerGlobalReceiver(PlayBrimstoneSoundPacket.ID, new PlayBrimstoneSoundPacket.Receiver());
		ClientPlayNetworking.registerGlobalReceiver(StopBrimstoneSoundsS2CPacket.ID, new StopBrimstoneSoundsS2CPacket.Receiver());
		ClientPlayNetworking.registerGlobalReceiver(ResetFrozenTicksPacket.ID, new ResetFrozenTicksPacket.Receiver());
		ClientPlayNetworking.registerGlobalReceiver(SyncFrozenPlayerSlimStatusS2C.ID, new SyncFrozenPlayerSlimStatusS2C.Receiver());
		ClientPlayNetworking.registerGlobalReceiver(AddMoltenParticlesPacket.ID, new AddMoltenParticlesPacket.Receiver());
		EntityRendererRegistry.register(ModEntityTypes.FROZEN_PLAYER, FrozenPlayerEntityRenderer::new);
		EntityRendererRegistry.register(ModEntityTypes.ICE_SHARD, IceShardEntityRenderer::new);
		EntityRendererRegistry.register(ModEntityTypes.BRIMSTONE, BrimstoneEntityRenderer::new);
		EntityRendererRegistry.register(ModEntityTypes.AMETHYST_SHARD, AmethystShardEntityRenderer::new);
		EntityRendererRegistry.register(ModEntityTypes.TORCH, TorchEntityRenderer::new);
		EntityRendererRegistry.register(ModEntityTypes.GRAPPLE_FISHING_BOBBER, FishingBobberEntityRenderer::new);
		ModelPredicateProviderRegistry.register(Items.CROSSBOW, Enchancement.id("brimstone"), (stack, world, entity, seed) -> {
			if (CrossbowItemAccessor.enchancement$getProjectiles(stack).stream().anyMatch(foundStack -> ItemStack.areEqual(foundStack, EnchancementUtil.BRIMSTONE_STACK))) {
				return stack.getSubNbt(Enchancement.MOD_ID).getInt("BrimstoneDamage") / 12F;
			}
			return 0;
		});
		ModelPredicateProviderRegistry.register(Items.CROSSBOW, Enchancement.id("amethyst_shard"), (stack, world, entity, seed) -> CrossbowItem.hasProjectile(stack, Items.AMETHYST_SHARD) || (CrossbowItem.isCharged(stack) && EnchancementUtil.hasEnchantment(ModEnchantments.SCATTER, stack) && !(entity instanceof PlayerEntity)) ? 1 : 0);
		ModelPredicateProviderRegistry.register(Items.CROSSBOW, Enchancement.id("torch"), (stack, world, entity, seed) -> CrossbowItem.hasProjectile(stack, Items.TORCH) || (CrossbowItem.isCharged(stack) && EnchancementUtil.hasEnchantment(ModEnchantments.TORCH, stack) && !(entity instanceof PlayerEntity)) ? 1 : 0);
		HandledScreens.register(ModScreenHandlerTypes.ENCHANTING_TABLE, EnchantingTableScreen::new);
		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(FrozenReloadListener.INSTANCE);
		FabricLoader.getInstance().getModContainer(Enchancement.MOD_ID).ifPresent(modContainer -> ResourceManagerHelper.registerBuiltinResourcePack(Enchancement.id("alternate_dash"), modContainer, ResourcePackActivationType.NORMAL));
		initEvents();
	}

	private void initEvents() {
		ItemTooltipCallback.EVENT.register(new EnchantmentDescriptionsEvent());
		ItemTooltipCallback.EVENT.register(new AssimilationTooltipEvent());
		HudRenderCallback.EVENT.register(new DashRenderEvent());
		HudRenderCallback.EVENT.register(new BouncyRenderEvent());
		HudRenderCallback.EVENT.register(new GaleRenderEvent());
		HudRenderCallback.EVENT.register(new BrimstoneRenderEvent());
	}
}
