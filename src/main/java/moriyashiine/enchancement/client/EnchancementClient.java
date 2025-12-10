/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.client;

import moriyashiine.enchancement.client.event.config.CoyoteBiteEvent;
import moriyashiine.enchancement.client.event.config.EnchantmentDescriptionsEvent;
import moriyashiine.enchancement.client.event.config.SyncVelocitiesEvent;
import moriyashiine.enchancement.client.event.config.ToggleablePassivesEvent;
import moriyashiine.enchancement.client.event.enchantmenteffectcomponenttype.*;
import moriyashiine.enchancement.client.event.internal.SyncBookshelvesEvent;
import moriyashiine.enchancement.client.gui.screen.ingame.EnchantingTableScreen;
import moriyashiine.enchancement.client.hud.*;
import moriyashiine.enchancement.client.particle.*;
import moriyashiine.enchancement.client.particle.render.SparkParticleRenderer;
import moriyashiine.enchancement.client.payload.*;
import moriyashiine.enchancement.client.reloadlisteners.FrozenReloadListener;
import moriyashiine.enchancement.client.render.entity.AmethystShardEntityRenderer;
import moriyashiine.enchancement.client.render.entity.BrimstoneEntityRenderer;
import moriyashiine.enchancement.client.render.entity.IceShardEntityRenderer;
import moriyashiine.enchancement.client.render.entity.TorchEntityRenderer;
import moriyashiine.enchancement.client.render.entity.model.FrozenPlayerEntityModel;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.init.ModEntityTypes;
import moriyashiine.enchancement.common.init.ModParticleTypes;
import moriyashiine.enchancement.common.init.ModScreenHandlerTypes;
import moriyashiine.strawberrylib.api.event.client.DisableHudBarEvent;
import moriyashiine.strawberrylib.api.event.client.ModifyNightVisionStrengthEvent;
import moriyashiine.strawberrylib.api.event.client.OutlineEntityEvent;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.particle.v1.ParticleRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.fabricmc.fabric.api.event.lifecycle.v1.CommonLifecycleEvents;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.fabric.api.resource.v1.pack.PackActivationType;
import net.fabricmc.fabric.api.resource.v1.reloader.ResourceReloaderKeys;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.particle.WaterBubbleParticle;
import net.minecraft.client.render.entity.EntityRendererFactories;
import net.minecraft.resource.ResourceType;
import org.lwjgl.glfw.GLFW;

public class EnchancementClient implements ClientModInitializer {
	private static final KeyBinding.Category KEY_CATEGORY = KeyBinding.Category.create(Enchancement.id(Enchancement.MOD_ID));
	public static final KeyBinding DIRECTION_BURST_KEYBINDING = KeyBindingHelper.registerKeyBinding(new KeyBinding("key." + Enchancement.MOD_ID + ".directionBurst", GLFW.GLFW_KEY_LEFT_SHIFT, KEY_CATEGORY));
	public static final KeyBinding ROTATION_BURST_KEYBINDING = KeyBindingHelper.registerKeyBinding(new KeyBinding("key." + Enchancement.MOD_ID + ".rotationBurst", GLFW.GLFW_KEY_LEFT_CONTROL, KEY_CATEGORY));
	public static final KeyBinding SLAM_KEYBINDING = KeyBindingHelper.registerKeyBinding(new KeyBinding("key." + Enchancement.MOD_ID + ".slam", GLFW.GLFW_KEY_LEFT_CONTROL, KEY_CATEGORY));
	public static final KeyBinding SLIDE_KEYBINDING = KeyBindingHelper.registerKeyBinding(new KeyBinding("key." + Enchancement.MOD_ID + ".slide", GLFW.GLFW_KEY_LEFT_CONTROL, KEY_CATEGORY));

	public static boolean betterCombatLoaded = false, irisLoaded = false;

	public static boolean drawTooltipsImmediately = false;

