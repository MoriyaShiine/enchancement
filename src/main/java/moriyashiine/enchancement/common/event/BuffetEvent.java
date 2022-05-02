/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.enchancement.common.event;

import moriyashiine.enchancement.common.registry.ModEnchantments;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.enchancement.mixin.buffet.LivingEntityAccessor;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.UseAction;

public class BuffetEvent implements ServerTickEvents.EndTick {
	@Override
	public void onEndTick(MinecraftServer server) {
		server.getPlayerManager().getPlayerList().forEach(player -> {
			if (player.getActiveItem().isFood() || player.getActiveItem().getUseAction() == UseAction.DRINK) {
				if (player.getItemUseTime() == player.getActiveItem().getMaxUseTime() / 2 && EnchancementUtil.hasEnchantment(ModEnchantments.BUFFET, player)) {
					((LivingEntityAccessor) player).enchancement$consumeItem();
				}
			}
		});
	}
}
