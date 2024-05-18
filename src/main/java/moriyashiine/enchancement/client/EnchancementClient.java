/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.client;

import moriyashiine.enchancement.client.event.*;
import moriyashiine.enchancement.client.payload.*;
import moriyashiine.enchancement.client.reloadlisteners.FrozenReloadListener;
import moriyashiine.enchancement.client.render.entity.AmethystShardEntityRenderer;
import moriyashiine.enchancement.client.render.entity.BrimstoneEntityRenderer;
import moriyashiine.enchancement.client.render.entity.IceShardEntityRenderer;
import moriyashiine.enchancement.client.render.entity.TorchEntityRenderer;
import moriyashiine.enchancement.client.render.entity.mob.FrozenPlayerEntityRenderer;
import moriyashiine.enchancement.client.screen.EnchantingTableScreen;
import moriyashiine.enchancement.client.util.EnchancementClientUtil;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.init.ModDataComponentTypes;
import moriyashiine.enchancement.common.init.ModEnchantments;
import moriyashiine.enchancement.common.init.ModEntityTypes;
import moriyashiine.enchancement.common.init.ModScreenHandlerTypes;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
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
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Items;
import net.minecraft.resource.ResourceType;
import org.lwjgl.glfw.GLFW;

import java.util.function.Supplier;

public class EnchancementClient implements ClientModInitializer {
	public static final KeyBinding DASH_KEYBINDING = registerKeyBinding(() -> KeyBindingHelper.registerKeyBinding(new KeyBinding("key." + Enchancement.MOD_ID + ".dash", GLFW.GLFW_KEY_LEFT_SHIFT, "key.categories." + Enchancement.MOD_ID)));
	public static final KeyBinding SLAM_KEYBINDING = registerKeyBinding(() -> KeyBindingHelper.registerKeyBinding(new KeyBinding("key." + Enchancement.MOD_ID + ".slam", GLFW.GLFW_KEY_LEFT_CONTROL, "key.categories." + Enchancement.MOD_ID)));
	public static final KeyBinding SLIDE_KEYBINDING = registerKeyBinding(() -> KeyBindingHelper.registerKeyBinding(new KeyBinding("key." + Enchancement.MOD_ID + ".slide", GLFW.GLFW_KEY_LEFT_CONTROL, "key.categories." + Enchancement.MOD_ID)));
	public static final KeyBinding STRAFE_KEYBINDING = registerKeyBinding(() -> KeyBindingHelper.registerKeyBinding(new KeyBinding("key." + Enchancement.MOD_ID + ".strafe", GLFW.GLFW_KEY_LEFT_CONTROL, "key.categories." + Enchancement.MOD_ID)));

	public static boolean betterCombatLoaded = false;

	@Override
	public void onInitializeClient() {
		EntityRendererRegistry.register(ModEntityTypes.FROZEN_PLAYER, FrozenPlayerEntityRenderer::new);
		EntityRendererRegistry.register(ModEntityTypes.ICE_SHARD, IceShardEntityRenderer::new);
		EntityRendererRegistry.register(ModEntityTypes.BRIMSTONE, BrimstoneEntityRenderer::new);
		EntityRendererRegistry.register(ModEntityTypes.AMETHYST_SHARD, AmethystShardEntityRenderer::new);
		EntityRendererRegistry.register(ModEntityTypes.TORCH, TorchEntityRenderer::new);
		EntityRendererRegistry.register(ModEntityTypes.GRAPPLE_FISHING_BOBBER, FishingBobberEntityRenderer::new);
		ModelPredicateProviderRegistry.register(Items.CROSSBOW, Enchancement.id("brimstone"), (stack, world, entity, seed) -> CrossbowItem.isCharged(stack) && stack.contains(ModDataComponentTypes.BRIMSTONE_DAMAGE) ? stack.get(ModDataComponentTypes.BRIMSTONE_DAMAGE) / 12F : 0);
		ModelPredicateProviderRegistry.register(Items.CROSSBOW, Enchancement.id("amethyst_shard"), (stack, world, entity, seed) -> stack.contains(DataComponentTypes.CHARGED_PROJECTILES) && stack.get(DataComponentTypes.CHARGED_PROJECTILES).contains(Items.AMETHYST_SHARD) || (CrossbowItem.isCharged(stack) && EnchancementUtil.hasEnchantment(ModEnchantments.SCATTER, stack) && !(entity instanceof PlayerEntity)) ? 1 : 0);
		ModelPredicateProviderRegistry.register(Items.CROSSBOW, Enchancement.id("torch"), (stack, world, entity, seed) -> stack.contains(DataComponentTypes.CHARGED_PROJECTILES) && stack.get(DataComponentTypes.CHARGED_PROJECTILES).contains(Items.TORCH) || (CrossbowItem.isCharged(stack) && EnchancementUtil.hasEnchantment(ModEnchantments.TORCH, stack) && !(entity instanceof PlayerEntity)) ? 1 : 0);
		HandledScreens.register(ModScreenHandlerTypes.ENCHANTING_TABLE, EnchantingTableScreen::new);
		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(FrozenReloadListener.INSTANCE);
		FabricLoader.getInstance().getModContainer(Enchancement.MOD_ID).ifPresent(modContainer -> ResourceManagerHelper.registerBuiltinResourcePack(Enchancement.id("alternate_dash"), modContainer, ResourcePackActivationType.NORMAL));
		FabricLoader.getInstance().getModContainer(Enchancement.MOD_ID).ifPresent(modContainer -> ResourceManagerHelper.registerBuiltinResourcePack(Enchancement.id("alternate_gale"), modContainer, ResourcePackActivationType.NORMAL));
		initEvents();
		initPayloads();
		betterCombatLoaded = FabricLoader.getInstance().isModLoaded("bettercombat");
	}

