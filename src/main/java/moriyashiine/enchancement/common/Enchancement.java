/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common;

import moriyashiine.enchancement.client.payload.*;
import moriyashiine.enchancement.common.event.config.*;
import moriyashiine.enchancement.common.event.enchantmenteffectcomponenttype.*;
import moriyashiine.enchancement.common.event.enchantmenteffecttype.FreezeEvent;
import moriyashiine.enchancement.common.event.enchantmenteffecttype.ModifySubmergedMovementSpeedEvent;
import moriyashiine.enchancement.common.event.internal.*;
import moriyashiine.enchancement.common.init.*;
import moriyashiine.enchancement.common.payload.*;
import moriyashiine.enchancement.common.reloadlistener.BaseBlocksReloadListener;
import moriyashiine.enchancement.common.reloadlistener.EnchantingMaterialsReloadListener;
import moriyashiine.enchancement.common.reloadlistener.HeadDropsReloadListener;
import moriyashiine.enchancement.common.util.enchantment.effect.EruptionMaceEffect;
import moriyashiine.enchancement.common.util.enchantment.effect.LightningDashMaceEffect;
import moriyashiine.enchancement.common.util.enchantment.effect.MaceEffect;
import moriyashiine.enchancement.common.util.enchantment.effect.WindBurstMaceEffect;
import moriyashiine.strawberrylib.api.SLib;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Enchancement implements ModInitializer {
	public static final String MOD_ID = "enchancement";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static boolean isLoaded = false;
	public static boolean isApoliLoaded = false;
	public static boolean commonEnchantmentDescriptionsModLoaded = false;

	@Override
	public void onInitialize() {
		SLib.init(MOD_ID);
		initRegistries();
		initPayloads();
		initEvents();
		ResourceLoader.get(PackType.SERVER_DATA).registerReloadListener(id("base_blocks"), new BaseBlocksReloadListener());
		ResourceLoader.get(PackType.SERVER_DATA).registerReloadListener(id("enchanting_materials"), new EnchantingMaterialsReloadListener());
		ResourceLoader.get(PackType.SERVER_DATA).registerReloadListener(id("head_drops"), new HeadDropsReloadListener());
		isLoaded = true;
		isApoliLoaded = FabricLoader.getInstance().isModLoaded("apoli");
		for (String mod : new String[]{"enchdesc", "enchantedtooltips", "idwtialsimmoedm"}) {
			if (FabricLoader.getInstance().isModLoaded(mod)) {
				commonEnchantmentDescriptionsModLoaded = true;
				break;
			}
		}
	}

	public static Identifier id(String value) {
		return Identifier.fromNamespaceAndPath(MOD_ID, value);
	}

	private void initRegistries() {
		EnchancementDataComponents.init();
		EnchancementEnchantmentEffectComponentTypes.init();
		EnchancementEnchantmentEntityEffectTypes.init();
		EnchancementEntityTypes.init();
		EnchancementLootConditionTypes.init();
		EnchancementLootFunctionTypes.init();
		EnchancementMenuTypes.init();
		EnchancementParticleTypes.init();
		EnchancementSoundEvents.init();

		MaceEffect.EFFECTS.add(new EruptionMaceEffect());
		MaceEffect.EFFECTS.add(new LightningDashMaceEffect());
		MaceEffect.EFFECTS.add(new WindBurstMaceEffect());
	}

	private void initPayloads() {
		// client payloads
		PayloadTypeRegistry.clientboundPlay().register(EnforceConfigMatchPayload.TYPE, EnforceConfigMatchPayload.CODEC);
		PayloadTypeRegistry.clientboundPlay().register(SyncBookshelvesPayload.TYPE, SyncBookshelvesPayload.CODEC);
		PayloadTypeRegistry.clientboundPlay().register(SyncEnchantingMaterialMapPayload.TYPE, SyncEnchantingMaterialMapPayload.CODEC);
		PayloadTypeRegistry.clientboundPlay().register(SyncEnchantingTableCostPayload.TYPE, SyncEnchantingTableCostPayload.CODEC);
		PayloadTypeRegistry.clientboundPlay().register(SyncHookedMovementDeltaPayload.TYPE, SyncHookedMovementDeltaPayload.CODEC);
		PayloadTypeRegistry.clientboundPlay().register(SyncOriginalMaxLevelsPayload.TYPE, SyncOriginalMaxLevelsPayload.CODEC);
		PayloadTypeRegistry.clientboundPlay().register(AddLightningDashParticlesPayload.TYPE, AddLightningDashParticlesPayload.CODEC);
		PayloadTypeRegistry.clientboundPlay().register(AddMoltenParticlesPayload.TYPE, AddMoltenParticlesPayload.CODEC);
		PayloadTypeRegistry.clientboundPlay().register(BoostInFluidS2CPayload.TYPE, BoostInFluidS2CPayload.CODEC);
		PayloadTypeRegistry.clientboundPlay().register(EMeterS2CPayload.TYPE, EMeterS2CPayload.CODEC);
		PayloadTypeRegistry.clientboundPlay().register(GlideS2CPayload.TYPE, GlideS2CPayload.CODEC);
		PayloadTypeRegistry.clientboundPlay().register(PlayBrimstoneFireSoundPayload.TYPE, PlayBrimstoneFireSoundPayload.CODEC);
		PayloadTypeRegistry.clientboundPlay().register(PlayBrimstoneTravelSoundPayload.TYPE, PlayBrimstoneTravelSoundPayload.CODEC);
		PayloadTypeRegistry.clientboundPlay().register(PlayEMeterFloatSoundPayload.TYPE, PlayEMeterFloatSoundPayload.CODEC);
		PayloadTypeRegistry.clientboundPlay().register(StartSlammingS2CPayload.TYPE, StartSlammingS2CPayload.CODEC);
		PayloadTypeRegistry.clientboundPlay().register(SlideS2CPayload.TYPE, SlideS2CPayload.CODEC);
		PayloadTypeRegistry.clientboundPlay().register(StopSlammingS2CPayload.TYPE, StopSlammingS2CPayload.CODEC);
		PayloadTypeRegistry.clientboundPlay().register(SyncFrozenPlayerSlimStatusS2CPayload.TYPE, SyncFrozenPlayerSlimStatusS2CPayload.CODEC);
		PayloadTypeRegistry.clientboundPlay().register(UseEruptionPayload.TYPE, UseEruptionPayload.CODEC);
		PayloadTypeRegistry.clientboundPlay().register(UseLightningDashPayload.TYPE, UseLightningDashPayload.CODEC);
		// common payloads
		PayloadTypeRegistry.serverboundPlay().register(SyncDeltaMovementPayload.TYPE, SyncDeltaMovementPayload.CODEC);
		PayloadTypeRegistry.serverboundPlay().register(AirJumpPayload.TYPE, AirJumpPayload.CODEC);
		PayloadTypeRegistry.serverboundPlay().register(BoostInFluidC2SPayload.TYPE, BoostInFluidC2SPayload.CODEC);
		PayloadTypeRegistry.serverboundPlay().register(ChargeJumpPayload.TYPE, ChargeJumpPayload.CODEC);
		PayloadTypeRegistry.serverboundPlay().register(DirectionBurstPayload.TYPE, DirectionBurstPayload.CODEC);
		PayloadTypeRegistry.serverboundPlay().register(EMeterC2SPayload.TYPE, EMeterC2SPayload.CODEC);
		PayloadTypeRegistry.serverboundPlay().register(GlideC2SPayload.TYPE, GlideC2SPayload.CODEC);
		PayloadTypeRegistry.serverboundPlay().register(RotationBurstPayload.TYPE, RotationBurstPayload.CODEC);
		PayloadTypeRegistry.serverboundPlay().register(StartSlammingC2SPayload.TYPE, StartSlammingC2SPayload.CODEC);
		PayloadTypeRegistry.serverboundPlay().register(SlideC2SPayload.TYPE, SlideC2SPayload.CODEC);
		PayloadTypeRegistry.serverboundPlay().register(StopSlammingC2SPayload.TYPE, StopSlammingC2SPayload.CODEC);
		PayloadTypeRegistry.serverboundPlay().register(SyncFrozenPlayerSlimStatusC2SPayload.TYPE, SyncFrozenPlayerSlimStatusC2SPayload.CODEC);
		PayloadTypeRegistry.serverboundPlay().register(SyncInvertedBounceStatusPayload.TYPE, SyncInvertedBounceStatusPayload.CODEC);
		PayloadTypeRegistry.serverboundPlay().register(UpdateWideMiningEntryPayload.TYPE, UpdateWideMiningEntryPayload.CODEC);
		PayloadTypeRegistry.serverboundPlay().register(WallJumpPayload.TYPE, WallJumpPayload.CODEC);
		PayloadTypeRegistry.serverboundPlay().register(WallJumpSlidingPayload.TYPE, WallJumpSlidingPayload.CODEC);
		// server receivers
		ServerPlayNetworking.registerGlobalReceiver(SyncDeltaMovementPayload.TYPE, new SyncDeltaMovementPayload.Receiver());
		ServerPlayNetworking.registerGlobalReceiver(AirJumpPayload.TYPE, new AirJumpPayload.Receiver());
		ServerPlayNetworking.registerGlobalReceiver(BoostInFluidC2SPayload.TYPE, new BoostInFluidC2SPayload.Receiver());
		ServerPlayNetworking.registerGlobalReceiver(ChargeJumpPayload.TYPE, new ChargeJumpPayload.Receiver());
		ServerPlayNetworking.registerGlobalReceiver(DirectionBurstPayload.TYPE, new DirectionBurstPayload.Receiver());
		ServerPlayNetworking.registerGlobalReceiver(EMeterC2SPayload.TYPE, new EMeterC2SPayload.Receiver());
		ServerPlayNetworking.registerGlobalReceiver(GlideC2SPayload.TYPE, new GlideC2SPayload.Receiver());
		ServerPlayNetworking.registerGlobalReceiver(RotationBurstPayload.TYPE, new RotationBurstPayload.Receiver());
		ServerPlayNetworking.registerGlobalReceiver(StartSlammingC2SPayload.TYPE, new StartSlammingC2SPayload.Receiver());
		ServerPlayNetworking.registerGlobalReceiver(SlideC2SPayload.TYPE, new SlideC2SPayload.Receiver());
		ServerPlayNetworking.registerGlobalReceiver(StopSlammingC2SPayload.TYPE, new StopSlammingC2SPayload.Receiver());
		ServerPlayNetworking.registerGlobalReceiver(SyncFrozenPlayerSlimStatusC2SPayload.TYPE, new SyncFrozenPlayerSlimStatusC2SPayload.Receiver());
		ServerPlayNetworking.registerGlobalReceiver(SyncInvertedBounceStatusPayload.TYPE, new SyncInvertedBounceStatusPayload.Receiver());
		ServerPlayNetworking.registerGlobalReceiver(UpdateWideMiningEntryPayload.TYPE, new UpdateWideMiningEntryPayload.Receiver());
		ServerPlayNetworking.registerGlobalReceiver(WallJumpPayload.TYPE, new WallJumpPayload.Receiver());
		ServerPlayNetworking.registerGlobalReceiver(WallJumpSlidingPayload.TYPE, new WallJumpSlidingPayload.Receiver());
	}

	private void initEvents() {
		// internal
		CacheEnchantmentRegistryEvent.init();
		EnforceConfigMatchEvent.init();
		InCombatEvent.init();
		MaceEnchantmentsEvent.init();
		SyncDeltaMovementsEvent.init();
		SyncEnchantingMaterialMapEvent.init();
		SyncOriginalMaxLevelsEvent.init();
		// config
		EnhanceMobsEvent.init();
		OverhaulEnchantingEvent.init();
		RebalanceEnchantmentsEvent.init();
		RebalanceEquipmentEvent.init();
		ToggleablePassivesEvent.init();
		// enchantment effect type
		FreezeEvent.init();
		ModifySubmergedMovementSpeedEvent.init();
		// enchantment effect component type
		AllowInterruptionEvent.init();
		ArmorDentingEvent.init();
		BounceEvent.init();
		BuryEntityEvent.init();
		ChainLightningEvent.init();
		ChargeJumpEvent.init();
		CriticalTipperEvent.init();
		EMeterEvent.init();
		EquipmentResetEvent.init();
		FellTreesEvent.init();
		FluidWalkingEvent.init();
		HeadDropsEvent.init();
		HeadshotEvent.init();
		LeechingTridentEvent.init();
		LightningDashEvent.init();
		MineOreVeinsEvent.init();
		RageEvent.init();
		RotationBurstEvent.init();
		SlamEvent.init();
		SlideEvent.init();
		WideMiningEvent.init();
	}
}
