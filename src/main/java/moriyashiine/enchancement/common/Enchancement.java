/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.enchancement.common;

import com.google.gson.Gson;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import moriyashiine.enchancement.common.event.*;
import moriyashiine.enchancement.common.packet.*;
import moriyashiine.enchancement.common.registry.ModEnchantments;
import moriyashiine.enchancement.common.registry.ModEntityTypes;
import moriyashiine.enchancement.common.registry.ModScreenHandlerTypes;
import moriyashiine.enchancement.common.registry.ModSoundEvents;
import moriyashiine.enchancement.common.reloadlisteners.BeheadingReloadListener;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;

public class Enchancement implements ModInitializer {
	public static final String MOD_ID = "enchancement";

	public static final ModConfig config;

	static {
		AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);
		config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
	}

	@Override
	public void onInitialize() {
		ServerPlayNetworking.registerGlobalReceiver(StrafePacket.ID, StrafePacket::receive);
		ServerPlayNetworking.registerGlobalReceiver(DashPacket.ID, DashPacket::receive);
		ServerPlayNetworking.registerGlobalReceiver(SyncMovingForwardPacket.ID, SyncMovingForwardPacket::receive);
		ServerPlayNetworking.registerGlobalReceiver(GaleJumpPacket.ID, GaleJumpPacket::receive);
		ServerPlayNetworking.registerGlobalReceiver(SyncFrozenPlayerSlimStatusC2S.ID, SyncFrozenPlayerSlimStatusC2S::receive);
		ModEntityTypes.init();
		ModEnchantments.init();
		ModSoundEvents.init();
		ModScreenHandlerTypes.init();
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new BeheadingReloadListener(new Gson(), MOD_ID + "_beheading"));
		initEvents();
	}

	private void initEvents() {
		ServerTickEvents.END_SERVER_TICK.register(server -> EnchancementUtil.tickPacketImmunities());
		ServerLifecycleEvents.SERVER_STOPPED.register(server -> EnchancementUtil.PACKET_IMMUNITIES.clear());
		ServerTickEvents.END_SERVER_TICK.register(new AssimilationEvent());
		ServerTickEvents.END_SERVER_TICK.register(new BuffetEvent());
		UseBlockCallback.EVENT.register(new FireAspectEvent());
		PlayerBlockBreakEvents.BEFORE.register(new ExtractingEvent());
		ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register(new BeheadingEvent());
		PlayerBlockBreakEvents.BEFORE.register(new LumberjackEvent());
		UseEntityCallback.EVENT.register(new BuryEvent());
	}
}
