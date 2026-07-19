package moriyashiine.enchancement.common.event.enchantmenteffectcomponenttype;

import moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype.util.PushComponent;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.ladysnake.cca.api.v3.component.ComponentKey;

public class EquipmentResetEvent implements ServerEntityEvents.EquipmentChange {
	public static void init() {
		ServerEntityEvents.EQUIPMENT_CHANGE.register(new EquipmentResetEvent());
	}

	public static void maybeReset(LivingEntity entity, ItemStack previous, ItemStack current) {
		if (!ItemStack.matchesIgnoringComponents(previous, current, DataComponentType::ignoreSwapAnimation)) {
			for (ComponentKey<?> key : entity.asComponentProvider().getComponentContainer().keys()) {
				if (entity.getComponent(key) instanceof PushComponent pushComponent && EnchantmentHelper.has(current, pushComponent.getEffectType())) {
					pushComponent.resetNextTick();
				}
			}
		}
	}

	@Override
	public void onChange(LivingEntity livingEntity, EquipmentSlot equipmentSlot, ItemStack previousStack, ItemStack currentStack) {
		if (equipmentSlot.getType() != EquipmentSlot.Type.HAND) {
			maybeReset(livingEntity, previousStack, currentStack);
		}
	}
}
