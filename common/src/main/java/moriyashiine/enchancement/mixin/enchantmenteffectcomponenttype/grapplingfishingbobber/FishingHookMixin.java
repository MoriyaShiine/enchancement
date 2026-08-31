package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.grapplingfishingbobber;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype.GrapplingFishingBobberComponent;
import moriyashiine.enchancement.common.init.EnchancementEntityComponents;
import moriyashiine.enchancement.common.init.EnchancementSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipBlockStateContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FishingHook.class)
public abstract class FishingHookMixin extends Projectile {
	@Shadow
	public abstract @Nullable Player getPlayerOwner();

	public FishingHookMixin(EntityType<? extends Projectile> type, Level level) {
		super(type, level);
	}

	@Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/FishingHook;checkCollision()V"), cancellable = true)
	private void enchancement$grapplingFishingBobber(CallbackInfo ci) {
		GrapplingFishingBobberComponent grapplingFishingBobber = EnchancementEntityComponents.GRAPPLING_FISHING_BOBBER.get(this);
		if (grapplingFishingBobber.getStrength() != 0) {
			if (grapplingFishingBobber.getGrappleState() != null) {
				setPos(grapplingFishingBobber.getGrapplePos());
				setDeltaMovement(Vec3.ZERO);
				if (tickCount % 10 == 0 && level().getBlockState(grapplingFishingBobber.getGrappleBlockPos()) != grapplingFishingBobber.getGrappleState()) {
					grapplingFishingBobber.setGrapplePos(null);
					grapplingFishingBobber.setGrappleBlockPos(null);
					grapplingFishingBobber.setGrappleState(null);
				}
				ci.cancel();
			} else {
				if (getDeltaMovement().horizontalDistanceSqr() == 0) {
					for (Direction direction : Direction.values()) {
						Vec3 offset = position().relative(direction, 0.2);
						BlockHitResult result = level().isBlockInLine(new ClipBlockStateContext(position(), offset, state -> !state.canBeReplaced()));
						if (!level().getBlockState(result.getBlockPos()).canBeReplaced()) {
							onHitBlock(result);
							return;
						}
					}
				}
			}
		}
	}

	@Inject(method = "shouldStopFishing", at = @At("HEAD"), cancellable = true)
	private void enchancement$grapplingFishingBobber(Player owner, CallbackInfoReturnable<Boolean> cir) {
		GrapplingFishingBobberComponent grapplingFishingBobber = EnchancementEntityComponents.GRAPPLING_FISHING_BOBBER.get(this);
		if (grapplingFishingBobber.getStrength() != 0) {
			if (owner.isRemoved() || !owner.isAlive() || !owner.getMainHandItem().is(Items.FISHING_ROD) && !owner.getOffhandItem().is(Items.FISHING_ROD) || distanceToSqr(owner) > 4096) {
				discard();
				cir.setReturnValue(true);
			}
			cir.setReturnValue(false);
		}
	}

	@Inject(method = "onHitBlock", at = @At("TAIL"))
	private void enchancement$grapplingFishingBobber(BlockHitResult hitResult, CallbackInfo ci) {
		GrapplingFishingBobberComponent grapplingFishingBobber = EnchancementEntityComponents.GRAPPLING_FISHING_BOBBER.get(this);
		if (grapplingFishingBobber.getStrength() != 0 && getPlayerOwner() instanceof Player player) {
			Vec3 hitPos = hitResult.getLocation().relative(hitResult.getDirection(), 0.01);
			BlockPos hitBlockPos = hitResult.getBlockPos();
			grapplingFishingBobber.setGrapplePos(hitPos);
			grapplingFishingBobber.setGrappleBlockPos(hitBlockPos);
			grapplingFishingBobber.setGrappleState(level().getBlockState(hitBlockPos));
			setPos(hitPos);
			setDeltaMovement(Vec3.ZERO);
			if (level().isClientSide()) {
				player.playSound(EnchancementSoundEvents.FISHING_BOBBER_GRAPPLE, 1, 1);
			}
		}
	}

	@Inject(method = "onHitEntity", at = @At("TAIL"))
	private void enchancement$grapplingFishingBobber(EntityHitResult hitResult, CallbackInfo ci) {
		GrapplingFishingBobberComponent grapplingFishingBobber = EnchancementEntityComponents.GRAPPLING_FISHING_BOBBER.get(this);
		if (grapplingFishingBobber.getStrength() != 0 && level().isClientSide()) {
			Player player = getPlayerOwner();
			if (player != null) {
				player.playSound(EnchancementSoundEvents.FISHING_BOBBER_GRAPPLE, 1, 1);
			}
		}
	}

	@ModifyArg(method = "pullEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/Vec3;scale(D)Lnet/minecraft/world/phys/Vec3;"))
	private double enchancement$grapplingFishingBobber(double value) {
		GrapplingFishingBobberComponent grapplingFishingBobber = EnchancementEntityComponents.GRAPPLING_FISHING_BOBBER.get(this);
		if (grapplingFishingBobber.getStrength() != 0) {
			return value * grapplingFishingBobber.getStrength();
		}
		return value;
	}

	@ModifyReturnValue(method = "retrieve", at = @At("RETURN"))
	private int enchancement$grapplingFishingBobber(int original) {
		GrapplingFishingBobberComponent grapplingFishingBobber = EnchancementEntityComponents.GRAPPLING_FISHING_BOBBER.get(this);
		if (grapplingFishingBobber.getStrength() != 0) {
			if (grapplingFishingBobber.getGrappleState() != null) {
				Player player = getPlayerOwner();
				if (player != null) {
					if (!level().isClientSide()) {
						if (getY() > player.getY()) {
							player.setDeltaMovement(player.getDeltaMovement().horizontal());
						}
						player.setDeltaMovement(player.getDeltaMovement().add(new Vec3(clamp(getX() - player.getX()), clamp(getY() - player.getY()), clamp(getZ() - player.getZ())).scale(0.2)));
						player.hurtMarked = true;
					}
				}
			}
			return level().isClientSide() ? original : 1;
		}
		return original;
	}

	@Unique
	private double clamp(double value) {
		GrapplingFishingBobberComponent grapplingFishingBobber = EnchancementEntityComponents.GRAPPLING_FISHING_BOBBER.get(this);
		float cap = grapplingFishingBobber.getStrength() * 4;
		return Mth.clamp(value, -cap, cap);
	}
}
