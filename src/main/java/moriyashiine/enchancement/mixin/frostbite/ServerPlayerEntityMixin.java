package moriyashiine.enchancement.mixin.frostbite;

import com.mojang.authlib.GameProfile;
import moriyashiine.enchancement.common.entity.mob.FrozenPlayerEntity;
import moriyashiine.enchancement.common.registry.ModEntityComponents;
import moriyashiine.enchancement.common.registry.ModEntityTypes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity {
	public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile profile) {
		super(world, pos, yaw, profile);
	}

	@Inject(method = "onDeath", at = @At("TAIL"))
	private void enchancement$frostbite(DamageSource source, CallbackInfo ci) {
		if (ModEntityComponents.FROZEN.get(this).shouldFreezeOnDeath(source)) {
			FrozenPlayerEntity frozenPlayer = ModEntityTypes.FROZEN_PLAYER.create(world);
			if (frozenPlayer != null) {
				frozenPlayer.setCustomName(getName());
				frozenPlayer.setPersistent();
				frozenPlayer.headYaw = headYaw;
				frozenPlayer.bodyYaw = bodyYaw;
				frozenPlayer.setPitch(getPitch());
				frozenPlayer.limbDistance = limbDistance;
				frozenPlayer.limbAngle = limbAngle;
				frozenPlayer.getDataTracker().set(FrozenPlayerEntity.SLIM, (getUuid().hashCode() & 1) == 1);
				frozenPlayer.teleport(getX(), getY(), getZ());
				ModEntityComponents.FROZEN.get(frozenPlayer).freeze();
				world.spawnEntity(frozenPlayer);
			}
		}
	}
}
