/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.client;

import moriyashiine.enchancement.client.event.config.CoyoteBiteEvent;
import moriyashiine.enchancement.client.event.config.EnchantmentDescriptionsEvent;
import moriyashiine.enchancement.client.event.config.RebalanceEquipmentClientEvent;
import moriyashiine.enchancement.client.event.config.ToggleablePassivesEvent;
import moriyashiine.enchancement.client.event.enchantmenteffectcomponenttype.*;
import moriyashiine.enchancement.client.event.enchantmenteffecttype.AutomaticallyFeedsTooltipClientEvent;
import moriyashiine.enchancement.client.event.internal.SyncBookshelvesEvent;
import moriyashiine.enchancement.client.event.internal.SyncDeltaMovementsEvent;
import moriyashiine.enchancement.client.gui.hud.*;
import moriyashiine.enchancement.client.gui.screens.inventory.ModEnchantmentScreen;
import moriyashiine.enchancement.client.particle.*;
import moriyashiine.enchancement.client.particle.group.SparkParticleGroup;
import moriyashiine.enchancement.client.payload.*;
import moriyashiine.enchancement.client.reloadlistener.FrozenReloadListener;
import moriyashiine.enchancement.client.renderer.entity.AmethystShardRenderer;
import moriyashiine.enchancement.client.renderer.entity.BrimstoneRenderer;
import moriyashiine.enchancement.client.renderer.entity.IceShardRenderer;
import moriyashiine.enchancement.client.renderer.entity.TorchRenderer;
import moriyashiine.enchancement.client.renderer.entity.model.FrozenPlayerModel;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.init.ModEntityTypes;
import moriyashiine.enchancement.common.init.ModMenuTypes;
import moriyashiine.enchancement.common.init.ModParticleTypes;
import moriyashiine.strawberrylib.api.event.client.AddNightVisionScaleEvent;
import moriyashiine.strawberrylib.api.event.client.DisableContextualInfoEvent;
import moriyashiine.strawberrylib.api.event.client.OutlineEntityEvent;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleGroupRegistry;
import net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ClientTooltipComponentCallback;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.fabricmc.fabric.api.event.lifecycle.v1.CommonLifecycleEvents;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.fabric.api.resource.v1.pack.PackActivationType;
import net.fabricmc.fabric.api.resource.v1.reloader.ResourceReloaderKeys;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.particle.BubbleParticle;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.server.packs.PackType;
import org.lwjgl.glfw.GLFW;

public class EnchancementClient implements ClientModInitializer {
	private static final KeyMapping.Category KEYMAPPING_CATEGORY = KeyMapping.Category.register(Enchancement.id(Enchancement.MOD_ID));
	public static final KeyMapping CHARGE_JUMP_KEYMAPPING = KeyMappingHelper.registerKeyMapping(new KeyMapping("key." + Enchancement.MOD_ID + ".chargeJump", GLFW.GLFW_KEY_LEFT_SHIFT, KEYMAPPING_CATEGORY));
	public static final KeyMapping DIRECTION_BURST_KEYMAPPING = KeyMappingHelper.registerKeyMapping(new KeyMapping("key." + Enchancement.MOD_ID + ".directionBurst", GLFW.GLFW_KEY_LEFT_SHIFT, KEYMAPPING_CATEGORY));
	public static final KeyMapping ROTATION_BURST_KEYMAPPING = KeyMappingHelper.registerKeyMapping(new KeyMapping("key." + Enchancement.MOD_ID + ".rotationBurst", GLFW.GLFW_KEY_LEFT_CONTROL, KEYMAPPING_CATEGORY));
	public static final KeyMapping SLAM_KEYMAPPING = KeyMappingHelper.registerKeyMapping(new KeyMapping("key." + Enchancement.MOD_ID + ".slam", GLFW.GLFW_KEY_LEFT_CONTROL, KEYMAPPING_CATEGORY));
	public static final KeyMapping SLIDE_KEYMAPPING = KeyMappingHelper.registerKeyMapping(new KeyMapping("key." + Enchancement.MOD_ID + ".slide", GLFW.GLFW_KEY_LEFT_CONTROL, KEYMAPPING_CATEGORY));

	public static boolean betterCombatLoaded = false;

