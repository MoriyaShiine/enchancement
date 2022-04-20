package moriyashiine.enchancement.common.enchantment;

import moriyashiine.enchancement.client.packet.ResetFrozenTicksPacket;
import moriyashiine.enchancement.common.component.entity.FrozenComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;

public class FrostbiteEnchantment extends Enchantment {
	public FrostbiteEnchantment(Rarity weight, EnchantmentTarget type, EquipmentSlot... slotTypes) {
		super(weight, type, slotTypes);
	}

	@Override
	protected boolean canAccept(Enchantment other) {
		return super.canAccept(other) && other != Enchantments.FIRE_ASPECT;
	}

	public static void applyEffect(LivingEntity living, DamageSource source, float amount) {
		if (!living.world.isClient && FrozenComponent.isSourceFrostbite(source)) {
			if (living.getHealth() - amount > 0) {
				int frozenTicks = living.getFrozenTicks();
				if (frozenTicks < 600) {
					living.setFrozenTicks(0);
					int targetFrozenTicks = Math.min(600, frozenTicks + 200);
					if (frozenTicks != targetFrozenTicks) {
						living.setFrozenTicks(targetFrozenTicks);
					}
				}
			} else if (living instanceof ServerPlayerEntity player) {
				ResetFrozenTicksPacket.send(player);
			}
		}
	}
}
