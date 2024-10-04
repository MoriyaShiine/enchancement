/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.overhaulenchantingtable;

import com.llamalad7.mixinextras.sugar.Local;
import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.screenhandlers.EnchantingTableScreenHandler;
import moriyashiine.enchancement.common.util.OverhaulMode;
import net.minecraft.block.BlockState;
import net.minecraft.block.EnchantingTableBlock;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnchantingTableBlock.class)
public class EnchantingTableBlockMixin {
	@Inject(method = "createScreenHandlerFactory", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/SimpleNamedScreenHandlerFactory;<init>(Lnet/minecraft/screen/ScreenHandlerFactory;Lnet/minecraft/text/Text;)V"), cancellable = true)
	private void enchancement$overhaulEnchantingTable(BlockState state, World world, BlockPos pos, CallbackInfoReturnable<NamedScreenHandlerFactory> cir, @Local Text text) {
		if (ModConfig.overhaulEnchantingTable != OverhaulMode.DISABLED) {
			cir.setReturnValue(new SimpleNamedScreenHandlerFactory((syncId, inventory, player) -> new EnchantingTableScreenHandler(syncId, inventory, ScreenHandlerContext.create(world, pos), world), text));
		}
	}
}
