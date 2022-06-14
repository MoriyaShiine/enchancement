/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.enchancement.mixin.vanillachanges.overhaulenchantingtable;

import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.screenhandlers.EnchantingTableScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.EnchantingTableBlock;
import net.minecraft.block.entity.BlockEntity;
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
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(EnchantingTableBlock.class)
public class EnchantingTableBlockMixin {
	@Inject(method = "createScreenHandlerFactory", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/SimpleNamedScreenHandlerFactory;<init>(Lnet/minecraft/screen/ScreenHandlerFactory;Lnet/minecraft/text/Text;)V"), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
	private void enchancement$overhaulEnchantingTable(BlockState state, World world, BlockPos pos, CallbackInfoReturnable<NamedScreenHandlerFactory> cir, BlockEntity blockEntity, Text text) {
		if (ModConfig.overhaulEnchantingTable) {
			cir.setReturnValue(new SimpleNamedScreenHandlerFactory((syncId, inventory, player) -> new EnchantingTableScreenHandler(syncId, inventory, ScreenHandlerContext.create(world, pos)), text));
		}
	}
}
