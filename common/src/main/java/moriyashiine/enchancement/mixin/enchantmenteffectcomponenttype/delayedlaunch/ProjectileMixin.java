package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.delayedlaunch;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype.DelayedLaunchComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Projectile.class)
public abstract class ProjectileMixin extends Entity {
	@Shadow
	public abstract @Nullable Entity getOwner();

	public ProjectileMixin(EntityType<?> type, Level level) {
		super(type, level);
	}

	@WrapMethod(method = "shoot")
	private void enchancement$delayedLaunch(double xd, double yd, double zd, float pow, float uncertainty, Operation<Void> original) {
		if (getOwner() instanceof LivingEntity owner) {
			DelayedLaunchComponent.maybeSet(owner, owner.getActiveItem(), this, pow, uncertainty);
		}
		original.call(xd, yd, zd, pow, uncertainty);
	}
}
