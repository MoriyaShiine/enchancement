package moriyashiine.enchancement.fabric.mixin.config.overhaulenchanting;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import moriyashiine.enchancement.common.EnchancementConfig;
import moriyashiine.enchancement.common.util.config.OverhaulMode;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EnchantingTableBlock;
import net.minecraft.world.level.block.entity.ChiseledBookShelfBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EnchantingTableBlock.class)
public class EnchantingTableBlockMixin {
	@ModifyExpressionValue(method = "isValidBookShelf", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/tags/TagKey;)Z", ordinal = 0))
	private static boolean enchancement$overhaulEnchanting(boolean original, Level level, BlockPos pos, BlockPos offset) {
		return original || (EnchancementConfig.overhaulEnchanting != OverhaulMode.DISABLED && level.getBlockEntity(pos.offset(offset)) instanceof ChiseledBookShelfBlockEntity);
	}
}
