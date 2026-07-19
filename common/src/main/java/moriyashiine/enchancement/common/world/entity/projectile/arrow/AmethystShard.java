package moriyashiine.enchancement.common.world.entity.projectile.arrow;

import moriyashiine.enchancement.common.init.EnchancementDamageTypes;
import moriyashiine.enchancement.common.init.EnchancementEntityTypes;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;

public class AmethystShard extends ShardEntity {
	private static final ParticleOptions PARTICLE = new ItemParticleOption(ParticleTypes.ITEM, Items.AMETHYST_SHARD);

	public AmethystShard(EntityType<AmethystShard> type, Level level) {
		super(type, level);
	}

	public AmethystShard(Level level, LivingEntity mob, @Nullable ItemStack shotFrom) {
		super(EnchancementEntityTypes.AMETHYST_SHARD, mob, level, shotFrom);
	}

	@Override
	protected ParticleOptions getParticleEffect() {
		return PARTICLE;
	}

	@Override
	protected ResourceKey<DamageType> getDamageType() {
		return EnchancementDamageTypes.AMETHYST_SHARD;
	}

	@Override
	protected void onTargetHit(Entity entity) {
		if (isOnFire()) {
			entity.igniteForSeconds(5);
		}
	}

	@Override
	public void shoot(double x, double y, double z, float power, float uncertainty) {
		super.shoot(x, y, z, power / 2, uncertainty);
	}
}
