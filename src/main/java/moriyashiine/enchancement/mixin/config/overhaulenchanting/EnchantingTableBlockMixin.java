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
import moriyashiine.enchancement.common.util.config.OverhaulMode;
import moriyashiine.enchancement.common.world.inventory.ModEnchantmentMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EnchantingTableBlock;
import net.minecraft.world.level.block.entity.ChiseledBookShelfBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnchantingTableBlock.class)
public class EnchantingTableBlockMixin {
	@Inject(method = "getMenuProvider", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/SimpleMenuProvider;<init>(Lnet/minecraft/world/inventory/MenuConstructor;Lnet/minecraft/network/chat/Component;)V"), cancellable = true)
	private void enchancement$overhaulEnchanting(BlockState state, Level level, BlockPos pos, CallbackInfoReturnable<MenuProvider> cir, @Local(name = "title") Component title) {
		if (ModConfig.overhaulEnchanting != OverhaulMode.DISABLED) {
			cir.setReturnValue(new SimpleMenuProvider((syncId, inventory, _) -> new ModEnchantmentMenu(syncId, inventory, ContainerLevelAccess.create(level, pos), level), title));
		}
	}

	@ModifyExpressionValue(method = "isValidBookShelf", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/tags/TagKey;)Z", ordinal = 0))
	private static boolean enchancement$overhaulEnchanting(boolean original, Level level, BlockPos pos, BlockPos offset) {
		return original || (ModConfig.overhaulEnchanting != OverhaulMode.DISABLED && level.getBlockEntity(pos.offset(offset)) instanceof ChiseledBookShelfBlockEntity);
	}

	@WrapOperation(method = "animateTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V"))
	private void enchancement$overhaulEnchanting(Level instance, ParticleOptions particle, double x, double y, double z, double xd, double yd, double zd, Operation<Void> original, @Local(argsOnly = true) BlockPos pos, @Local(name = "offset") BlockPos offset) {
		if (ModConfig.overhaulEnchanting == OverhaulMode.CHISELED && instance.getBlockEntity(pos.offset(offset)) instanceof ChiseledBookShelfBlockEntity chiseledBookshelfBlockEntity && ModBlockComponents.CHISELED_BOOKSHELF.get(chiseledBookshelfBlockEntity).hasEnchantments()) {
			particle = ModParticleTypes.CHISELED_ENCHANT;
		}
		original.call(instance, particle, x, y, z, xd, yd, zd);
	}
}
