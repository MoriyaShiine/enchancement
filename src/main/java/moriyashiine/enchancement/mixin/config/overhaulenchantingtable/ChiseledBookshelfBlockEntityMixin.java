/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.overhaulenchantingtable;

import moriyashiine.enchancement.common.component.block.ChiseledBookshelfComponent;
import moriyashiine.enchancement.common.init.ModBlockComponents;
import net.minecraft.block.entity.ChiseledBookshelfBlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChiseledBookshelfBlockEntity.class)
public class ChiseledBookshelfBlockEntityMixin {
	@Inject(method = "updateState", at = @At("HEAD"))
	private void enchancement$overhaulEnchantingTable(int interactedSlot, CallbackInfo ci) {
		ChiseledBookshelfComponent chiseledBookshelfComponent = ModBlockComponents.CHISELED_BOOKSHELF.get(this);
		chiseledBookshelfComponent.update();
		chiseledBookshelfComponent.sync();
	}

	@Inject(method = "readNbt", at = @At("TAIL"))
	private void enchancement$overhaulEnchantingTable(NbtCompound nbt, RegistryWrapper.WrapperLookup registries, CallbackInfo ci) {
		ModBlockComponents.CHISELED_BOOKSHELF.get(this).update();
	}
}
