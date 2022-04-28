package moriyashiine.enchancement.common.registry;

import moriyashiine.enchancement.common.Enchancement;
import net.minecraft.entity.damage.DamageSource;

public class ModDamageSources {
	public static final DamageSource LIFE_DRAIN = new ModDamageSource(Enchancement.MOD_ID + ".life_drain").setBypassesArmor();

	private static class ModDamageSource extends DamageSource {
		protected ModDamageSource(String name) {
			super(name);
		}

		@Override
		public DamageSource setBypassesArmor() {
			return super.setBypassesArmor();
		}
	}
}
