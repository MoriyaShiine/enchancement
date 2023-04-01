/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.common.registry;

import moriyashiine.enchancement.common.Enchancement;
import virtuoel.pehkui.api.*;

public class ModScaleTypes {
	public static ScaleType SLIDE_HITBOX_TYPE;
	public static ScaleModifier SLIDE_HITBOX_MODIFIER;

	public static void init() {
		SLIDE_HITBOX_MODIFIER = ScaleRegistries.register(ScaleRegistries.SCALE_MODIFIERS, Enchancement.id("slide_hitbox"), new TypedScaleModifier(() -> SLIDE_HITBOX_TYPE));
		ScaleTypes.HITBOX_WIDTH.getDefaultBaseValueModifiers().add(SLIDE_HITBOX_MODIFIER);
		ScaleTypes.HITBOX_HEIGHT.getDefaultBaseValueModifiers().add(SLIDE_HITBOX_MODIFIER);
		SLIDE_HITBOX_TYPE = ScaleRegistries.register(ScaleRegistries.SCALE_TYPES, Enchancement.id("slide_hitbox"), ScaleType.Builder.create().affectsDimensions().addDependentModifier(SLIDE_HITBOX_MODIFIER).build());
	}
}