	@Override
	public void onInitializeClient() {
		initEntities();
		initParticles();
		initPayloads();
		initEvents();
		MenuScreens.register(ModMenuTypes.ENCHANTING_TABLE, ModEnchantmentScreen::new);
		ResourceLoader.get(PackType.CLIENT_RESOURCES).registerReloadListener(FrozenReloadListener.ID, FrozenReloadListener.INSTANCE);
		ResourceLoader.get(PackType.CLIENT_RESOURCES).addListenerOrdering(ResourceReloaderKeys.Client.TEXTURES, FrozenReloadListener.ID);
		FabricLoader.getInstance().getModContainer(Enchancement.MOD_ID).ifPresent(modContainer -> ResourceLoader.registerBuiltinPack(Enchancement.id("alternate_air_jump"), modContainer, PackActivationType.NORMAL));
		FabricLoader.getInstance().getModContainer(Enchancement.MOD_ID).ifPresent(modContainer -> ResourceLoader.registerBuiltinPack(Enchancement.id("alternate_burst"), modContainer, PackActivationType.NORMAL));
		betterCombatLoaded = FabricLoader.getInstance().isModLoaded("bettercombat");
	}

	private void initEntities() {
		ModelLayerRegistry.registerModelLayer(FrozenPlayerModel.LAYER, () -> FrozenPlayerModel.createBodyLayer(false));
		ModelLayerRegistry.registerModelLayer(FrozenPlayerModel.LAYER_SLIM, () -> FrozenPlayerModel.createBodyLayer(true));
		EntityRenderers.register(ModEntityTypes.AMETHYST_SHARD, AmethystShardRenderer::new);
		EntityRenderers.register(ModEntityTypes.BRIMSTONE, BrimstoneRenderer::new);
		EntityRenderers.register(ModEntityTypes.ICE_SHARD, IceShardRenderer::new);
		EntityRenderers.register(ModEntityTypes.TORCH, TorchRenderer::new);
	}

	private void initParticles() {
		ParticleProviderRegistry.getInstance().register(ModParticleTypes.BRIMSTONE_BUBBLE, BubbleParticle.Provider::new);
		ParticleProviderRegistry.getInstance().register(ModParticleTypes.CHISELED_ENCHANT, PurpleFlyTowardsPositionParticle.Provider::new);
		ParticleProviderRegistry.getInstance().register(ModParticleTypes.CRITICAL_TIPPER, TintlessDamageParticle.Provider::new);
		ParticleProviderRegistry.getInstance().register(ModParticleTypes.HONEY_BUBBLE, HoneyBubbleParticle.Provider::new);
		ParticleProviderRegistry.getInstance().register(ModParticleTypes.SHORT_SMALL_FLAME, ShortFlameParticle.SmallProvider::new);
		ParticleGroupRegistry.register(SparkParticleGroup.SHEET, SparkParticleGroup::new);
		ParticleProviderRegistry.getInstance().register(ModParticleTypes.SPARK, _ -> new SparkParticle.Provider());
		ParticleProviderRegistry.getInstance().register(ModParticleTypes.VELOCITY_LINE, VelocityLineParticle.Provider::new);
	}

