package moriyashiine.enchancement.neoforge.mixin.config.overhaulenchanting;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import moriyashiine.enchancement.common.EnchancementConfig;
import moriyashiine.enchancement.common.util.config.OverhaulMode;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.ChiseledBookShelfBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.extensions.IBlockExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(IBlockExtension.class)
public interface IBlockExtensionMixin {
	@ModifyExpressionValue(method = "getEnchantPowerBonus", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/tags/TagKey;)Z"))
	private static boolean enchancement$overhaulEnchanting(boolean original, BlockState state, BlockGetter level, BlockPos pos) {
		return original || (EnchancementConfig.overhaulEnchanting != OverhaulMode.DISABLED && level.getBlockEntity(pos) instanceof ChiseledBookShelfBlockEntity);
	}
}