	private void initEvents() {
		ItemTooltipCallback.EVENT.register(new EnchantmentDescriptionsEvent());
		ClientTickEvents.END_WORLD_TICK.register(new CoyoteBiteEvent());
		ItemTooltipCallback.EVENT.register(new AssimilationTooltipEvent());
//		HudRenderCallback.EVENT.register(new StrafeRenderEvent());
		ItemTooltipCallback.EVENT.register(new AdrenalineRenderEvent());
		HudRenderCallback.EVENT.register(new DashRenderEvent());
		HudRenderCallback.EVENT.register(new BouncyRenderEvent());
		HudRenderCallback.EVENT.register(new GaleRenderEvent());
		HudRenderCallback.EVENT.register(new BrimstoneRenderEvent());
	}

	private void initPayloads() {
		ClientPlayNetworking.registerGlobalReceiver(EnforceConfigMatchPayload.ID, new EnforceConfigMatchPayload.Receiver());
		ClientPlayNetworking.registerGlobalReceiver(SyncEnchantingMaterialMapPayload.ID, new SyncEnchantingMaterialMapPayload.Receiver());
		ClientPlayNetworking.registerGlobalReceiver(SyncEnchantingTableBookshelfCountPayload.ID, new SyncEnchantingTableBookshelfCountPayload.Receiver());
		ClientPlayNetworking.registerGlobalReceiver(SyncEnchantingTableCostPayload.ID, new SyncEnchantingTableCostPayload.Receiver());
		ClientPlayNetworking.registerGlobalReceiver(AddStrafeParticlesPayload.ID, new AddStrafeParticlesPayload.Receiver());
		ClientPlayNetworking.registerGlobalReceiver(AddGaleParticlesPayload.ID, new AddGaleParticlesPayload.Receiver());
		ClientPlayNetworking.registerGlobalReceiver(PlayBrimstoneSoundPayload.ID, new PlayBrimstoneSoundPayload.Receiver());
		ClientPlayNetworking.registerGlobalReceiver(ResetFrozenTicksPayload.ID, new ResetFrozenTicksPayload.Receiver());
		ClientPlayNetworking.registerGlobalReceiver(SyncFrozenPlayerSlimStatusS2CPayload.ID, new SyncFrozenPlayerSlimStatusS2CPayload.Receiver());
		ClientPlayNetworking.registerGlobalReceiver(AddMoltenParticlesPayload.ID, new AddMoltenParticlesPayload.Receiver());
	}

	private static KeyBinding registerKeyBinding(Supplier<KeyBinding> supplier) {
		KeyBinding keyBinding = supplier.get();
		EnchancementClientUtil.VANILLA_AND_ENCHANCEMENT_KEYBINDINGS.add(keyBinding);
		return keyBinding;
	}
}
