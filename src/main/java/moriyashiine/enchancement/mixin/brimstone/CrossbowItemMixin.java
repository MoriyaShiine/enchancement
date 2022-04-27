package moriyashiine.enchancement.mixin.brimstone;

import moriyashiine.enchancement.common.entity.projectile.BrimstoneEntity;
import moriyashiine.enchancement.common.registry.ModSoundEvents;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(CrossbowItem.class)
public class CrossbowItemMixin {
	@Unique
	private static boolean playBrimstoneSound = false;

	@ModifyVariable(method = "createArrow", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/item/ArrowItem;createArrow(Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/LivingEntity;)Lnet/minecraft/entity/projectile/PersistentProjectileEntity;"))
	private static PersistentProjectileEntity enchancement$brimstone(PersistentProjectileEntity value, World world, LivingEntity entity, ItemStack crossbow, ItemStack arrow) {
		if (ItemStack.areEqual(arrow, EnchancementUtil.BRIMSTONE_STACK)) {
			playBrimstoneSound = true;
			entity.timeUntilRegen = 0;
			entity.damage(DamageSource.WITHER, 2);
			BrimstoneEntity brimstone = new BrimstoneEntity(world, entity);
			brimstone.getDataTracker().set(BrimstoneEntity.FORCED_PITCH, entity.getPitch());
			brimstone.getDataTracker().set(BrimstoneEntity.FORCED_YAW, entity.getHeadYaw());
			return brimstone;
		}
		return value;
	}

	@ModifyArg(method = "shoot", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;playSound(Lnet/minecraft/entity/player/PlayerEntity;DDDLnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FF)V"))
	private static SoundEvent enchancement$brimstone(SoundEvent value) {
		if (playBrimstoneSound) {
			playBrimstoneSound = false;
			return ModSoundEvents.ENTITY_BRIMSTONE_FIRE;
		}
		return value;
	}
}
