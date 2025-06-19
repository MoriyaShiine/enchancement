/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.overhaulenchanting;

import moriyashiine.enchancement.common.component.block.ChiseledBookshelfComponent;
import moriyashiine.enchancement.common.init.ModBlockComponents;
import net.minecraft.block.entity.ChiseledBookshelfBlockEntity;
import net.minecraft.storage.ReadView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChiseledBookshelfBlockEntity.class)
public class ChiseledBookshelfBlockEntityMixin {
	@Inject(method = "updateState", at = @At("HEAD"))
	private void enchancement$overhaulEnchanting(int interactedSlot, CallbackInfo ci) {
		ChiseledBookshelfComponent chiseledBookshelfComponent = ModBlockComponents.CHISELED_BOOKSHELF.get(this);
		chiseledBookshelfComponent.update();
		chiseledBookshelfComponent.sync();
	}

	@Inject(method = "readData", at = @At("TAIL"))
	private void enchancement$overhaulEnchanting(ReadView view, CallbackInfo ci) {
		ModBlockComponents.CHISELED_BOOKSHELF.get(this).update();
	}
}