	@Override
	public void onInitializeClient() {
		initEntities();
		initParticles();
		initEvents();
		initPayloads();
		HandledScreens.register(ModScreenHandlerTypes.ENCHANTING_TABLE, EnchantingTableScreen::new);
		ResourceLoader.get(ResourceType.CLIENT_RESOURCES).registerReloader(FrozenReloadListener.ID, FrozenReloadListener.INSTANCE);
		ResourceLoader.get(ResourceType.CLIENT_RESOURCES).addReloaderOrdering(ResourceReloaderKeys.Client.TEXTURES, FrozenReloadListener.ID);
		FabricLoader.getInstance().getModContainer(Enchancement.MOD_ID).ifPresent(modContainer -> ResourceLoader.registerBuiltinPack(Enchancement.id("alternate_air_jump"), modContainer, PackActivationType.NORMAL));
		FabricLoader.getInstance().getModContainer(Enchancement.MOD_ID).ifPresent(modContainer -> ResourceLoader.registerBuiltinPack(Enchancement.id("alternate_burst"), modContainer, PackActivationType.NORMAL));
		betterCombatLoaded = FabricLoader.getInstance().isModLoaded("bettercombat");
		irisLoaded = FabricLoader.getInstance().isModLoaded("iris");
	}

	private void initEntities() {
		EntityModelLayerRegistry.registerModelLayer(FrozenPlayerEntityModel.LAYER, () -> FrozenPlayerEntityModel.getTexturedModelData(false));
		EntityModelLayerRegistry.registerModelLayer(FrozenPlayerEntityModel.LAYER_SLIM, () -> FrozenPlayerEntityModel.getTexturedModelData(true));
		EntityRendererFactories.register(ModEntityTypes.AMETHYST_SHARD, AmethystShardEntityRenderer::new);
		EntityRendererFactories.register(ModEntityTypes.BRIMSTONE, BrimstoneEntityRenderer::new);
		EntityRendererFactories.register(ModEntityTypes.ICE_SHARD, IceShardEntityRenderer::new);
		EntityRendererFactories.register(ModEntityTypes.TORCH, TorchEntityRenderer::new);
	}

