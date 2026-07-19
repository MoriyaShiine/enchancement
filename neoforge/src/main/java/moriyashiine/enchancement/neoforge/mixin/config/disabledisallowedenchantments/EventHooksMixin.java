package moriyashiine.enchancement.neoforge.mixin.config.disabledisallowedenchantments;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.neoforge.event.EventHooks;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EventHooks.class)
public class EventHooksMixin {
	@WrapMethod(method = "getEnchantmentLevelSpecific")
	private static int enchancement$disableDisallowedEnchantments(int level, ItemInstance stack, Holder<Enchantment> ench, Operation<Integer> original) {
		if (!EnchancementUtil.isEnchantmentAllowed(ench)) {
			return 0;
		}
		return original.call(level, stack, ench);
	}
}
