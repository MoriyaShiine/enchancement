package moriyashiine.enchancement.common.event.config;

import moriyashiine.enchancement.api.event.CappedMultiplyDeltaMovementEvent;
import moriyashiine.enchancement.common.EnchancementConfig;
import moriyashiine.enchancement.common.component.entity.config.AirMobilityComponent;
import moriyashiine.enchancement.common.init.EnchancementDataComponents;
import moriyashiine.enchancement.common.init.EnchancementEntityComponents;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.strawberrylib.api.event.ModifyDestroySpeedEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public class ToggleablePassivesEvent {
	public static void init() {
		CappedMultiplyDeltaMovementEvent.EVENT.register(new AirMobility());
		ModifyDestroySpeedEvent.ADD_EFFICIENCY.register(new Efficiency());
	}

	private static class AirMobility implements CappedMultiplyDeltaMovementEvent {
		@Override
		public float multiply(Level level, LivingEntity living) {
			if (EnchancementConfig.toggleablePassives && !living.onGround()) {
				AirMobilityComponent airMobility = EnchancementEntityComponents.AIR_MOBILITY.getNullable(living);
				if (airMobility != null) {
					return airMobility.getModifier();
				}
			}
			return 1;
		}
	}

	private static class Efficiency implements ModifyDestroySpeedEvent {
		@Override
		public float modify(Player player, ItemStack stack, Level level, BlockState state, @Nullable BlockPos pos) {
			if (hasEfficiency(stack)) {
				return EnchancementUtil.hasWeakEnchantments(stack) ? 9 : 25;
			}
			return 0;
		}

		private static boolean hasEfficiency(ItemStack stack) {
			if (EnchancementConfig.toggleablePassives) {
				if (stack.is(ItemTags.MINING_ENCHANTABLE) && stack.getOrDefault(EnchancementDataComponents.TOGGLEABLE_PASSIVE, false)) {
					if (stack.isEnchanted()) {
						return true;
					}
					stack.remove(EnchancementDataComponents.TOGGLEABLE_PASSIVE);
				}
			}
			return false;
		}
	}
}
