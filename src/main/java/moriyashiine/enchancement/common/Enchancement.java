/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.common;

import com.google.gson.Gson;
import moriyashiine.enchancement.api.event.MultiplyMovementSpeedEvent;
import moriyashiine.enchancement.common.event.*;
import moriyashiine.enchancement.common.init.ModEnchantments;
import moriyashiine.enchancement.common.init.ModEntityTypes;
import moriyashiine.enchancement.common.init.ModScreenHandlerTypes;
import moriyashiine.enchancement.common.init.ModSoundEvents;
import moriyashiine.enchancement.common.packet.*;
import moriyashiine.enchancement.common.reloadlisteners.BeheadingReloadListener;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
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
		ServerPlayNetworking.registerGlobalReceiver(StrafePacket.ID, new StrafePacket.Receiver());
		ServerPlayNetworking.registerGlobalReceiver(DashPacket.ID, new DashPacket.Receiver());
		ServerPlayNetworking.registerGlobalReceiver(SlideSlamPacket.ID, new SlideSlamPacket.Receiver());
		ServerPlayNetworking.registerGlobalReceiver(SlideVelocityPacket.ID, new SlideVelocityPacket.Receiver());
		ServerPlayNetworking.registerGlobalReceiver(BuoyPacket.ID, new BuoyPacket.Receiver());
		ServerPlayNetworking.registerGlobalReceiver(GalePacket.ID, new GalePacket.Receiver());
		ServerPlayNetworking.registerGlobalReceiver(StopBrimstoneSoundsC2SPacket.ID, new StopBrimstoneSoundsC2SPacket.Receiver());
		ServerPlayNetworking.registerGlobalReceiver(SyncFrozenPlayerSlimStatusC2S.ID, new SyncFrozenPlayerSlimStatusC2S.Receiver());
		ModEntityTypes.init();
		ModEnchantments.init();
		ModSoundEvents.init();
		ModScreenHandlerTypes.init();
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new BeheadingReloadListener(new Gson(), MOD_ID + "_beheading"));
		initEvents();
		isApoliLoaded = FabricLoader.getInstance().isModLoaded("apoli");
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
		ServerTickEvents.END_SERVER_TICK.register(server -> EnchancementUtil.tickPacketImmunities());
		ServerLifecycleEvents.SERVER_STOPPED.register(server -> EnchancementUtil.PACKET_IMMUNITIES.clear());
		MultiplyMovementSpeedEvent.EVENT.register(new EnchantedChestplateAirMobilityEvent());
		ServerTickEvents.END_SERVER_TICK.register(new AssimilationEvent());
		MultiplyMovementSpeedEvent.EVENT.register(new AdrenalineEvent());
		MultiplyMovementSpeedEvent.EVENT.register(new BuoyEvent());
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
}
