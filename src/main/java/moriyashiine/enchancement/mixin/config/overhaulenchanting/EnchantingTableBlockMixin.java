/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.overhaulenchanting;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.init.ModBlockComponents;
import moriyashiine.enchancement.common.init.ModParticleTypes;
import moriyashiine.enchancement.common.screenhandlers.EnchantingTableScreenHandler;
import moriyashiine.enchancement.common.util.config.OverhaulMode;
import net.minecraft.block.BlockState;
import net.minecraft.block.EnchantingTableBlock;
import net.minecraft.block.entity.ChiseledBookshelfBlockEntity;
import net.minecraft.particle.ParticleEffect;
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
	private void enchancement$overhaulEnchanting(BlockState state, World world, BlockPos pos, CallbackInfoReturnable<NamedScreenHandlerFactory> cir, @Local Text text) {
		if (ModConfig.overhaulEnchanting != OverhaulMode.DISABLED) {
			cir.setReturnValue(new SimpleNamedScreenHandlerFactory((syncId, inventory, player) -> new EnchantingTableScreenHandler(syncId, inventory, ScreenHandlerContext.create(world, pos), world), text));
		}
	}

	@ModifyExpressionValue(method = "canAccessPowerProvider", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isIn(Lnet/minecraft/registry/tag/TagKey;)Z", ordinal = 0))
	private static boolean enchancement$overhaulEnchanting(boolean original, World world, BlockPos tablePos, BlockPos providerOffset) {
		return original || (ModConfig.overhaulEnchanting != OverhaulMode.DISABLED && world.getBlockEntity(tablePos.add(providerOffset)) instanceof ChiseledBookshelfBlockEntity);
	}

	@WrapOperation(method = "randomDisplayTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;addParticleClient(Lnet/minecraft/particle/ParticleEffect;DDDDDD)V"))
	private void enchancement$overhaulEnchanting(World instance, ParticleEffect parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ, Operation<Void> original, @Local(ordinal = 0, argsOnly = true) BlockPos pos, @Local(ordinal = 1) BlockPos offset) {
		if (ModConfig.overhaulEnchanting == OverhaulMode.CHISELED && instance.getBlockEntity(pos.add(offset)) instanceof ChiseledBookshelfBlockEntity chiseledBookshelfBlockEntity && ModBlockComponents.CHISELED_BOOKSHELF.get(chiseledBookshelfBlockEntity).hasEnchantments()) {
			parameters = ModParticleTypes.CHISELED_ENCHANTMENT;
		}
		original.call(instance, parameters, x, y, z, velocityX, velocityY, velocityZ);
	}
}
