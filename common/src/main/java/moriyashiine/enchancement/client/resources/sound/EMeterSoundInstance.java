package moriyashiine.enchancement.client.resources.sound;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype.EMeterComponent;
import moriyashiine.enchancement.common.init.EnchancementEntityComponents;
import moriyashiine.enchancement.common.init.EnchancementSoundEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

import java.util.UUID;

public class EMeterSoundInstance extends AbstractTickableSoundInstance {
	public static final Identifier ALTERNATE_ID = Enchancement.id("alternate_e_meter");

	private final Entity entity;
	private final UUID floatingUuid;

	public EMeterSoundInstance(Entity entity, UUID floatingUuid) {
		super(EnchancementSoundEvents.GENERIC_E_METER_FLOAT, entity.getSoundSource(), entity.getRandom());
		this.entity = entity;
		this.floatingUuid = floatingUuid;
		x = entity.getX();
		y = entity.getY();
		z = entity.getZ();
		looping = true;
		volume = 0.001F;
		if (Minecraft.getInstance().getResourcePackRepository().getSelectedPacks().stream().noneMatch(pack -> pack.getId().equals(ALTERNATE_ID.toString()))) {
			pitch = Mth.nextFloat(entity.getRandom(), 0.85F, 1.15F);
		}
	}

	@Override
	public void tick() {
		boolean done = entity == null || !entity.isAlive();
		if (!done) {
			EMeterComponent eMeter = EnchancementEntityComponents.E_METER.getNullable(entity);
			if (eMeter == null || !eMeter.isFloatingUuid(floatingUuid)) {
				done = true;
			}
		}
		if (done) {
			volume -= 1 / 6F;
			if (volume < 0) {
				stop();
			}
			return;
		} else if (volume < 1) {
			volume = Math.min(1, volume + 1 / 6F);
		}
		x = entity.getX();
		y = entity.getY();
		z = entity.getZ();
	}
}
