/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.client;

import moriyashiine.enchancement.client.event.config.CoyoteBiteClientEvent;
import moriyashiine.enchancement.client.event.config.EnchantmentDescriptionsClientEvent;
import moriyashiine.enchancement.client.event.config.ToggleablePassivesClientEvent;
import moriyashiine.enchancement.client.event.enchantmenteffectcomponenttype.*;
import moriyashiine.enchancement.client.event.enchantmenteffecttype.AutomaticallyFeedsTooltipClientEvent;
import moriyashiine.enchancement.client.event.internal.SyncBookshelvesClientEvent;
import moriyashiine.enchancement.client.event.internal.SyncDeltaMovementsClientEvent;
import moriyashiine.enchancement.client.gui.hud.*;
import moriyashiine.enchancement.client.gui.screens.inventory.OverhauledEnchantmentScreen;
import moriyashiine.enchancement.client.particle.*;
import moriyashiine.enchancement.client.particle.group.SparkParticleGroup;
import moriyashiine.enchancement.client.payload.*;
import moriyashiine.enchancement.client.reloadlistener.FrozenReloadListener;
import moriyashiine.enchancement.client.renderer.entity.AmethystShardRenderer;
import moriyashiine.enchancement.client.renderer.entity.BrimstoneRenderer;
import moriyashiine.enchancement.client.renderer.entity.IceShardRenderer;
import moriyashiine.enchancement.client.renderer.entity.TorchRenderer;
import moriyashiine.enchancement.client.renderer.entity.model.FrozenPlayerModel;
import moriyashiine.enchancement.client.renderer.entity.model.MobSpinAttackEffectModel;
import moriyashiine.enchancement.client.resources.sound.EMeterSoundInstance;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.EnchancementService;
import moriyashiine.enchancement.common.init.EnchancementEntityTypes;
import moriyashiine.enchancement.common.init.EnchancementMenuTypes;
import moriyashiine.enchancement.common.init.EnchancementParticleTypes;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleGroupRegistry;
import net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
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
	public static final KeyMapping BOOST_IN_FLUID_HOVER_KEYMAPPING = KeyMappingHelper.registerKeyMapping(new KeyMapping("key." + Enchancement.MOD_ID + ".boostInFluidHover", GLFW.GLFW_KEY_SPACE, KEYMAPPING_CATEGORY));
	public static final KeyMapping CHARGE_JUMP_KEYMAPPING = KeyMappingHelper.registerKeyMapping(new KeyMapping("key." + Enchancement.MOD_ID + ".chargeJump", GLFW.GLFW_KEY_LEFT_SHIFT, KEYMAPPING_CATEGORY));
	public static final KeyMapping DIRECTION_BURST_KEYMAPPING = KeyMappingHelper.registerKeyMapping(new KeyMapping("key." + Enchancement.MOD_ID + ".directionBurst", GLFW.GLFW_KEY_LEFT_SHIFT, KEYMAPPING_CATEGORY));
	public static final KeyMapping E_METER_HOVER_KEYMAPPING = KeyMappingHelper.registerKeyMapping(new KeyMapping("key." + Enchancement.MOD_ID + ".eMeterHover", GLFW.GLFW_KEY_SPACE, KEYMAPPING_CATEGORY));
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
		MenuScreens.register(EnchancementMenuTypes.OVERHAULED_ENCHANTING_TABLE, OverhauledEnchantmentScreen::new);
		ResourceLoader.get(PackType.CLIENT_RESOURCES).registerReloadListener(FrozenReloadListener.ID, FrozenReloadListener.INSTANCE);
		ResourceLoader.get(PackType.CLIENT_RESOURCES).addListenerOrdering(ResourceReloaderKeys.Client.TEXTURES, FrozenReloadListener.ID);
		FabricLoader.getInstance().getModContainer(Enchancement.MOD_ID).ifPresent(modContainer -> ResourceLoader.registerBuiltinPack(Enchancement.id("alternate_air_jump"), modContainer, PackActivationType.NORMAL));
		FabricLoader.getInstance().getModContainer(Enchancement.MOD_ID).ifPresent(modContainer -> ResourceLoader.registerBuiltinPack(Enchancement.id("alternate_burst"), modContainer, PackActivationType.NORMAL));
		FabricLoader.getInstance().getModContainer(Enchancement.MOD_ID).ifPresent(modContainer -> ResourceLoader.registerBuiltinPack(EMeterSoundInstance.ALTERNATE_ID, modContainer, PackActivationType.NORMAL));
		betterCombatLoaded = FabricLoader.getInstance().isModLoaded("bettercombat");
		if (FabricLoader.getInstance().isModLoaded("appleskin")) {
			EnchancementService.INSTANCE.initAppleSkinIntegration();
		}
	}

	private void initEntities() {
		ModelLayerRegistry.registerModelLayer(FrozenPlayerModel.LAYER, () -> FrozenPlayerModel.createBodyLayer(false));
		ModelLayerRegistry.registerModelLayer(FrozenPlayerModel.LAYER_SLIM, () -> FrozenPlayerModel.createBodyLayer(true));
		EntityRenderers.register(EnchancementEntityTypes.AMETHYST_SHARD, AmethystShardRenderer::new);
		EntityRenderers.register(EnchancementEntityTypes.BRIMSTONE, BrimstoneRenderer::new);
		EntityRenderers.register(EnchancementEntityTypes.ICE_SHARD, IceShardRenderer::new);
		EntityRenderers.register(EnchancementEntityTypes.TORCH, TorchRenderer::new);

		ModelLayerRegistry.registerModelLayer(MobSpinAttackEffectModel.LAYER, MobSpinAttackEffectModel::createLayer);
	}

	private void initParticles() {
		ParticleProviderRegistry.getInstance().register(EnchancementParticleTypes.BRIMSTONE_BUBBLE, BubbleParticle.Provider::new);
		ParticleProviderRegistry.getInstance().register(EnchancementParticleTypes.CHISELED_ENCHANT, PurpleFlyTowardsPositionParticle.Provider::new);
		ParticleProviderRegistry.getInstance().register(EnchancementParticleTypes.CRITICAL_TIPPER, TintlessDamageParticle.Provider::new);
		ParticleProviderRegistry.getInstance().register(EnchancementParticleTypes.HONEY_BUBBLE, HoneyBubbleParticle.Provider::new);
		ParticleProviderRegistry.getInstance().register(EnchancementParticleTypes.SHORT_SMALL_FLAME, ShortFlameParticle.SmallProvider::new);
		ParticleGroupRegistry.register(SparkParticleGroup.SHEET, SparkParticleGroup::new);
		ParticleProviderRegistry.getInstance().register(EnchancementParticleTypes.SPARK, _ -> new SparkParticle.Provider());
		ParticleProviderRegistry.getInstance().register(EnchancementParticleTypes.VELOCITY_LINE, VelocityLineParticle.Provider::new);
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
		ClientPlayNetworking.registerGlobalReceiver(EMeterS2CPayload.TYPE, new EMeterS2CPayload.Receiver());
		ClientPlayNetworking.registerGlobalReceiver(GlideS2CPayload.TYPE, new GlideS2CPayload.Receiver());
		ClientPlayNetworking.registerGlobalReceiver(PlayBrimstoneFireSoundPayload.TYPE, new PlayBrimstoneFireSoundPayload.Receiver());
		ClientPlayNetworking.registerGlobalReceiver(PlayBrimstoneTravelSoundPayload.TYPE, new PlayBrimstoneTravelSoundPayload.Receiver());
		ClientPlayNetworking.registerGlobalReceiver(PlayEMeterFloatSoundPayload.TYPE, new PlayEMeterFloatSoundPayload.Receiver());
		ClientPlayNetworking.registerGlobalReceiver(StartSlammingS2CPayload.TYPE, new StartSlammingS2CPayload.Receiver());
		ClientPlayNetworking.registerGlobalReceiver(SlideS2CPayload.TYPE, new SlideS2CPayload.Receiver());
		ClientPlayNetworking.registerGlobalReceiver(StopSlammingS2CPayload.TYPE, new StopSlammingS2CPayload.Receiver());
		ClientPlayNetworking.registerGlobalReceiver(SyncFrozenPlayerSlimStatusS2CPayload.TYPE, new SyncFrozenPlayerSlimStatusS2CPayload.Receiver());
		ClientPlayNetworking.registerGlobalReceiver(UseEruptionPayload.TYPE, new UseEruptionPayload.Receiver());
		ClientPlayNetworking.registerGlobalReceiver(UseLightningDashPayload.TYPE, new UseLightningDashPayload.Receiver());
	}

	private void initEvents() {
		// internal
		SyncBookshelvesClientEvent.init();
		SyncDeltaMovementsClientEvent.init();
		// config
		CoyoteBiteClientEvent.init();
		EnchantmentDescriptionsClientEvent.init();
		ToggleablePassivesClientEvent.init();
		// enchantment effect type
		AutomaticallyFeedsTooltipClientEvent.init();
		// enchantment effect component type
		BounceClientEvent.init();
		ChargeJumpClientEvent.init();
		EntityXrayClientEvent.init();
		NightVisionClientEvent.init();
		RageClientEvent.init();
		WideMiningClientEvent.init();
		// hud elements
		HudElementRegistry.attachElementBefore(VanillaHudElements.HELD_ITEM_TOOLTIP, Enchancement.id("chiseled_bookshelf_peeking"), new ChiseledBookshelfPeekingHudElement());
		HudElementRegistry.attachElementAfter(VanillaHudElements.CROSSHAIR, Enchancement.id("air_jump"), new AirJumpHudElement());
		HudElementRegistry.attachElementAfter(VanillaHudElements.CROSSHAIR, Enchancement.id("brimstone"), new BrimstoneHudElement());
		HudElementRegistry.attachElementAfter(VanillaHudElements.CROSSHAIR, Enchancement.id("direction_burst"), new DirectionBurstHudElement());
		HudElementRegistry.attachElementAfter(VanillaHudElements.CROSSHAIR, Enchancement.id("e_meter"), new EMeterHudElement());
		HudElementRegistry.attachElementAfter(VanillaHudElements.CROSSHAIR, Enchancement.id("extended_water_time"), new ExtendedWaterTimeHudElement());
		HudElementRegistry.attachElementAfter(VanillaHudElements.CROSSHAIR, Enchancement.id("rotation_burst"), new RotationBurstHudElement());
	}
}
