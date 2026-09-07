package moriyashiine.enchancement.mixin.config.overhaulenchanting;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.EnchancementConfig;
import moriyashiine.enchancement.common.util.config.OverhaulMode;
import net.minecraft.core.TypedInstance;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.ChiseledBookShelfBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TypedInstance.class)
public interface TypedInstanceMixin {
	@ModifyReturnValue(method = "is(Lnet/minecraft/tags/TagKey;)Z", at = @At("RETURN"))
	private <T> boolean enchancement$overhaulEnchanting(boolean original, TagKey<T> tag) {
		if (!original && tag == BlockTags.ENCHANTMENT_POWER_PROVIDER && EnchancementConfig.overhaulEnchanting != OverhaulMode.DISABLED && this instanceof BlockState state && state.getBlock() instanceof ChiseledBookShelfBlock) {
			return true;
		}
		return original;
	}
}
