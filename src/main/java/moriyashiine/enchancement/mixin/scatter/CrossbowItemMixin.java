/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.scatter;

import moriyashiine.enchancement.common.entity.projectile.AmethystShardEntity;
import moriyashiine.enchancement.common.init.ModEnchantments;
import moriyashiine.enchancement.common.init.ModSoundEvents;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.CrossbowUser;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CrossbowItem.class)
public class CrossbowItemMixin {
	@Inject(method = "shoot", at = @At("HEAD"), cancellable = true)
	private static void enchancement$scatter(World world, LivingEntity shooter, Hand hand, ItemStack crossbow, ItemStack projectile, float soundPitch, boolean creative, float speed, float divergence, float simulated, CallbackInfo ci) {
		if (!world.isClient && (projectile.isOf(Items.AMETHYST_SHARD) || !(shooter instanceof PlayerEntity))) {
			int level = EnchantmentHelper.getLevel(ModEnchantments.SCATTER, crossbow);
			if (level > 0) {
				speed /= 2;
				int count = MathHelper.nextInt(world.random, level * 6, level * 8);
				int multishot = EnchantmentHelper.getLevel(Enchantments.MULTISHOT, crossbow);
				while (multishot > 0) {
					count = (int) (count * 1.5F);
					multishot--;
				}
				for (int i = 0; i < count; i++) {
					AmethystShardEntity projectileEntity = new AmethystShardEntity(world, shooter);
					if (shooter instanceof CrossbowUser crossbowUser) {
						crossbowUser.shoot(crossbowUser.getTarget(), crossbow, projectileEntity, simulated);
					} else {
						Vec3d opposite = shooter.getOppositeRotationVector(1);
						Vector3f velocity = shooter.getRotationVec(1).toVector3f().rotate(new Quaternionf().setAngleAxis(simulated * ((float) Math.PI / 180), opposite.x, opposite.y, opposite.z));
						projectileEntity.setVelocity(velocity.x(), velocity.y(), velocity.z(), speed, 0);
					}
					projectileEntity.setVelocity(projectileEntity.getVelocity().getX(), projectileEntity.getVelocity().getY(), projectileEntity.getVelocity().getZ(), speed, 16);
					world.spawnEntity(projectileEntity);
				}
				crossbow.damage(1, shooter, stackUser -> stackUser.sendToolBreakStatus(hand));
				world.playSound(null, shooter.getX(), shooter.getY(), shooter.getZ(), ModSoundEvents.ITEM_CROSSBOW_SCATTER, SoundCategory.PLAYERS, 1, soundPitch);
				if (shooter instanceof PlayerEntity player) {
					player.getItemCooldownManager().set(crossbow.getItem(), 20);
				}
				ci.cancel();
			}
		}
	}
}
