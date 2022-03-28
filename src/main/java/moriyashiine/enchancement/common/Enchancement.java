package moriyashiine.enchancement.common;

import com.google.gson.GsonBuilder;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import moriyashiine.enchancement.common.config.IdentifierTypeAdapter;
import moriyashiine.enchancement.common.config.ModConfig;
import moriyashiine.enchancement.common.packet.AttemptGaleJumpPacket;
import moriyashiine.enchancement.common.packet.SyncDashPacket;
import moriyashiine.enchancement.common.packet.SyncMovingForwardPacket;
import moriyashiine.enchancement.common.registry.ModEnchantments;
import moriyashiine.enchancement.common.registry.ModEntityTypes;
import moriyashiine.enchancement.common.registry.ModSoundEvents;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class Enchancement implements ModInitializer {
	public static final String MOD_ID = "enchancement";

	private static ConfigHolder<ModConfig> config;

	public static final Map<Enchantment, Integer> CACHED_MAX_LEVELS = new HashMap<>();

	@Override
	public void onInitialize() {
		ServerPlayNetworking.registerGlobalReceiver(SyncMovingForwardPacket.ID, SyncMovingForwardPacket::receive);
		ServerPlayNetworking.registerGlobalReceiver(SyncDashPacket.ID, SyncDashPacket::receive);
		ServerPlayNetworking.registerGlobalReceiver(AttemptGaleJumpPacket.ID, AttemptGaleJumpPacket::receive);
		ModEntityTypes.init();
		ModEnchantments.init();
		ModSoundEvents.init();
		initEvents();
	}

	public static ModConfig getConfig() {
		if (config == null) {
			AutoConfig.register(ModConfig.class, (config1, aClass) -> new GsonConfigSerializer<>(config1, aClass, new GsonBuilder().setPrettyPrinting().registerTypeAdapter(Identifier.class, new IdentifierTypeAdapter()).create()));
			config = AutoConfig.getConfigHolder(ModConfig.class);
		}
		return config.getConfig();
	}

	private void initEvents() {
		UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
			int fireIgnitionLevel = getConfig().fireAspectIgnitionLevel;
			if (fireIgnitionLevel >= 0 && player.isSneaking() && EnchantmentHelper.getLevel(Enchantments.FIRE_ASPECT, player.getStackInHand(hand)) >= fireIgnitionLevel) {
				ActionResult result = Items.FLINT_AND_STEEL.useOnBlock(new ItemUsageContext(player, hand, hitResult));
				if (result != ActionResult.FAIL) {
					return result;
				}
			}
			return ActionResult.PASS;
		});
	}
}
