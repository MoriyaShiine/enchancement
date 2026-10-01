package moriyashiine.enchancement.mixin.config.enchantedbookgrinding;

import moriyashiine.enchancement.common.EnchancementConfig;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilMenu.class)
public abstract class AnvilMenuMixin extends ItemCombinerMenu {
	public AnvilMenuMixin(@Nullable MenuType<?> menuType, int containerId, Inventory inventory, ContainerLevelAccess access, ItemCombinerMenuSlotDefinition itemInputSlots) {
		super(menuType, containerId, inventory, access, itemInputSlots);
	}

	@Inject(method = "createResult", at = @At("TAIL"))
	private void enchancement$enchantedBookGrinding(CallbackInfo ci) {
		if (EnchancementConfig.enchantedBookGrinding && !inputSlots.getItem(0).is(Items.ENCHANTED_BOOK) && inputSlots.getItem(1).is(Items.ENCHANTED_BOOK)) {
			resultSlots.setItem(0, ItemStack.EMPTY);
			broadcastChanges();
		}
	}
}
