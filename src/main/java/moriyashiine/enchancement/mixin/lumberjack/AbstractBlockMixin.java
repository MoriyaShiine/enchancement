/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.lumberjack;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.event.LumberjackEvent;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;

@Mixin(AbstractBlock.class)
public class AbstractBlockMixin {
	@ModifyReturnValue(method = "calcBlockBreakingDelta", at = @At("RETURN"))
	private float enchancement$lumberjack(float original, BlockState state, PlayerEntity player, BlockView world, BlockPos pos) {
		if (LumberjackEvent.canActivate(player, player.getMainHandStack(), state)) {
			LumberjackEvent.Entry entry = LumberjackEvent.Entry.get(player);
			if (entry == null) {
				entry = new LumberjackEvent.Entry(player, LumberjackEvent.gatherTree(new ArrayList<>(), world, new BlockPos.Mutable().set(pos), state.getBlock()));
				LumberjackEvent.ENTRIES.add(entry);
			}
			if (LumberjackEvent.isValid(entry.tree())) {
				return original * MathHelper.lerp(Math.min(1, entry.tree().size() / 24F), 1, 0.1F);
			}
		}
		return original;
	}
}
