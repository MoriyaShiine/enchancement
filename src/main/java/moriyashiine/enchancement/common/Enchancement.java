/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common;

import moriyashiine.enchancement.api.event.MultiplyMovementSpeedEvent;
import moriyashiine.enchancement.client.payload.*;
import moriyashiine.enchancement.common.event.config.*;
import moriyashiine.enchancement.common.event.enchantmenteffect.ModifySubmergedMovementSpeedEvent;
import moriyashiine.enchancement.common.event.enchantmenteffectcomponenttype.*;
import moriyashiine.enchancement.common.event.internal.*;
import moriyashiine.enchancement.common.init.*;
import moriyashiine.enchancement.common.payload.*;
import moriyashiine.enchancement.common.reloadlistener.EnchantingMaterialReloadListener;
import moriyashiine.enchancement.common.reloadlistener.HeadDropsReloadListener;
import moriyashiine.enchancement.common.reloadlistener.MineOreVeinsBaseBlockReloadListener;
import moriyashiine.enchancement.common.util.enchantment.EruptionMaceEffect;
import moriyashiine.enchancement.common.util.enchantment.LightningDashMaceEffect;
import moriyashiine.enchancement.common.util.enchantment.MaceEffect;
import moriyashiine.enchancement.common.util.enchantment.WindBurstMaceEffect;
import moriyashiine.strawberrylib.api.SLib;
import moriyashiine.strawberrylib.api.event.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.fabricmc.fabric.api.item.v1.EnchantmentEvents;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
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
		SLib.init(MOD_ID);
		initRegistries();
		initEvents();
		initPayloads();
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(EnchantingMaterialReloadListener.ID, EnchantingMaterialReloadListener::new);
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new HeadDropsReloadListener());
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new MineOreVeinsBaseBlockReloadListener());
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

	private void initRegistries() {
		ModComponentTypes.init();
		ModEnchantmentEffectComponentTypes.init();
		ModEnchantmentEntityEffectTypes.init();
		ModEntityTypes.init();
		ModLootConditionTypes.init();
		ModLootFunctionTypes.init();
		ModParticleTypes.init();
		ModScreenHandlerTypes.init();
		ModSoundEvents.init();

		MaceEffect.EFFECTS.add(new EruptionMaceEffect());
		MaceEffect.EFFECTS.add(new LightningDashMaceEffect());
		MaceEffect.EFFECTS.add(new WindBurstMaceEffect());
	}

	private void initEvents() {
		// internal
		ServerLifecycleEvents.SERVER_STARTED.register(new CacheEnchantmentRegistryEvent());
		ServerPlayConnectionEvents.JOIN.register(new EnforceConfigMatchEvent.Join());
		ServerTickEvents.END_SERVER_TICK.register(new EnforceConfigMatchEvent.Tick());
		ServerLivingEntityEvents.AFTER_DAMAGE.register(new InCombatEvent());
		ServerPlayConnectionEvents.JOIN.register(new SyncEnchantingMaterialMapEvent.Join());
		ServerTickEvents.END_SERVER_TICK.register(new SyncEnchantingMaterialMapEvent.Tick());
		ServerPlayConnectionEvents.JOIN.register(new SyncOriginalMaxLevelsEvent.Join());
		ServerLifecycleEvents.SERVER_STARTED.register(new SyncOriginalMaxLevelsEvent.ServerStarted());
		ServerTickEvents.END_SERVER_TICK.register(new SyncVelocitiesEvent());
		// config
		DefaultItemComponentEvents.MODIFY.register(new AnimalArmorEnchantmentEvent.AllowComponent());
		EnchantmentEvents.ALLOW_ENCHANTING.register(new AnimalArmorEnchantmentEvent.AllowEnchanting());
		LootTableEvents.MODIFY.register(new OverhaulEnchantingEvent());
		EnchantmentEvents.ALLOW_ENCHANTING.register(new RebalanceEnchantmentsEvent.AllowEnchanting());
		ServerLifecycleEvents.SERVER_STARTED.register(new RebalanceEnchantmentsEvent.ServerStarted());
		UseBlockCallback.EVENT.register(new RebalanceEnchantmentsEvent.UseBlock());
		ServerLivingEntityEvents.AFTER_DAMAGE.register(new RebalanceEquipmentEvent.Interrupt());
		TickEntityEvent.EVENT.register(new RebalanceEquipmentEvent.Tick());
		MultiplyMovementSpeedEvent.EVENT.register(new ToggleablePassivesEvent.AirMobility());
		TickEntityEvent.EVENT.register(new ToggleablePassivesEvent.Efficiency());
		// enchantment effect
		MultiplyMovementSpeedEvent.EVENT.register(new ModifySubmergedMovementSpeedEvent());
		// enchantment effect component type
		ServerLivingEntityEvents.AFTER_DAMAGE.register(new AllowInterruptionEvent());
		PreventFallDamageEvent.EVENT.register(new BounceEvent.Bounce());
		ServerTickEvents.END_SERVER_TICK.register(new BounceEvent.DelayedBounce());
		ServerLivingEntityEvents.ALLOW_DAMAGE.register(new BuryEntityEvent.Unbury());
		UseEntityCallback.EVENT.register(new BuryEntityEvent.Use());
		ServerLivingEntityEvents.ALLOW_DAMAGE.register(new ChainLightningEvent());
		ModifyJumpVelocityEvent.EVENT.register(new ChargeJumpEvent());
		ModifyCriticalStatusEvent.EVENT.register(new CriticalTipperEvent());
		ServerEntityEvents.EQUIPMENT_CHANGE.register(new EquipmentResetEvent());
		ModifyBlockBreakingSpeedEvent.MULTIPLY_TOTAL.register(new FellTreesEvent.BreakSpeed());
		PlayerBlockBreakEvents.BEFORE.register(new FellTreesEvent.FellTree());
		PreventFallDamageEvent.EVENT.register(new FluidWalkingEvent());
		ServerLivingEntityEvents.AFTER_DEATH.register(new FreezeEvent.HandleDeath());
		ServerLivingEntityEvents.ALLOW_DAMAGE.register(new FreezeEvent.HandleDamage());
		ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register(new HeadDropsEvent());
		ServerLivingEntityEvents.AFTER_DAMAGE.register(new LeechingTridentEvent());
		PreventFallDamageEvent.EVENT.register(new LightningDashEvent());
		ModifyBlockBreakingSpeedEvent.MULTIPLY_TOTAL.register(new MineOreVeinsEvent.BreakSpeed());
		PlayerBlockBreakEvents.BEFORE.register(new MineOreVeinsEvent.MineOres());
		ModifyDamageTakenEvent.Base.ADD.register(new RageEvent.DamageDealtBonus());
		ModifyDamageTakenEvent.Final.MULTIPLY_TOTAL.register(new RageEvent.DamageTakenReduction());
		MultiplyMovementSpeedEvent.EVENT.register(new RageEvent.SpeedBonus());
		ModifyJumpVelocityEvent.EVENT.register(new RotationBurstEvent());
		PreventFallDamageEvent.EVENT.register(new SlamEvent.FallImmunity());
		ModifyJumpVelocityEvent.EVENT.register(new SlamEvent.JumpBoost());
		ModifyJumpVelocityEvent.EVENT.register(new SlideEvent());
	}

	private void initPayloads() {
		// client payloads
		PayloadTypeRegistry.playS2C().register(EnforceConfigMatchPayload.ID, EnforceConfigMatchPayload.CODEC);
		PayloadTypeRegistry.playS2C().register(SyncBookshelvesPayload.ID, SyncBookshelvesPayload.CODEC);
		PayloadTypeRegistry.playS2C().register(SyncEnchantingMaterialMapPayload.ID, SyncEnchantingMaterialMapPayload.CODEC);
		PayloadTypeRegistry.playS2C().register(SyncEnchantingTableCostPayload.ID, SyncEnchantingTableCostPayload.CODEC);
		PayloadTypeRegistry.playS2C().register(SyncHookedVelocityPayload.ID, SyncHookedVelocityPayload.CODEC);
		PayloadTypeRegistry.playS2C().register(SyncOriginalMaxLevelsPayload.ID, SyncOriginalMaxLevelsPayload.CODEC);
		PayloadTypeRegistry.playS2C().register(AddLightningDashParticlesPayload.ID, AddLightningDashParticlesPayload.CODEC);
		PayloadTypeRegistry.playS2C().register(AddMoltenParticlesPayload.ID, AddMoltenParticlesPayload.CODEC);
		PayloadTypeRegistry.playS2C().register(BoostInFluidS2CPayload.ID, BoostInFluidS2CPayload.CODEC);
		PayloadTypeRegistry.playS2C().register(GlideS2CPayload.ID, GlideS2CPayload.CODEC);
		PayloadTypeRegistry.playS2C().register(PlayBrimstoneFireSoundPayload.ID, PlayBrimstoneFireSoundPayload.CODEC);
		PayloadTypeRegistry.playS2C().register(PlayBrimstoneTravelSoundPayload.ID, PlayBrimstoneTravelSoundPayload.CODEC);
		PayloadTypeRegistry.playS2C().register(StartSlammingS2CPayload.ID, StartSlammingS2CPayload.CODEC);
		PayloadTypeRegistry.playS2C().register(StartSlidingS2CPayload.ID, StartSlidingS2CPayload.CODEC);
		PayloadTypeRegistry.playS2C().register(StopSlammingS2CPayload.ID, StopSlammingS2CPayload.CODEC);
		PayloadTypeRegistry.playS2C().register(StopSlidingS2CPayload.ID, StopSlidingS2CPayload.CODEC);
		PayloadTypeRegistry.playS2C().register(SyncFrozenPlayerSlimStatusS2CPayload.ID, SyncFrozenPlayerSlimStatusS2CPayload.CODEC);
		PayloadTypeRegistry.playS2C().register(UseEruptionPayload.ID, UseEruptionPayload.CODEC);
		PayloadTypeRegistry.playS2C().register(UseLightningDashPayload.ID, UseLightningDashPayload.CODEC);
		// common payloads
		PayloadTypeRegistry.playC2S().register(SyncVelocityPayload.ID, SyncVelocityPayload.CODEC);
		PayloadTypeRegistry.playC2S().register(AirJumpPayload.ID, AirJumpPayload.CODEC);
		PayloadTypeRegistry.playC2S().register(BoostInFluidC2SPayload.ID, BoostInFluidC2SPayload.CODEC);
		PayloadTypeRegistry.playC2S().register(DirectionBurstPayload.ID, DirectionBurstPayload.CODEC);
		PayloadTypeRegistry.playC2S().register(GlideC2SPayload.ID, GlideC2SPayload.CODEC);
		PayloadTypeRegistry.playC2S().register(RotationBurstPayload.ID, RotationBurstPayload.CODEC);
		PayloadTypeRegistry.playC2S().register(StartSlammingC2SPayload.ID, StartSlammingC2SPayload.CODEC);
		PayloadTypeRegistry.playC2S().register(StartSlidingC2SPayload.ID, StartSlidingC2SPayload.CODEC);
		PayloadTypeRegistry.playC2S().register(StopSlammingC2SPayload.ID, StopSlammingC2SPayload.CODEC);
		PayloadTypeRegistry.playC2S().register(StopSlidingC2SPayload.ID, StopSlidingC2SPayload.CODEC);
		PayloadTypeRegistry.playC2S().register(SyncFrozenPlayerSlimStatusC2SPayload.ID, SyncFrozenPlayerSlimStatusC2SPayload.CODEC);
		PayloadTypeRegistry.playC2S().register(SyncInvertedBounceStatusPayload.ID, SyncInvertedBounceStatusPayload.CODEC);
		PayloadTypeRegistry.playC2S().register(WallJumpPayload.ID, WallJumpPayload.CODEC);
		PayloadTypeRegistry.playC2S().register(WallJumpSlidingPayload.ID, WallJumpSlidingPayload.CODEC);
		// server receivers
		ServerPlayNetworking.registerGlobalReceiver(SyncVelocityPayload.ID, new SyncVelocityPayload.Receiver());
		ServerPlayNetworking.registerGlobalReceiver(AirJumpPayload.ID, new AirJumpPayload.Receiver());
		ServerPlayNetworking.registerGlobalReceiver(BoostInFluidC2SPayload.ID, new BoostInFluidC2SPayload.Receiver());
		ServerPlayNetworking.registerGlobalReceiver(DirectionBurstPayload.ID, new DirectionBurstPayload.Receiver());
		ServerPlayNetworking.registerGlobalReceiver(GlideC2SPayload.ID, new GlideC2SPayload.Receiver());
		ServerPlayNetworking.registerGlobalReceiver(RotationBurstPayload.ID, new RotationBurstPayload.Receiver());
		ServerPlayNetworking.registerGlobalReceiver(StartSlammingC2SPayload.ID, new StartSlammingC2SPayload.Receiver());
		ServerPlayNetworking.registerGlobalReceiver(StartSlidingC2SPayload.ID, new StartSlidingC2SPayload.Receiver());
		ServerPlayNetworking.registerGlobalReceiver(StopSlammingC2SPayload.ID, new StopSlammingC2SPayload.Receiver());
		ServerPlayNetworking.registerGlobalReceiver(StopSlidingC2SPayload.ID, new StopSlidingC2SPayload.Receiver());
		ServerPlayNetworking.registerGlobalReceiver(SyncFrozenPlayerSlimStatusC2SPayload.ID, new SyncFrozenPlayerSlimStatusC2SPayload.Receiver());
		ServerPlayNetworking.registerGlobalReceiver(SyncInvertedBounceStatusPayload.ID, new SyncInvertedBounceStatusPayload.Receiver());
		ServerPlayNetworking.registerGlobalReceiver(WallJumpPayload.ID, new WallJumpPayload.Receiver());
		ServerPlayNetworking.registerGlobalReceiver(WallJumpSlidingPayload.ID, new WallJumpSlidingPayload.Receiver());
	}
}