	private void initPayloads() {
		// internal
		ClientPlayNetworking.registerGlobalReceiver(EnforceConfigMatchPayload.TYPE, new EnforceConfigMatchPayload.Receiver());
		ClientPlayNetworking.registerGlobalReceiver(SyncBookshelvesPayload.TYPE, new SyncBookshelvesPayload.Receiver());
		ClientPlayNetworking.registerGlobalReceiver(SyncEnchantingMaterialMapPayload.TYPE, new SyncEnchantingMaterialMapPayload.Receiver());
		ClientPlayNetworking.registerGlobalReceiver(SyncEnchantingTableCostPayload.TYPE, new SyncEnchantingTableCostPayload.Receiver());
		ClientPlayNetworking.registerGlobalReceiver(SyncHookedMovementDeltaPayload.TYPE, new SyncHookedMovementDeltaPayload.Receiver());
		ClientPlayNetworking.registerGlobalReceiver(SyncOriginalMaxLevelsPayload.TYPE, new SyncOriginalMaxLevelsPayload.Receiver());
		// enchantment
		ClientPlayNetworking.registerGlobalReceiver(AddLightningDashParticlesPayload.TYPE, new AddLightningDashParticlesPayload.Receiver());
		ClientPlayNetworking.registerGlobalReceiver(AddMoltenParticlesPayload.TYPE, new AddMoltenParticlesPayload.Receiver());
		ClientPlayNetworking.registerGlobalReceiver(BoostInFluidS2CPayload.TYPE, new BoostInFluidS2CPayload.Receiver());
		ClientPlayNetworking.registerGlobalReceiver(GlideS2CPayload.TYPE, new GlideS2CPayload.Receiver());
		ClientPlayNetworking.registerGlobalReceiver(PlayBrimstoneFireSoundPayload.TYPE, new PlayBrimstoneFireSoundPayload.Receiver());
		ClientPlayNetworking.registerGlobalReceiver(PlayBrimstoneTravelSoundPayload.TYPE, new PlayBrimstoneTravelSoundPayload.Receiver());
		ClientPlayNetworking.registerGlobalReceiver(StartSlammingS2CPayload.TYPE, new StartSlammingS2CPayload.Receiver());
		ClientPlayNetworking.registerGlobalReceiver(StartSlidingS2CPayload.TYPE, new StartSlidingS2CPayload.Receiver());
		ClientPlayNetworking.registerGlobalReceiver(StopSlammingS2CPayload.TYPE, new StopSlammingS2CPayload.Receiver());
		ClientPlayNetworking.registerGlobalReceiver(StopSlidingS2CPayload.TYPE, new StopSlidingS2CPayload.Receiver());
		ClientPlayNetworking.registerGlobalReceiver(SyncFrozenPlayerSlimStatusS2CPayload.TYPE, new SyncFrozenPlayerSlimStatusS2CPayload.Receiver());
		ClientPlayNetworking.registerGlobalReceiver(UseEruptionPayload.TYPE, new UseEruptionPayload.Receiver());
		ClientPlayNetworking.registerGlobalReceiver(UseLightningDashPayload.TYPE, new UseLightningDashPayload.Receiver());
	}

	private void initEvents() {
		// internal
		ClientTickEvents.END_LEVEL_TICK.register(new SyncBookshelvesEvent());
		ClientTickEvents.START_LEVEL_TICK.register(new SyncDeltaMovementsEvent());
		// config
		ClientTickEvents.END_LEVEL_TICK.register(new CoyoteBiteEvent());
		ItemTooltipCallback.EVENT.register(new EnchantmentDescriptionsEvent.DescriptionText());
		ClientTooltipComponentCallback.EVENT.register(new EnchantmentDescriptionsEvent.Icons());
		CommonLifecycleEvents.TAGS_LOADED.register(new EnchantmentDescriptionsEvent.ClearIconCache());
		ClientTickEvents.END_LEVEL_TICK.register(new RebalanceEquipmentClientEvent());
		ItemTooltipCallback.EVENT.register(new ToggleablePassivesEvent());
		// enchantment effect type
		ItemTooltipCallback.EVENT.register(new AutomaticallyFeedsTooltipClientEvent());
		// enchantment effect component type
		ClientTickEvents.END_LEVEL_TICK.register(new BounceClientEvent());
		DisableContextualInfoEvent.EVENT.register(new ChargeJumpClientEvent());
		ClientTickEvents.END_LEVEL_TICK.register(new EntityXrayClientEvent.Tick());
		OutlineEntityEvent.EVENT.register(new EntityXrayClientEvent.Outline());
		AddNightVisionScaleEvent.EVENT.register(new NightVisionClientEvent());
		ItemTooltipCallback.EVENT.register(new RageClientEvent());
		// hud elements
		HudElementRegistry.attachElementBefore(VanillaHudElements.HELD_ITEM_TOOLTIP, Enchancement.id("chiseled_bookshelf_peeking"), new ChiseledBookshelfPeekingHudElement());
		HudElementRegistry.attachElementAfter(VanillaHudElements.CROSSHAIR, Enchancement.id("air_jump"), new AirJumpHudElement());
		HudElementRegistry.attachElementAfter(VanillaHudElements.CROSSHAIR, Enchancement.id("brimstone"), new BrimstoneHudElement());
		HudElementRegistry.attachElementAfter(VanillaHudElements.CROSSHAIR, Enchancement.id("charge_jump"), new ChargeJumpHudElement());
		HudElementRegistry.attachElementAfter(VanillaHudElements.CROSSHAIR, Enchancement.id("direction_burst"), new DirectionBurstHudElement());
		HudElementRegistry.attachElementAfter(VanillaHudElements.CROSSHAIR, Enchancement.id("rotation_burst"), new RotationBurstHudElement());
	}
}
