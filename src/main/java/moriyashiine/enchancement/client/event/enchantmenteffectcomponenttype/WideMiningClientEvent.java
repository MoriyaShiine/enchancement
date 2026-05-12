/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.client.event.enchantmenteffectcomponenttype;

import moriyashiine.enchancement.common.component.level.WideMiningComponent;
import moriyashiine.enchancement.common.event.enchantmenteffectcomponenttype.WideMiningEvent;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.ArrayList;
import java.util.List;

public class WideMiningClientEvent implements ClientTickEvents.EndLevelTick {
	private static final Minecraft minecraft = Minecraft.getInstance();

	public static WideMiningComponent.Entry entry = null;

	@Override
	public void onEndTick(ClientLevel level) {
		entry = null;
		if (minecraft.player != null && minecraft.hitResult instanceof BlockHitResult result) {
			BlockPos hitPos = result.getBlockPos().immutable();
			BlockState state = level.getBlockState(hitPos);
			if (WideMiningEvent.canActivate(minecraft.player, minecraft.player.getMainHandItem(), state)) {
				WideMiningComponent.Entry maybeEntry = createEntry(minecraft.player, result.getDirection(), minecraft.player.getMainHandItem(), level, hitPos);
				if (WideMiningEvent.isValid(maybeEntry.blocks(), minecraft.player.getMainHandItem())) {
					entry = maybeEntry;
				}
			}
		}
	}

	public static WideMiningComponent.Entry createEntry(Player player, Direction direction, ItemStack stack, Level level, BlockPos pos) {
		List<BlockPos> blocks = gatherBlocks(direction, stack, level, pos);
		float destroySpeed = 0;
		for (BlockPos blockPos : blocks) {
			destroySpeed += level.getBlockState(blockPos).getDestroySpeed(level, blockPos);
		}
		return new WideMiningComponent.Entry(player.getUUID(), pos, blocks, destroySpeed);
	}

	private static List<BlockPos> gatherBlocks(Direction direction, ItemStack stack, BlockGetter level, BlockPos pos) {
		List<BlockPos> blocks = new ArrayList<>();
		BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
		for (int x = -1; x <= 1; x++) {
			for (int y = -1; y <= 1; y++) {
				for (int z = -1; z <= 1; z++) {
					if (!(x == 0 && y == 0 && z == 0)) {
						if ((x == 0 && direction.getStepX() != 0) || (y == 0 && direction.getStepY() != 0) || (z == 0 && direction.getStepZ() != 0)) {
							mutable.set(pos.getX() + x, pos.getY() + y, pos.getZ() + z);
							if (!(level instanceof Level borderLevel) || borderLevel.getWorldBorder().isWithinBounds(mutable)) {
								BlockState state = level.getBlockState(mutable);
								if (!state.hasBlockEntity() && stack.isCorrectToolForDrops(state)) {
									blocks.add(mutable.immutable());
								}
							}
						}
					}
				}
			}
		}
		return blocks;
	}
}
