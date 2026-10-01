package moriyashiine.enchancement.mixin.config.rebalanceequipment;

import moriyashiine.enchancement.common.EnchancementConfig;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.Items;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilMenu.class)
public abstract class AnvilMenuMixin extends ItemCombinerMenu {
	@Shadow
	private int repairItemCountCost;

	public AnvilMenuMixin(@Nullable MenuType<?> menuType, int containerId, Inventory inventory, ContainerLevelAccess access, ItemCombinerMenuSlotDefinition itemInputSlots) {
		super(menuType, containerId, inventory, access, itemInputSlots);
	}

	@Inject(method = "createResult", at = @At("TAIL"))
	private void enchancement$rebalanceEquipment(CallbackInfo ci) {
		if (EnchancementConfig.rebalanceEquipment && inputSlots.getItem(1).is(Items.ENCHANTED_BOOK)) {
			repairItemCountCost = 1;
		}
	}
}
