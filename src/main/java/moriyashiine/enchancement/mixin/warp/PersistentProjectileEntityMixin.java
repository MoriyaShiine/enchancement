package moriyashiine.enchancement.mixin.warp;

import moriyashiine.enchancement.common.registry.ModEnchantments;
import moriyashiine.enchancement.common.registry.ModSoundEvents;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PersistentProjectileEntity.class)
public abstract class PersistentProjectileEntityMixin {
	@Shadow
	protected abstract ItemStack asItemStack();

	@Inject(method = "onBlockHit", at = @At("TAIL"))
	private void enchancement$warpTrident(BlockHitResult blockHitResult, CallbackInfo ci) {
		if (PersistentProjectileEntity.class.cast(this) instanceof TridentEntity entity && EnchantmentHelper.getLevel(ModEnchantments.WARP, asItemStack()) > 0) {
			Entity owner = entity.getOwner();
			if (owner instanceof LivingEntity living) {
				owner.world.playSoundFromEntity(null, owner, ModSoundEvents.ENTITY_GENERIC_TELEPORT, owner.getSoundCategory(), 1, 1);
				BlockPos pos = blockHitResult.getBlockPos().offset(blockHitResult.getSide());
				living.teleport(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, true);
			}
		}
	}
}
