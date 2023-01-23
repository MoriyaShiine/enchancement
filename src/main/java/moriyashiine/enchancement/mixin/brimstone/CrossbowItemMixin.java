/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.enchancement.mixin.brimstone;

import moriyashiine.enchancement.client.packet.PlayBrimstoneSoundPacket;
import moriyashiine.enchancement.client.packet.StopBrimstoneSoundPacket;
import moriyashiine.enchancement.common.entity.projectile.BrimstoneEntity;
import moriyashiine.enchancement.common.registry.ModDamageSources;
import moriyashiine.enchancement.common.registry.ModEnchantments;
import moriyashiine.enchancement.common.registry.ModSoundEvents;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
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
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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
	private static boolean loadProjectiles(LivingEntity shooter, ItemStack projectile) {
		throw new UnsupportedOperationException();
	}

	@Inject(method = "onStoppedUsing", at = @At("HEAD"), cancellable = true)
	private void enchancement$brimstone(ItemStack stack, World world, LivingEntity user, int remainingUseTicks, CallbackInfo ci) {
		if (EnchancementUtil.hasEnchantment(ModEnchantments.BRIMSTONE, stack) && !CrossbowItem.isCharged(stack)) {
			if (!world.isClient) {
				UUID uuid = stack.getNbt().getUuid("BrimstoneUUID");
				PlayerLookup.tracking(user).forEach(foundPlayer -> StopBrimstoneSoundPacket.send(foundPlayer, uuid));
				if (user instanceof ServerPlayerEntity player) {
					StopBrimstoneSoundPacket.send(player, uuid);
				}
			}
			int damage = EnchancementUtil.getBrimstoneDamage(getPullProgress(getMaxUseTime(stack) - remainingUseTicks, stack));
			if (damage > 0 && loadProjectiles(user, stack)) {
				CrossbowItem.setCharged(stack, true);
				stack.getNbt().putInt("BrimstoneDamage", damage);
				world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ITEM_CROSSBOW_LOADING_END, user instanceof PlayerEntity ? SoundCategory.PLAYERS : SoundCategory.HOSTILE, 1, 1 / (world.getRandom().nextFloat() * 0.5F + 1) + 0.2F);
				ci.cancel();
			}
		}
	}

	@Inject(method = "usageTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentHelper;getLevel(Lnet/minecraft/enchantment/Enchantment;Lnet/minecraft/item/ItemStack;)I"))
	private void enchancement$brimstone(World world, LivingEntity user, ItemStack stack, int remainingUseTicks, CallbackInfo ci) {
		if (EnchancementUtil.hasEnchantment(ModEnchantments.BRIMSTONE, stack)) {
			UUID uuid;
			if (stack.getNbt().contains("BrimstoneUUID")) {
				uuid = stack.getNbt().getUuid("BrimstoneUUID");
			} else {
				uuid = UUID.randomUUID();
				stack.getNbt().putUuid("BrimstoneUUID", uuid);
			}
			if (remainingUseTicks == getMaxUseTime(stack)) {
				PlayerLookup.tracking(user).forEach(foundPlayer -> PlayBrimstoneSoundPacket.send(foundPlayer, user.getId(), uuid));
				if (user instanceof ServerPlayerEntity player) {
					PlayBrimstoneSoundPacket.send(player, user.getId(), uuid);
				}
			}
		}
	}

	@ModifyVariable(method = "createArrow", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/item/ArrowItem;createArrow(Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/LivingEntity;)Lnet/minecraft/entity/projectile/PersistentProjectileEntity;"))
	private static PersistentProjectileEntity enchancement$brimstone(PersistentProjectileEntity value, World world, LivingEntity entity, ItemStack crossbow, ItemStack arrow) {
		if (ItemStack.areEqual(arrow, EnchancementUtil.BRIMSTONE_STACK)) {
			damage = crossbow.getNbt().getInt("BrimstoneDamage");
			entity.timeUntilRegen = 0;
			entity.damage(ModDamageSources.LIFE_DRAIN, damage);
			BrimstoneEntity brimstone = new BrimstoneEntity(world, entity);
			brimstone.setDamage(damage);
			brimstone.getDataTracker().set(BrimstoneEntity.FORCED_PITCH, entity.getPitch());
			brimstone.getDataTracker().set(BrimstoneEntity.FORCED_YAW, entity.getHeadYaw());
			return brimstone;
		}
		return value;
	}

	@Inject(method = "getPullTime", at = @At("HEAD"), cancellable = true)
	private static void enchancement$brimstone(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
		if (EnchancementUtil.hasEnchantment(ModEnchantments.BRIMSTONE, stack)) {
			cir.setReturnValue(60);
		}
	}

	@ModifyArg(method = "shoot", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;playSound(Lnet/minecraft/entity/player/PlayerEntity;DDDLnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FF)V"))
	private static SoundEvent enchancement$brimstone(SoundEvent value) {
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
