package moriyashiine.enchancement.common.component.entity.config;

import moriyashiine.enchancement.common.init.EnchancementEntityComponents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent;

public class IgnitedComponent implements AutoSyncedComponent, ServerTickingComponent {
	private final LivingEntity obj;
	private boolean ignited = false, ignoreFireResistance = false;
	private int syncTicks = 0;

	public IgnitedComponent(LivingEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readData(ValueInput input) {
		ignited = input.getBooleanOr("Ignited", false);
		ignoreFireResistance = input.getBooleanOr("IgnoreFireResistance", false);
		syncTicks = input.getIntOr("SyncTicks", 0);
	}

	@Override
	public void writeData(ValueOutput output) {
		output.putBoolean("Ignited", ignited);
		output.putBoolean("IgnoreFireResistance", ignoreFireResistance);
		output.putInt("SyncTicks", syncTicks);
	}

	@Override
	public void serverTick() {
		if (ignited && obj.getRemainingFireTicks() <= 0) {
			ignited = ignoreFireResistance = false;
			syncTicks = 3;
		}
		if (syncTicks > 0 && --syncTicks == 0) {
			sync();
		}
	}

	public void sync() {
		EnchancementEntityComponents.IGNITED.sync(obj);
	}

	public boolean isIgnited() {
		return ignited;
	}

	public void markIgnited() {
		ignited = true;
		sync();
	}

	public boolean ignoreFireResistance() {
		return ignoreFireResistance;
	}

	public void alternateIgnoreFireResistance() {
		ignoreFireResistance = !ignoreFireResistance;
	}
}
