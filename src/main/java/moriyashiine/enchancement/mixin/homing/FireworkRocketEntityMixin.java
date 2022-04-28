package moriyashiine.enchancement.mixin.homing;

import moriyashiine.enchancement.common.registry.ModEnchantments;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FireworkRocketEntity.class)
public abstract class FireworkRocketEntityMixin extends Entity {
	@Unique
	private static final TrackedData<ItemStack> STACK_SHOT_FROM = DataTracker.registerData(FireworkRocketEntity.class, TrackedDataHandlerRegistry.ITEM_STACK);
	@Unique
	private static final TrackedData<Boolean> HAS_HOMING = DataTracker.registerData(FireworkRocketEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

	@Shadow
	private int lifeTime;

	public FireworkRocketEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Inject(method = "<init>(Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/Entity;DDDZ)V", at = @At("TAIL"))
	private void enchancement$homing(World world, ItemStack stack, Entity entity, double x, double y, double z, boolean shotAtAngle, CallbackInfo ci) {
		if (entity instanceof LivingEntity living) {
			ItemStack shotFromStack = ItemStack.EMPTY;
			if (EnchancementUtil.hasEnchantment(ModEnchantments.HOMING, living.getMainHandStack())) {
				shotFromStack = living.getMainHandStack();
			} else if (EnchancementUtil.hasEnchantment(ModEnchantments.HOMING, living.getOffHandStack())) {
				shotFromStack = living.getOffHandStack();
			}
			if (!shotFromStack.isEmpty()) {
				getDataTracker().set(STACK_SHOT_FROM, shotFromStack);
				getDataTracker().set(HAS_HOMING, true);
			}
		}
	}

	@ModifyVariable(method = "tick", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/entity/projectile/FireworkRocketEntity;getVelocity()Lnet/minecraft/util/math/Vec3d;", ordinal = 1), ordinal = 0)
	private Vec3d enchancement$homing(Vec3d value) {
		if (age % 3 == 0 && getDataTracker().get(HAS_HOMING) && ((ProjectileEntityAccessor) this).enchancement$getOwner() instanceof LivingEntity living) {
			ItemStack stackShotFrom = getDataTracker().get(STACK_SHOT_FROM);
			if (living.getMainHandStack() == stackShotFrom || living.getOffHandStack() == stackShotFrom) {
				Vec3d eyePos = living.getEyePos();
				if (squaredDistanceTo(eyePos) < 4000) {
					BlockHitResult raycast = world.raycast(new RaycastContext(eyePos, eyePos.add(living.getRotationVector().multiply(64)), RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, living));
					if (raycast.getType() != HitResult.Type.MISS) {
						lifeTime += 3;
					}
					Vec3d target = raycast.getPos();
					lookAt(EntityAnchorArgumentType.EntityAnchor.EYES, target);
					return value.lerp(target.subtract(getPos()).normalize(), age % 60 / 60F);
				}
			}
		}
		return value;
	}

	@Inject(method = "initDataTracker", at = @At("TAIL"))
	private void enchancement$homing(CallbackInfo ci) {
		getDataTracker().startTracking(STACK_SHOT_FROM, ItemStack.EMPTY);
		getDataTracker().startTracking(HAS_HOMING, false);
	}
}
