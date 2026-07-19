/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.widemining.client;

import com.mojang.blaze3d.vertex.PoseStack;
import moriyashiine.enchancement.client.event.enchantmenteffectcomponenttype.WideMiningClientEvent;
import moriyashiine.enchancement.common.init.EnchancementLevelComponents;
import moriyashiine.strawberrylib.api.module.SLibClientUtils;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.state.level.BlockBreakingRenderState;
import net.minecraft.client.renderer.state.level.BlockOutlineRenderState;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;
import java.util.Set;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {
	@Unique
	private boolean callingAgain = false;

	@Shadow
	@Final
	private Minecraft minecraft;

	@Shadow
	private @Nullable ClientLevel level;

	@Shadow
	protected abstract void renderBlockOutline(MultiBufferSource.BufferSource bufferSource, PoseStack poseStack, boolean onlyTranslucentBlocks, LevelRenderState levelRenderState);

	@Inject(method = "extractBlockDestroyAnimation", at = @At("TAIL"))
	private void enchancement$wideMining(Camera camera, LevelRenderState levelRenderState, CallbackInfo ci) {
		if (level != null) {
			Set<BlockPos> rendered = new HashSet<>();
			EnchancementLevelComponents.WIDE_MINING.get(level).getEntries().forEach(entry -> {
				int progress = 0;
				for (BlockBreakingRenderState state : levelRenderState.blockBreakingRenderStates) {
					if (state.blockPos().equals(entry.origin())) {
						progress = state.progress();
						break;
					}
				}
				if (progress > 0 && !rendered.contains(entry.origin())) {
					rendered.add(entry.origin());
					for (BlockPos pos : entry.blocks()) {
						levelRenderState.blockBreakingRenderStates.add(new BlockBreakingRenderState(pos, level.getBlockState(pos), progress));
					}
				}
			});
		}
	}

	@Inject(method = "renderBlockOutline", at = @At("TAIL"))
	private void enchancement$wideMining(MultiBufferSource.BufferSource bufferSource, PoseStack poseStack, boolean onlyTranslucentBlocks, LevelRenderState levelRenderState, CallbackInfo ci) {
		if (!callingAgain && level != null && levelRenderState.blockOutlineRenderState != null && SLibClientUtils.isHost(minecraft.player) && WideMiningClientEvent.entry != null) {
			callingAgain = true;
			WideMiningClientEvent.entry.blocks().forEach(pos -> {
				BlockState state = level.getBlockState(pos);
				if (!state.isAir() && level.getWorldBorder().isWithinBounds(pos)) {
					BlockStateModel model = minecraft.getModelManager().getBlockStateModelSet().get(state);
					boolean isBlockTranslucent = model.hasMaterialFlag(1);
					boolean highContrast = minecraft.options.highContrastBlockOutline().get();
					CollisionContext context = minecraft.gameRenderer.getMainCamera().entity() instanceof Entity cameraEntity ? CollisionContext.of(cameraEntity) : CollisionContext.empty();
					VoxelShape shape = state.getShape(level, pos, context);
					levelRenderState.blockOutlineRenderState = new BlockOutlineRenderState(pos, isBlockTranslucent, highContrast, shape);
					renderBlockOutline(bufferSource, poseStack, onlyTranslucentBlocks, levelRenderState);
				}
			});
			callingAgain = false;
		}
	}
}
