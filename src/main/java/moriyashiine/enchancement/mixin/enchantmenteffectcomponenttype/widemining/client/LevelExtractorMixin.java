/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.widemining.client;

import moriyashiine.enchancement.common.init.EnchancementLevelComponents;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.extract.LevelExtractor;
import net.minecraft.client.renderer.state.level.BlockBreakingRenderState;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.core.BlockPos;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;
import java.util.Set;

@Mixin(LevelExtractor.class)
public abstract class LevelExtractorMixin {
	@Shadow
	private @Nullable ClientLevel level;

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
}
