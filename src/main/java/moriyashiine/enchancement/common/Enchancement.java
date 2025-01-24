/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common;

import moriyashiine.enchancement.api.event.MultiplyMovementSpeedEvent;
import moriyashiine.enchancement.client.payload.*;
import moriyashiine.enchancement.common.event.*;
import moriyashiine.enchancement.common.init.*;
import moriyashiine.enchancement.common.payload.*;
import moriyashiine.enchancement.common.reloadlisteners.EnchantingMaterialReloadListener;
import moriyashiine.enchancement.common.reloadlisteners.HeadDropsReloadListener;
import moriyashiine.enchancement.common.reloadlisteners.MineOreVeinsBaseBlockReloadListener;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.item.v1.EnchantmentEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Enchancement implements ModInitializer {
	public static final String MOD_ID = "enchancement";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static boolean isApoliLoaded = false;
	public static boolean commonEnchantmentDescriptionsModLoaded = false;

	@Override
	public void onInitialize() {
		ModComponentTypes.init();
		ModEnchantmentEffectComponentTypes.init();
		ModEnchantmentEntityEffectTypes.init();
		ModLootConditionTypes.init();
		ModEntityTypes.init();
		ModParticleTypes.init();
		ModSoundEvents.init();
		ModScreenHandlerTypes.init();
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(EnchantingMaterialReloadListener.ID, EnchantingMaterialReloadListener::new);
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new HeadDropsReloadListener());
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new MineOreVeinsBaseBlockReloadListener());
		initEvents();
		initPayloads();
		isApoliLoaded = FabricLoader.getInstance().isModLoaded("apoli");
		for (String mod : new String[]{"enchdesc", "enchantedtooltips", "idwtialsimmoedm"}) {
			if (FabricLoader.getInstance().isModLoaded(mod)) {
				commonEnchantmentDescriptionsModLoaded = true;
				break;
			}
		}
	}

	public static Identifier id(String value) {
		return Identifier.of(MOD_ID, value);
	}

	private void initEvents() {
		// internal
		ServerLifecycleEvents.SERVER_STARTED.register(new CacheEnchantmentRegistryEvent());
		ServerPlayConnectionEvents.JOIN.register(new EnforceConfigMatchEvent());
		ServerPlayConnectionEvents.JOIN.register(new SyncEnchantingMaterialMapEvent.Join());
		ServerTickEvents.END_SERVER_TICK.register(new SyncEnchantingMaterialMapEvent.Tick());
		// config
		EnchantmentEvents.ALLOW_ENCHANTING.register(new RebalanceEnchantmentsEvent.AllowEnchanting());
		ServerLifecycleEvents.SERVER_STARTED.register(new RebalanceEnchantmentsEvent.ServerStarted());
		UseBlockCallback.EVENT.register(new RebalanceEnchantmentsEvent.UseBlock());
		UseItemCallback.EVENT.register(new RebalanceEnchantmentsEvent.UseItem());
		MultiplyMovementSpeedEvent.EVENT.register(new ToggleablePassivesEvent());
		// enchantment
		ServerEntityEvents.EQUIPMENT_CHANGE.register(new EquipmentResetEvent());
		ServerLivingEntityEvents.ALLOW_DAMAGE.register(new AllowInterruptionEvent());
		ServerLivingEntityEvents.ALLOW_DAMAGE.register(new BuryEntityEvent.Unbury());
		UseEntityCallback.EVENT.register(new BuryEntityEvent.Use());
		ServerLivingEntityEvents.ALLOW_DAMAGE.register(new ChainLightningEvent());
		PlayerBlockBreakEvents.BEFORE.register(new FellTreesEvent());
		ServerLivingEntityEvents.AFTER_DEATH.register(new FreezeEvent.HandleDeath());
		ServerLivingEntityEvents.ALLOW_DAMAGE.register(new FreezeEvent.HandleDamage());
		ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register(new HeadDropsEvent());
		ServerLivingEntityEvents.ALLOW_DAMAGE.register(new LeechingTridentEvent());
		ServerLivingEntityEvents.ALLOW_DAMAGE.register(new LightningDashEvent());
		PlayerBlockBreakEvents.BEFORE.register(new MineOreVeinsEvent());
		MultiplyMovementSpeedEvent.EVENT.register(new ModifyMovementSpeedEvent());
		MultiplyMovementSpeedEvent.EVENT.register(new RageEvent());
	}

	private void initPayloads() {
		// client payloads
		PayloadTypeRegistry.playS2C().register(EnforceConfigMatchPayload.ID, EnforceConfigMatchPayload.CODEC);
		PayloadTypeRegistry.playS2C().register(SyncEnchantingMaterialMapPayload.ID, SyncEnchantingMaterialMapPayload.CODEC);
		PayloadTypeRegistry.playS2C().register(SyncEnchantingTableBookshelfCountPayload.ID, SyncEnchantingTableBookshelfCountPayload.CODEC);
		PayloadTypeRegistry.playS2C().register(SyncEnchantingTableCostPayload.ID, SyncEnchantingTableCostPayload.CODEC);
		PayloadTypeRegistry.playS2C().register(AddAirJumpParticlesPayload.ID, AddAirJumpParticlesPayload.CODEC);
		PayloadTypeRegistry.playS2C().register(AddLightningDashParticlesPayload.ID, AddLightningDashParticlesPayload.CODEC);
		PayloadTypeRegistry.playS2C().register(AddMoltenParticlesPayload.ID, AddMoltenParticlesPayload.CODEC);
		PayloadTypeRegistry.playS2C().register(AddMovementBurstParticlesPayload.ID, AddMovementBurstParticlesPayload.CODEC);
		PayloadTypeRegistry.playS2C().register(GlideS2CPayload.ID, GlideS2CPayload.CODEC);
		PayloadTypeRegistry.playS2C().register(PlayBrimstoneFireSoundPayload.ID, PlayBrimstoneFireSoundPayload.CODEC);
		PayloadTypeRegistry.playS2C().register(PlayBrimstoneTravelSoundPayload.ID, PlayBrimstoneTravelSoundPayload.CODEC);
		PayloadTypeRegistry.playS2C().register(SyncFrozenPlayerSlimStatusS2CPayload.ID, SyncFrozenPlayerSlimStatusS2CPayload.CODEC);
		PayloadTypeRegistry.playS2C().register(UseEruptionPayload.ID, UseEruptionPayload.CODEC);
		PayloadTypeRegistry.playS2C().register(UseLightningDashPayload.ID, UseLightningDashPayload.CODEC);
		// common payloads
		PayloadTypeRegistry.playC2S().register(SyncVelocityPayload.ID, SyncVelocityPayload.CODEC);
		PayloadTypeRegistry.playC2S().register(AirJumpPayload.ID, AirJumpPayload.CODEC);
		PayloadTypeRegistry.playC2S().register(BoostInFluidPayload.ID, BoostInFluidPayload.CODEC);
		PayloadTypeRegistry.playC2S().register(DirectionBurstPayload.ID, DirectionBurstPayload.CODEC);
		PayloadTypeRegistry.playC2S().register(GlideC2SPayload.ID, GlideC2SPayload.CODEC);
		PayloadTypeRegistry.playC2S().register(RotationBurstPayload.ID, RotationBurstPayload.CODEC);
		PayloadTypeRegistry.playC2S().register(SlamPayload.ID, SlamPayload.CODEC);
		PayloadTypeRegistry.playC2S().register(StartSlidingPayload.ID, StartSlidingPayload.CODEC);
		PayloadTypeRegistry.playC2S().register(StopSlidingPayload.ID, StopSlidingPayload.CODEC);
		PayloadTypeRegistry.playC2S().register(SyncFrozenPlayerSlimStatusC2SPayload.ID, SyncFrozenPlayerSlimStatusC2SPayload.CODEC);
		PayloadTypeRegistry.playC2S().register(SyncInvertedBounceStatusPayload.ID, SyncInvertedBounceStatusPayload.CODEC);
		PayloadTypeRegistry.playC2S().register(WallJumpPayload.ID, WallJumpPayload.CODEC);
		PayloadTypeRegistry.playC2S().register(WallJumpSlidingPayload.ID, WallJumpSlidingPayload.CODEC);
		// server receivers
		ServerPlayNetworking.registerGlobalReceiver(SyncVelocityPayload.ID, new SyncVelocityPayload.Receiver());
		ServerPlayNetworking.registerGlobalReceiver(AirJumpPayload.ID, new AirJumpPayload.Receiver());
		ServerPlayNetworking.registerGlobalReceiver(BoostInFluidPayload.ID, new BoostInFluidPayload.Receiver());
		ServerPlayNetworking.registerGlobalReceiver(DirectionBurstPayload.ID, new DirectionBurstPayload.Receiver());
		ServerPlayNetworking.registerGlobalReceiver(GlideC2SPayload.ID, new GlideC2SPayload.Receiver());
		ServerPlayNetworking.registerGlobalReceiver(RotationBurstPayload.ID, new RotationBurstPayload.Receiver());
		ServerPlayNetworking.registerGlobalReceiver(SlamPayload.ID, new SlamPayload.Receiver());
		ServerPlayNetworking.registerGlobalReceiver(StartSlidingPayload.ID, new StartSlidingPayload.Receiver());
		ServerPlayNetworking.registerGlobalReceiver(StopSlidingPayload.ID, new StopSlidingPayload.Receiver());
		ServerPlayNetworking.registerGlobalReceiver(SyncFrozenPlayerSlimStatusC2SPayload.ID, new SyncFrozenPlayerSlimStatusC2SPayload.Receiver());
		ServerPlayNetworking.registerGlobalReceiver(SyncInvertedBounceStatusPayload.ID, new SyncInvertedBounceStatusPayload.Receiver());
		ServerPlayNetworking.registerGlobalReceiver(WallJumpPayload.ID, new WallJumpPayload.Receiver());
		ServerPlayNetworking.registerGlobalReceiver(WallJumpSlidingPayload.ID, new WallJumpSlidingPayload.Receiver());
	}
}
