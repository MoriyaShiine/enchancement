/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.overhaulenchanting;

import moriyashiine.enchancement.common.component.block.ChiseledBookshelfComponent;
import moriyashiine.enchancement.common.init.ModBlockComponents;
import net.minecraft.world.level.block.entity.ChiseledBookShelfBlockEntity;
import net.minecraft.world.level.storage.ValueInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChiseledBookShelfBlockEntity.class)
public class ChiseledBookShelfBlockEntityMixin {
	@Inject(method = "updateState", at = @At("HEAD"))
	private void enchancement$overhaulEnchanting(int interactedSlot, CallbackInfo ci) {
		ChiseledBookshelfComponent chiseledBookshelfComponent = ModBlockComponents.CHISELED_BOOKSHELF.get(this);
		chiseledBookshelfComponent.update();
		chiseledBookshelfComponent.sync();
	}

	@Inject(method = "loadAdditional", at = @At("TAIL"))
	private void enchancement$overhaulEnchanting(ValueInput input, CallbackInfo ci) {
		ModBlockComponents.CHISELED_BOOKSHELF.get(this).update();
	}
}
