/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.brimstone;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.client.payload.PlayBrimstoneSoundPayload;
import moriyashiine.enchancement.common.entity.projectile.BrimstoneEntity;
import moriyashiine.enchancement.common.init.ModDamageTypes;
import moriyashiine.enchancement.common.init.ModDataComponentTypes;
import moriyashiine.enchancement.common.init.ModEnchantments;
import moriyashiine.enchancement.common.init.ModSoundEvents;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(CrossbowItem.class)
public abstract class CrossbowItemMixin {
	@Unique
	private static int damage = -1;

	@Shadow
	public abstract int getMaxUseTime(ItemStack stack);

	@Shadow
	private static float getPullProgress(int useTicks, ItemStack stack) {
		throw new UnsupportedOperationException();
	}

	@Shadow
	public static int getPullTime(ItemStack stack) {
		throw new UnsupportedOperationException();
	}

	@Shadow
	private static boolean loadProjectiles(LivingEntity shooter, ItemStack projectile) {
		throw new UnsupportedOperationException();
	}

	@Inject(method = "onStoppedUsing", at = @At("HEAD"), cancellable = true)
	private void enchancement$brimstone(ItemStack stack, World world, LivingEntity user, int remainingUseTicks, CallbackInfo ci) {
		if (EnchancementUtil.hasEnchantment(ModEnchantments.BRIMSTONE, stack) && !CrossbowItem.isCharged(stack)) {
			stack.remove(ModDataComponentTypes.BRIMSTONE_UUID);
			int damage = EnchancementUtil.getBrimstoneDamage(getPullProgress(getMaxUseTime(stack) - remainingUseTicks, stack));
			if (damage > 0 && loadProjectiles(user, stack)) {
				stack.set(ModDataComponentTypes.BRIMSTONE_DAMAGE, damage);
				world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ITEM_CROSSBOW_LOADING_END, user instanceof PlayerEntity ? SoundCategory.PLAYERS : SoundCategory.HOSTILE, 1, 1 / (world.getRandom().nextFloat() * 0.5F + 1) + 0.2F);
				ci.cancel();
			}
		}
	}

	@Inject(method = "usageTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentHelper;getLevel(Lnet/minecraft/enchantment/Enchantment;Lnet/minecraft/item/ItemStack;)I"))
	private void enchancement$brimstone(World world, LivingEntity user, ItemStack stack, int remainingUseTicks, CallbackInfo ci) {
		if (EnchancementUtil.hasEnchantment(ModEnchantments.BRIMSTONE, stack)) {
			UUID[] uuid = {stack.get(ModDataComponentTypes.BRIMSTONE_UUID)};
			if (uuid[0] == null) {
				uuid[0] = UUID.randomUUID();
				stack.set(ModDataComponentTypes.BRIMSTONE_UUID, uuid[0]);
			}
			if (remainingUseTicks == getMaxUseTime(stack)) {
				PlayerLookup.tracking(user).forEach(foundPlayer -> PlayBrimstoneSoundPayload.send(foundPlayer, user.getId(), uuid[0]));
				if (user instanceof ServerPlayerEntity player) {
					PlayBrimstoneSoundPayload.send(player, user.getId(), uuid[0]);
				}
			}
		}
	}

	@ModifyReturnValue(method = "getPullTime", at = @At("RETURN"))
	private static int enchancement$brimstone(int original, ItemStack stack) {
		int level = EnchantmentHelper.getLevel(ModEnchantments.BRIMSTONE, stack);
		if (level > 0) {
			return (int) Math.max(1, original * 4.8F / level);
		}
		return original;
	}

	@ModifyReturnValue(method = "createArrowEntity", at = @At(value = "RETURN", ordinal = 1))
	private ProjectileEntity enchancement$brimstone(ProjectileEntity original, World world, LivingEntity shooter, ItemStack weaponStack, ItemStack projectileStack) {
		if (weaponStack.contains(ModDataComponentTypes.BRIMSTONE_DAMAGE)) {
			damage = weaponStack.get(ModDataComponentTypes.BRIMSTONE_DAMAGE);
			weaponStack.remove(ModDataComponentTypes.BRIMSTONE_DAMAGE);
			shooter.damage(ModDamageTypes.create(world, ModDamageTypes.LIFE_DRAIN), shooter.getMaxHealth() * (damage / 20F));
			BrimstoneEntity brimstone = new BrimstoneEntity(world, shooter);
			brimstone.setShotFromCrossbow(true);
			brimstone.setDamage(damage);
			brimstone.getDataTracker().set(BrimstoneEntity.FORCED_PITCH, shooter.getPitch());
			brimstone.getDataTracker().set(BrimstoneEntity.FORCED_YAW, shooter.getHeadYaw());
			if (shooter instanceof PlayerEntity player) {
				player.getItemCooldownManager().set(weaponStack.getItem(), (int) (getPullTime(weaponStack) * (damage / 12F)));
			}
			return brimstone;
		}
		return original;
	}

	@ModifyArg(method = "shoot", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;playSound(Lnet/minecraft/entity/player/PlayerEntity;DDDLnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FF)V"))
	private SoundEvent enchancement$brimstone(SoundEvent value) {
		if (damage >= 0) {
			SoundEvent sound = getFireSound(damage);
			damage = -1;
			return sound;
		}
		return value;
	}

	@Unique
	private static SoundEvent getFireSound(int damage) {
		if (damage >= 12) {
			return ModSoundEvents.ITEM_CROSSBOW_BRIMSTONE_6;
		} else if (damage >= 10) {
			return ModSoundEvents.ITEM_CROSSBOW_BRIMSTONE_5;
		} else if (damage >= 8) {
			return ModSoundEvents.ITEM_CROSSBOW_BRIMSTONE_4;
		} else if (damage >= 6) {
			return ModSoundEvents.ITEM_CROSSBOW_BRIMSTONE_3;
		} else if (damage >= 4) {
			return ModSoundEvents.ITEM_CROSSBOW_BRIMSTONE_2;
		}
		return ModSoundEvents.ITEM_CROSSBOW_BRIMSTONE_1;
	}
}
