package moriyashiine.enchancement.mixin.torch;

import moriyashiine.enchancement.common.registry.ModEnchantments;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.function.Predicate;

@Mixin({HostileEntity.class, PlayerEntity.class})
public class TorchCrossbowMixin {
    @ModifyVariable(method = "getProjectileType", at = @At("STORE"), ordinal = 0)
    private Predicate<ItemStack> enchancement$torch(Predicate<ItemStack> value, ItemStack stack) {
        if (EnchancementUtil.hasEnchantment(ModEnchantments.TORCH, stack)) {
            value = value.or(projectile -> projectile.isOf(Items.TORCH));
        }
        return value;
    }
}
