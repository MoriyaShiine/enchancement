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
import moriyashiine.enchancement.common.util.EnchancementUtil;
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

	public static boolean isApoliLoaded = false, isSpectrumLoaded = false;
	public static boolean commonEnchantmentDescriptionsModLoaded = false;

	@Override
	public void onInitialize() {
		ModDataComponentTypes.init();
		ModEntityTypes.init();
		ModEnchantments.init();
		ModSoundEvents.init();
		ModScreenHandlerTypes.init();
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new EnchantingMaterialReloadListener());
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new BeheadingReloadListener());
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new ExtractingBaseBlockReloadListener());
		initEvents();
		initPayloads();
		isApoliLoaded = FabricLoader.getInstance().isModLoaded("apoli");
		isSpectrumLoaded = FabricLoader.getInstance().isModLoaded("spectrum");
		for (String mod : new String[]{"enchdesc", "enchantedtooltips", "idwtialsimmoedm"}) {
			if (FabricLoader.getInstance().isModLoaded(mod)) {
				commonEnchantmentDescriptionsModLoaded = true;
				break;
			}
		}
	}

	public static Identifier id(String value) {
		return new Identifier(MOD_ID, value);
	}

	private void initEvents() {
		ServerPlayConnectionEvents.JOIN.register(new EnforceConfigMatchEvent());
		ServerLifecycleEvents.SERVER_STARTED.register(new InitializeDefaultEnchantmentsEvent.ServerStart());
		ServerLifecycleEvents.END_DATA_PACK_RELOAD.register(new InitializeDefaultEnchantmentsEvent.ReloadResources());
		ServerPlayConnectionEvents.JOIN.register(new SyncEnchantingMaterialMapEvent.Join());
		ServerTickEvents.END_SERVER_TICK.register(new SyncEnchantingMaterialMapEvent.Tick());
		ServerTickEvents.END_SERVER_TICK.register(server -> EnchancementUtil.tickPacketImmunities());
		ServerLifecycleEvents.SERVER_STOPPED.register(server -> EnchancementUtil.PACKET_IMMUNITIES.clear());
		MultiplyMovementSpeedEvent.EVENT.register(new EnchantedChestplateAirMobilityEvent());
		ServerTickEvents.END_SERVER_TICK.register(new AssimilationEvent());
		MultiplyMovementSpeedEvent.EVENT.register(new AdrenalineEvent());
		MultiplyMovementSpeedEvent.EVENT.register(new BuoyEvent());
		ServerEntityEvents.EQUIPMENT_CHANGE.register(new GaleEvent());
		UseBlockCallback.EVENT.register(new FireAspectEvent());
		ServerLivingEntityEvents.AFTER_DEATH.register(new FrostbiteEvent.Freeze());
		ServerLivingEntityEvents.ALLOW_DAMAGE.register(new FrostbiteEvent.HandleDamage());
		ServerLivingEntityEvents.ALLOW_DAMAGE.register(new LeechEvent());
		PlayerBlockBreakEvents.BEFORE.register(new ExtractingEvent());
		ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register(new BeheadingEvent());
		PlayerBlockBreakEvents.BEFORE.register(new LumberjackEvent());
		ServerLivingEntityEvents.ALLOW_DAMAGE.register(new BuryEvent.Unbury());
		UseEntityCallback.EVENT.register(new BuryEvent.Use());
	}

	private void initPayloads() {
		// client payloads
		PayloadTypeRegistry.playS2C().register(EnforceConfigMatchPayload.ID, EnforceConfigMatchPayload.CODEC);
		PayloadTypeRegistry.playS2C().register(SyncEnchantingMaterialMapPayload.ID, SyncEnchantingMaterialMapPayload.CODEC);
		PayloadTypeRegistry.playS2C().register(SyncEnchantingTableBookshelfCountPayload.ID, SyncEnchantingTableBookshelfCountPayload.CODEC);
		PayloadTypeRegistry.playS2C().register(SyncEnchantingTableCostPayload.ID, SyncEnchantingTableCostPayload.CODEC);
		PayloadTypeRegistry.playS2C().register(AddStrafeParticlesPayload.ID, AddStrafeParticlesPayload.CODEC);
		PayloadTypeRegistry.playS2C().register(AddGaleParticlesPayload.ID, AddGaleParticlesPayload.CODEC);
		PayloadTypeRegistry.playS2C().register(PlayBrimstoneSoundPayload.ID, PlayBrimstoneSoundPayload.CODEC);
		PayloadTypeRegistry.playS2C().register(ResetFrozenTicksPayload.ID, ResetFrozenTicksPayload.CODEC);
		PayloadTypeRegistry.playS2C().register(SyncFrozenPlayerSlimStatusS2CPayload.ID, SyncFrozenPlayerSlimStatusS2CPayload.CODEC);
		PayloadTypeRegistry.playS2C().register(AddMoltenParticlesPayload.ID, AddMoltenParticlesPayload.CODEC);
		// common payloads
		PayloadTypeRegistry.playC2S().register(StrafePayload.ID, StrafePayload.CODEC);
		PayloadTypeRegistry.playC2S().register(DashPayload.ID, DashPayload.CODEC);
		PayloadTypeRegistry.playC2S().register(SlideSlamPayload.ID, SlideSlamPayload.CODEC);
		PayloadTypeRegistry.playC2S().register(SlideSetVelocityPayload.ID, SlideSetVelocityPayload.CODEC);
		PayloadTypeRegistry.playC2S().register(SlideResetVelocityPayload.ID, SlideResetVelocityPayload.CODEC);
		PayloadTypeRegistry.playC2S().register(BuoyPayload.ID, BuoyPayload.CODEC);
		PayloadTypeRegistry.playC2S().register(GalePayload.ID, GalePayload.CODEC);
		PayloadTypeRegistry.playC2S().register(SyncFrozenPlayerSlimStatusC2SPayload.ID, SyncFrozenPlayerSlimStatusC2SPayload.CODEC);
		// server receivers
		ServerPlayNetworking.registerGlobalReceiver(StrafePayload.ID, new StrafePayload.Receiver());
		ServerPlayNetworking.registerGlobalReceiver(DashPayload.ID, new DashPayload.Receiver());
		ServerPlayNetworking.registerGlobalReceiver(SlideSlamPayload.ID, new SlideSlamPayload.Receiver());
		ServerPlayNetworking.registerGlobalReceiver(SlideSetVelocityPayload.ID, new SlideSetVelocityPayload.Receiver());
		ServerPlayNetworking.registerGlobalReceiver(SlideResetVelocityPayload.ID, new SlideResetVelocityPayload.Receiver());
		ServerPlayNetworking.registerGlobalReceiver(BuoyPayload.ID, new BuoyPayload.Receiver());
		ServerPlayNetworking.registerGlobalReceiver(GalePayload.ID, new GalePayload.Receiver());
		ServerPlayNetworking.registerGlobalReceiver(SyncFrozenPlayerSlimStatusC2SPayload.ID, new SyncFrozenPlayerSlimStatusC2SPayload.Receiver());
	}
}
