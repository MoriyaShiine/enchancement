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
import moriyashiine.enchancement.common.reloadlistener.BaseBlocksReloadListener;
import moriyashiine.enchancement.common.reloadlistener.EnchantingMaterialsReloadListener;
import moriyashiine.enchancement.common.reloadlistener.HeadDropsReloadListener;
import moriyashiine.enchancement.common.util.enchantment.effect.EruptionMaceEffect;
import moriyashiine.enchancement.common.util.enchantment.effect.LightningDashMaceEffect;
import moriyashiine.enchancement.common.util.enchantment.effect.MaceEffect;
import moriyashiine.enchancement.common.util.enchantment.effect.WindBurstMaceEffect;
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
		ModComponentTypes.init();
		ModEnchantmentEffectComponentTypes.init();
		ModEnchantmentEntityEffectTypes.init();
		ModEntityTypes.init();
		ModLootConditionTypes.init();
		ModLootFunctionTypes.init();
		ModMenuTypes.init();
		ModParticleTypes.init();
		ModSoundEvents.init();

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
		PayloadTypeRegistry.clientboundPlay().register(GlideS2CPayload.TYPE, GlideS2CPayload.CODEC);
		PayloadTypeRegistry.clientboundPlay().register(PlayBrimstoneFireSoundPayload.TYPE, PlayBrimstoneFireSoundPayload.CODEC);
		PayloadTypeRegistry.clientboundPlay().register(PlayBrimstoneTravelSoundPayload.TYPE, PlayBrimstoneTravelSoundPayload.CODEC);
		PayloadTypeRegistry.clientboundPlay().register(StartSlammingS2CPayload.TYPE, StartSlammingS2CPayload.CODEC);
		PayloadTypeRegistry.clientboundPlay().register(StartSlidingS2CPayload.TYPE, StartSlidingS2CPayload.CODEC);
		PayloadTypeRegistry.clientboundPlay().register(StopSlammingS2CPayload.TYPE, StopSlammingS2CPayload.CODEC);
		PayloadTypeRegistry.clientboundPlay().register(StopSlidingS2CPayload.TYPE, StopSlidingS2CPayload.CODEC);
		PayloadTypeRegistry.clientboundPlay().register(SyncFrozenPlayerSlimStatusS2CPayload.TYPE, SyncFrozenPlayerSlimStatusS2CPayload.CODEC);
		PayloadTypeRegistry.clientboundPlay().register(UseEruptionPayload.TYPE, UseEruptionPayload.CODEC);
		PayloadTypeRegistry.clientboundPlay().register(UseLightningDashPayload.TYPE, UseLightningDashPayload.CODEC);
		// common payloads
		PayloadTypeRegistry.serverboundPlay().register(SyncDeltaMovementPayload.TYPE, SyncDeltaMovementPayload.CODEC);
		PayloadTypeRegistry.serverboundPlay().register(AirJumpPayload.TYPE, AirJumpPayload.CODEC);
		PayloadTypeRegistry.serverboundPlay().register(BoostInFluidC2SPayload.TYPE, BoostInFluidC2SPayload.CODEC);
		PayloadTypeRegistry.serverboundPlay().register(DirectionBurstPayload.TYPE, DirectionBurstPayload.CODEC);
		PayloadTypeRegistry.serverboundPlay().register(GlideC2SPayload.TYPE, GlideC2SPayload.CODEC);
		PayloadTypeRegistry.serverboundPlay().register(RotationBurstPayload.TYPE, RotationBurstPayload.CODEC);
		PayloadTypeRegistry.serverboundPlay().register(StartSlammingC2SPayload.TYPE, StartSlammingC2SPayload.CODEC);
		PayloadTypeRegistry.serverboundPlay().register(StartSlidingC2SPayload.TYPE, StartSlidingC2SPayload.CODEC);
		PayloadTypeRegistry.serverboundPlay().register(StopSlammingC2SPayload.TYPE, StopSlammingC2SPayload.CODEC);
		PayloadTypeRegistry.serverboundPlay().register(StopSlidingC2SPayload.TYPE, StopSlidingC2SPayload.CODEC);
		PayloadTypeRegistry.serverboundPlay().register(SyncFrozenPlayerSlimStatusC2SPayload.TYPE, SyncFrozenPlayerSlimStatusC2SPayload.CODEC);
		PayloadTypeRegistry.serverboundPlay().register(SyncInvertedBounceStatusPayload.TYPE, SyncInvertedBounceStatusPayload.CODEC);
		PayloadTypeRegistry.serverboundPlay().register(WallJumpPayload.TYPE, WallJumpPayload.CODEC);
		PayloadTypeRegistry.serverboundPlay().register(WallJumpSlidingPayload.TYPE, WallJumpSlidingPayload.CODEC);
		// server receivers
		ServerPlayNetworking.registerGlobalReceiver(SyncDeltaMovementPayload.TYPE, new SyncDeltaMovementPayload.Receiver());
		ServerPlayNetworking.registerGlobalReceiver(AirJumpPayload.TYPE, new AirJumpPayload.Receiver());
		ServerPlayNetworking.registerGlobalReceiver(BoostInFluidC2SPayload.TYPE, new BoostInFluidC2SPayload.Receiver());
		ServerPlayNetworking.registerGlobalReceiver(DirectionBurstPayload.TYPE, new DirectionBurstPayload.Receiver());
		ServerPlayNetworking.registerGlobalReceiver(GlideC2SPayload.TYPE, new GlideC2SPayload.Receiver());
		ServerPlayNetworking.registerGlobalReceiver(RotationBurstPayload.TYPE, new RotationBurstPayload.Receiver());
		ServerPlayNetworking.registerGlobalReceiver(StartSlammingC2SPayload.TYPE, new StartSlammingC2SPayload.Receiver());
		ServerPlayNetworking.registerGlobalReceiver(StartSlidingC2SPayload.TYPE, new StartSlidingC2SPayload.Receiver());
		ServerPlayNetworking.registerGlobalReceiver(StopSlammingC2SPayload.TYPE, new StopSlammingC2SPayload.Receiver());
		ServerPlayNetworking.registerGlobalReceiver(StopSlidingC2SPayload.TYPE, new StopSlidingC2SPayload.Receiver());
		ServerPlayNetworking.registerGlobalReceiver(SyncFrozenPlayerSlimStatusC2SPayload.TYPE, new SyncFrozenPlayerSlimStatusC2SPayload.Receiver());
		ServerPlayNetworking.registerGlobalReceiver(SyncInvertedBounceStatusPayload.TYPE, new SyncInvertedBounceStatusPayload.Receiver());
		ServerPlayNetworking.registerGlobalReceiver(WallJumpPayload.TYPE, new WallJumpPayload.Receiver());
		ServerPlayNetworking.registerGlobalReceiver(WallJumpSlidingPayload.TYPE, new WallJumpSlidingPayload.Receiver());
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
		ServerTickEvents.END_SERVER_TICK.register(new SyncDeltaMovementsEvent());
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
		ModifyDestroySpeedEvent.ADD_EFFICIENCY.register(new ToggleablePassivesEvent.Efficiency());
		// enchantment effect
		MultiplyMovementSpeedEvent.EVENT.register(new ModifySubmergedMovementSpeedEvent());
		// enchantment effect component type
		ServerLivingEntityEvents.AFTER_DAMAGE.register(new AllowInterruptionEvent());
		PreventFallDamageEvent.EVENT.register(new BounceEvent.Bounce());
		ServerTickEvents.END_SERVER_TICK.register(new BounceEvent.DelayedBounce());
		ServerLivingEntityEvents.ALLOW_DAMAGE.register(new BuryEntityEvent.Unbury());
		UseEntityCallback.EVENT.register(new BuryEntityEvent.Use());
		AfterDamageIncludingDeathEvent.EVENT.register(new ChainLightningEvent());
		ModifyMovementEvents.JUMP_DELTA.register(new ChargeJumpEvent());
		ModifyCriticalStatusEvent.EVENT.register(new CriticalTipperEvent());
		ServerEntityEvents.EQUIPMENT_CHANGE.register(new EquipmentResetEvent());
		ModifyDestroySpeedEvent.MULTIPLY_TOTAL.register(new FellTreesEvent.DestroySpeed());
		PlayerBlockBreakEvents.BEFORE.register(new FellTreesEvent.FellTree());
		PreventFallDamageEvent.EVENT.register(new FluidWalkingEvent());
		ServerLivingEntityEvents.AFTER_DEATH.register(new FreezeEvent.HandleDeath());
		ServerLivingEntityEvents.ALLOW_DAMAGE.register(new FreezeEvent.HandleDamage());
		ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register(new HeadDropsEvent());
		ServerLivingEntityEvents.AFTER_DAMAGE.register(new LeechingTridentEvent());
		PreventFallDamageEvent.EVENT.register(new LightningDashEvent());
		ModifyDestroySpeedEvent.MULTIPLY_TOTAL.register(new MineOreVeinsEvent.DestroySpeed());
		PlayerBlockBreakEvents.BEFORE.register(new MineOreVeinsEvent.MineOres());
		ModifyDamageTakenEvent.ADD.register(new RageEvent.DamageDealtBonus());
		ModifyDamageTakenEvent.MULTIPLY_TOTAL.register(new RageEvent.DamageTakenReduction());
		MultiplyMovementSpeedEvent.EVENT.register(new RageEvent.SpeedBonus());
		ModifyMovementEvents.JUMP_DELTA.register(new RotationBurstEvent());
		PreventFallDamageEvent.EVENT.register(new SlamEvent.FallImmunity());
		ModifyMovementEvents.JUMP_DELTA.register(new SlamEvent.JumpBoost());
		ModifyMovementEvents.JUMP_DELTA.register(new SlideEvent());
	}
}
