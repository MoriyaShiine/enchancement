/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.widemining.client;

import com.mojang.blaze3d.vertex.PoseStack;
import moriyashiine.enchancement.client.event.enchantmenteffectcomponenttype.WideMiningClientEvent;
import moriyashiine.strawberrylib.api.module.SLibClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.state.level.BlockOutlineRenderState;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {
	@Unique
	private final Minecraft client = Minecraft.getInstance();

	@Unique
	private boolean callingAgain = false;

	@Shadow
	protected abstract void submitBlockOutline(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, LevelRenderState levelRenderState);

	@Inject(method = "submitBlockOutline", at = @At("TAIL"))
	private void enchancement$wideMining(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, LevelRenderState levelRenderState, CallbackInfo ci) {
		if (!callingAgain && client.level != null && levelRenderState.blockOutlineRenderState != null && SLibClientUtils.isHost(client.player) && WideMiningClientEvent.entry != null) {
			callingAgain = true;
			WideMiningClientEvent.entry.blocks().forEach(pos -> {
				BlockState state = client.level.getBlockState(pos);
				if (!state.isAir() && client.level.getWorldBorder().isWithinBounds(pos)) {
					BlockStateModel model = client.getModelManager().getBlockStateModelSet().get(state);
					boolean isBlockTranslucent = model.hasMaterialFlag(1);
					boolean highContrast = client.options.highContrastBlockOutline().get();
					CollisionContext context = client.gameRenderer.mainCamera().entity() instanceof Entity cameraEntity ? CollisionContext.of(cameraEntity) : CollisionContext.empty();
					VoxelShape shape = state.getShape(client.level, pos, context);
					levelRenderState.blockOutlineRenderState = new BlockOutlineRenderState(pos, isBlockTranslucent, highContrast, shape);
					submitBlockOutline(poseStack, submitNodeCollector, levelRenderState);
				}
			});
			callingAgain = false;
		}
	}
}
