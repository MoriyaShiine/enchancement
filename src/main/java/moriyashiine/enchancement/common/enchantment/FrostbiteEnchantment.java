package moriyashiine.enchancement.common.enchantment;

import moriyashiine.enchancement.client.packet.ResetFrozenTicksPacket;
import moriyashiine.enchancement.common.component.entity.FrozenComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;

public class FrostbiteEnchantment extends EmptyEnchantment {
	public FrostbiteEnchantment(Rarity weight, EnchantmentTarget type, EquipmentSlot... slotTypes) {
		super(weight, type, slotTypes);
	}

	@Override
	protected boolean canAccept(Enchantment other) {
		return super.canAccept(other) && other != Enchantments.FIRE_ASPECT;
	}

	public static void applyEffect(Entity target, DamageSource source, float amount) {
		if (!target.world.isClient && target instanceof LivingEntity living && FrozenComponent.isSourceFrostbite(source)) {
			if (living.getHealth() - amount > 0) {
				if (target.getFrozenTicks() < 300) {
					target.setFrozenTicks(300);
				}
			} else if (target instanceof ServerPlayerEntity player) {
				ResetFrozenTicksPacket.send(player);
			}
		}
	}
}
