/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.enchancement.mixin.frostbite;

import com.mojang.authlib.GameProfile;
import moriyashiine.enchancement.client.packet.SyncFrozenPlayerSlimStatusS2C;
import moriyashiine.enchancement.common.entity.mob.FrozenPlayerEntity;
import moriyashiine.enchancement.common.registry.ModEntityComponents;
import moriyashiine.enchancement.common.registry.ModEntityTypes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.encryption.PlayerPublicKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity {
	public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile, @Nullable PlayerPublicKey publicKey) {
		super(world, pos, yaw, gameProfile, publicKey);
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
				frozenPlayer.teleport(getX(), getY(), getZ());
				ModEntityComponents.FROZEN.get(frozenPlayer).freeze();
				SyncFrozenPlayerSlimStatusS2C.send(ServerPlayerEntity.class.cast(this), frozenPlayer.getUuid());
				world.spawnEntity(frozenPlayer);
			}
		}
	}
}
