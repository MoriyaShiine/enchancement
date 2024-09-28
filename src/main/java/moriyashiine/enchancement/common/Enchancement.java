/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common;

import moriyashiine.enchancement.api.event.MultiplyMovementSpeedEvent;
import moriyashiine.enchancement.client.payload.*;
import moriyashiine.enchancement.common.event.*;
import moriyashiine.enchancement.common.init.*;
import moriyashiine.enchancement.common.payload.*;
import moriyashiine.enchancement.common.reloadlisteners.BeheadingReloadListener;
import moriyashiine.enchancement.common.reloadlisteners.EnchantingMaterialReloadListener;
import moriyashiine.enchancement.common.reloadlisteners.ExtractingBaseBlockReloadListener;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
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
		ModSoundEvents.init();
		ModScreenHandlerTypes.init();
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new EnchantingMaterialReloadListener());
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new BeheadingReloadListener());
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new ExtractingBaseBlockReloadListener());
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
		ServerLifecycleEvents.SERVER_STARTED.register(new GatherEnchantmentRegistryEvent());
		ServerPlayConnectionEvents.JOIN.register(new EnforceConfigMatchEvent());
		ServerLifecycleEvents.SERVER_STARTED.register(new InitializeDefaultEnchantmentsEvent.ServerStart());
		ServerLifecycleEvents.END_DATA_PACK_RELOAD.register(new InitializeDefaultEnchantmentsEvent.ReloadResources());
		ServerPlayConnectionEvents.JOIN.register(new SyncEnchantingMaterialMapEvent.Join());
		ServerTickEvents.END_SERVER_TICK.register(new SyncEnchantingMaterialMapEvent.Tick());
		MultiplyMovementSpeedEvent.EVENT.register(new EnchantedChestplateAirMobilityEvent());
		ServerEntityEvents.EQUIPMENT_CHANGE.register(new AirJumpEvent());
		ServerLivingEntityEvents.ALLOW_DAMAGE.register(new BuryEntityEvent.Unbury());
		UseEntityCallback.EVENT.register(new BuryEntityEvent.Use());
		PlayerBlockBreakEvents.BEFORE.register(new FellTreesEvent());
		ServerLivingEntityEvents.AFTER_DEATH.register(new FreezeEvent.HandleDeath());
		ServerLivingEntityEvents.ALLOW_DAMAGE.register(new FreezeEvent.HandleDamage());
		ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register(new HeadDropsEvent());
		ServerLivingEntityEvents.ALLOW_DAMAGE.register(new LeechEvent());
		PlayerBlockBreakEvents.BEFORE.register(new MineOreVeinsEvent());
		MultiplyMovementSpeedEvent.EVENT.register(new ModifyMovementSpeedEvent());
		MultiplyMovementSpeedEvent.EVENT.register(new RageEvent());
		ServerLifecycleEvents.SERVER_STARTED.register(new RebalanceChannelingEvent());
		UseBlockCallback.EVENT.register(new RebalanceFireAspectEvent());
	}

	private void initPayloads() {
		// client payloads
		PayloadTypeRegistry.playS2C().register(EnforceConfigMatchPayload.ID, EnforceConfigMatchPayload.CODEC);
		PayloadTypeRegistry.playS2C().register(SyncEnchantingMaterialMapPayload.ID, SyncEnchantingMaterialMapPayload.CODEC);
		PayloadTypeRegistry.playS2C().register(SyncEnchantingTableBookshelfCountPayload.ID, SyncEnchantingTableBookshelfCountPayload.CODEC);
		PayloadTypeRegistry.playS2C().register(SyncEnchantingTableCostPayload.ID, SyncEnchantingTableCostPayload.CODEC);
		PayloadTypeRegistry.playS2C().register(AddStrafeParticlesPayload.ID, AddStrafeParticlesPayload.CODEC);
		PayloadTypeRegistry.playS2C().register(AddAirJumpParticlesPayload.ID, AddAirJumpParticlesPayload.CODEC);
		PayloadTypeRegistry.playS2C().register(PlayBrimstoneSoundPayload.ID, PlayBrimstoneSoundPayload.CODEC);
		PayloadTypeRegistry.playS2C().register(ResetFrozenTicksPayload.ID, ResetFrozenTicksPayload.CODEC);
		PayloadTypeRegistry.playS2C().register(SyncFrozenPlayerSlimStatusS2CPayload.ID, SyncFrozenPlayerSlimStatusS2CPayload.CODEC);
		PayloadTypeRegistry.playS2C().register(AddMoltenParticlesPayload.ID, AddMoltenParticlesPayload.CODEC);
		// common payloads
		PayloadTypeRegistry.playC2S().register(DirectionMovementBurstPayload.ID, DirectionMovementBurstPayload.CODEC);
		PayloadTypeRegistry.playC2S().register(RotationMovementBurstPayload.ID, RotationMovementBurstPayload.CODEC);
		PayloadTypeRegistry.playC2S().register(SlamPayload.ID, SlamPayload.CODEC);
		PayloadTypeRegistry.playC2S().register(StartSlidingPayload.ID, StartSlidingPayload.CODEC);
		PayloadTypeRegistry.playC2S().register(StopSlidingPayload.ID, StopSlidingPayload.CODEC);
		PayloadTypeRegistry.playC2S().register(BoostInFluidPayload.ID, BoostInFluidPayload.CODEC);
		PayloadTypeRegistry.playC2S().register(AirJumpPayload.ID, AirJumpPayload.CODEC);
		PayloadTypeRegistry.playC2S().register(SyncFrozenPlayerSlimStatusC2SPayload.ID, SyncFrozenPlayerSlimStatusC2SPayload.CODEC);
		// server receivers
		ServerPlayNetworking.registerGlobalReceiver(DirectionMovementBurstPayload.ID, new DirectionMovementBurstPayload.Receiver());
		ServerPlayNetworking.registerGlobalReceiver(RotationMovementBurstPayload.ID, new RotationMovementBurstPayload.Receiver());
		ServerPlayNetworking.registerGlobalReceiver(SlamPayload.ID, new SlamPayload.Receiver());
		ServerPlayNetworking.registerGlobalReceiver(StartSlidingPayload.ID, new StartSlidingPayload.Receiver());
		ServerPlayNetworking.registerGlobalReceiver(StopSlidingPayload.ID, new StopSlidingPayload.Receiver());
		ServerPlayNetworking.registerGlobalReceiver(BoostInFluidPayload.ID, new BoostInFluidPayload.Receiver());
		ServerPlayNetworking.registerGlobalReceiver(AirJumpPayload.ID, new AirJumpPayload.Receiver());
		ServerPlayNetworking.registerGlobalReceiver(SyncFrozenPlayerSlimStatusC2SPayload.ID, new SyncFrozenPlayerSlimStatusC2SPayload.Receiver());
	}
}