	private void initParticles() {
		ParticleFactoryRegistry.getInstance().register(ModParticleTypes.BRIMSTONE_BUBBLE, WaterBubbleParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(ModParticleTypes.CHISELED_ENCHANTMENT, PurpleConnectionParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(ModParticleTypes.CRITICAL_TIPPER, TintlessDamageParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(ModParticleTypes.HONEY_BUBBLE, HoneyBubbleParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(ModParticleTypes.SHORT_SMALL_FLAME, ShortFlameParticle.SmallFactory::new);
		ParticleRendererRegistry.register(SparkParticleRenderer.SHEET, SparkParticleRenderer::new);
		ParticleFactoryRegistry.getInstance().register(ModParticleTypes.SPARK, provider -> new SparkParticle.Factory());
		ParticleFactoryRegistry.getInstance().register(ModParticleTypes.VELOCITY_LINE, VelocityLineParticle.Factory::new);
	}

	private void initEvents() {
		// internal
		ClientTickEvents.END_WORLD_TICK.register(new SyncBookshelvesEvent());
		// config
		ClientTickEvents.END_WORLD_TICK.register(new CoyoteBiteEvent());
		ItemTooltipCallback.EVENT.register(new EnchantmentDescriptionsEvent.DescriptionText());
		TooltipComponentCallback.EVENT.register(new EnchantmentDescriptionsEvent.Icons());
		CommonLifecycleEvents.TAGS_LOADED.register(new EnchantmentDescriptionsEvent.ClearIconCache());
		ClientTickEvents.START_WORLD_TICK.register(new SyncVelocitiesEvent());
		ItemTooltipCallback.EVENT.register(new ToggleablePassivesEvent());
		// enchantment
		ItemTooltipCallback.EVENT.register(new AutomaticallyFeedsTooltipClientEvent());
		ClientTickEvents.END_WORLD_TICK.register(new BounceClientEvent());
		DisableHudBarEvent.EVENT.register(new ChargeJumpClientEvent());
		ClientTickEvents.END_WORLD_TICK.register(new EntityXrayClientEvent.Tick());
		OutlineEntityEvent.EVENT.register(new EntityXrayClientEvent.Outline());
		ModifyNightVisionStrengthEvent.ADD.register(new NightVisionClientEvent());
		ItemTooltipCallback.EVENT.register(new RageClientEvent());
		// hud elements
		HudElementRegistry.attachElementBefore(VanillaHudElements.HELD_ITEM_TOOLTIP, Enchancement.id("chiseled_bookshelf_peeking"), new ChiseledBookshelfPeekingHudElement());
		HudElementRegistry.attachElementAfter(VanillaHudElements.CROSSHAIR, Enchancement.id("air_jump"), new AirJumpHudElement());
		HudElementRegistry.attachElementAfter(VanillaHudElements.CROSSHAIR, Enchancement.id("brimstone"), new BrimstoneHudElement());
		HudElementRegistry.attachElementAfter(VanillaHudElements.CROSSHAIR, Enchancement.id("charge_jump"), new ChargeJumpHudElement());
		HudElementRegistry.attachElementAfter(VanillaHudElements.CROSSHAIR, Enchancement.id("direction_burst"), new DirectionBurstHudElement());
		HudElementRegistry.attachElementAfter(VanillaHudElements.CROSSHAIR, Enchancement.id("rotation_burst"), new RotationBurstHudElement());
	}

	private void initPayloads() {
		// internal
		ClientPlayNetworking.registerGlobalReceiver(EnforceConfigMatchPayload.ID, new EnforceConfigMatchPayload.Receiver());
		ClientPlayNetworking.registerGlobalReceiver(SyncBookshelvesPayload.ID, new SyncBookshelvesPayload.Receiver());
		ClientPlayNetworking.registerGlobalReceiver(SyncEnchantingMaterialMapPayload.ID, new SyncEnchantingMaterialMapPayload.Receiver());
		ClientPlayNetworking.registerGlobalReceiver(SyncEnchantingTableCostPayload.ID, new SyncEnchantingTableCostPayload.Receiver());
		ClientPlayNetworking.registerGlobalReceiver(SyncHookedVelocityPayload.ID, new SyncHookedVelocityPayload.Receiver());
		ClientPlayNetworking.registerGlobalReceiver(SyncOriginalMaxLevelsPayload.ID, new SyncOriginalMaxLevelsPayload.Receiver());
		// enchantment
		ClientPlayNetworking.registerGlobalReceiver(AddLightningDashParticlesPayload.ID, new AddLightningDashParticlesPayload.Receiver());
		ClientPlayNetworking.registerGlobalReceiver(AddMoltenParticlesPayload.ID, new AddMoltenParticlesPayload.Receiver());
		ClientPlayNetworking.registerGlobalReceiver(BoostInFluidS2CPayload.ID, new BoostInFluidS2CPayload.Receiver());
		ClientPlayNetworking.registerGlobalReceiver(GlideS2CPayload.ID, new GlideS2CPayload.Receiver());
		ClientPlayNetworking.registerGlobalReceiver(PlayBrimstoneFireSoundPayload.ID, new PlayBrimstoneFireSoundPayload.Receiver());
		ClientPlayNetworking.registerGlobalReceiver(PlayBrimstoneTravelSoundPayload.ID, new PlayBrimstoneTravelSoundPayload.Receiver());
		ClientPlayNetworking.registerGlobalReceiver(StartSlammingS2CPayload.ID, new StartSlammingS2CPayload.Receiver());
		ClientPlayNetworking.registerGlobalReceiver(StartSlidingS2CPayload.ID, new StartSlidingS2CPayload.Receiver());
		ClientPlayNetworking.registerGlobalReceiver(StopSlammingS2CPayload.ID, new StopSlammingS2CPayload.Receiver());
		ClientPlayNetworking.registerGlobalReceiver(StopSlidingS2CPayload.ID, new StopSlidingS2CPayload.Receiver());
		ClientPlayNetworking.registerGlobalReceiver(SyncFrozenPlayerSlimStatusS2CPayload.ID, new SyncFrozenPlayerSlimStatusS2CPayload.Receiver());
		ClientPlayNetworking.registerGlobalReceiver(UseEruptionPayload.ID, new UseEruptionPayload.Receiver());
		ClientPlayNetworking.registerGlobalReceiver(UseLightningDashPayload.ID, new UseLightningDashPayload.Receiver());
	}
}
