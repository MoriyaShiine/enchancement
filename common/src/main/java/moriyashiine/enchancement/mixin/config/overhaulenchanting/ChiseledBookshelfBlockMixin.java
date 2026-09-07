package moriyashiine.enchancement.mixin.config.overhaulenchanting;

import moriyashiine.enchancement.common.EnchancementConfig;
import moriyashiine.enchancement.common.util.config.OverhaulMode;
import net.fabricmc.fabric.api.block.v1.FabricBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.ChiseledBookShelfBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(ChiseledBookShelfBlock.class)
public class ChiseledBookshelfBlockMixin implements FabricBlock {
	@Shadow
	@Final
	public static List<BooleanProperty> SLOT_OCCUPIED_PROPERTIES;

	@Override
	public float getProvidedEnchantmentPower(BlockState state, BlockGetter level, BlockPos pos) {
		if (EnchancementConfig.overhaulEnchanting != OverhaulMode.DISABLED) {
			int books = 0;
			for (BooleanProperty property : SLOT_OCCUPIED_PROPERTIES) {
				if (state.getValue(property)) {
					books++;
				}
			}
			return books / 3F;
		}
		return 0;
	}
}
