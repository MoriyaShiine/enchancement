/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.init;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.enchantment.effect.*;
import moriyashiine.enchancement.common.enchantment.effect.entity.*;
import moriyashiine.enchancement.common.lootcondition.AttackerBehindLootCondition;
import moriyashiine.enchancement.common.lootcondition.HasExtendedWaterTimeLootCondition;
import moriyashiine.enchancement.common.lootcondition.WetLootCondition;
import moriyashiine.enchancement.common.tag.ModDamageTypeTags;
import moriyashiine.enchancement.common.tag.ModEnchantmentTags;
import moriyashiine.enchancement.common.tag.ModStatusEffectTags;
import moriyashiine.enchancement.common.util.SubmersionGate;
import moriyashiine.enchancement.data.provider.ModEnchantmentTagProvider;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentLevelBasedValue;
import net.minecraft.enchantment.effect.AllOfEnchantmentEffects;
import net.minecraft.enchantment.effect.AttributeEnchantmentEffect;
import net.minecraft.enchantment.effect.EnchantmentEffectTarget;
import net.minecraft.enchantment.effect.entity.*;
import net.minecraft.enchantment.effect.value.AddEnchantmentEffect;
import net.minecraft.enchantment.effect.value.MultiplyEnchantmentEffect;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.loot.condition.AllOfLootCondition;
import net.minecraft.loot.condition.DamageSourcePropertiesLootCondition;
import net.minecraft.loot.condition.EntityPropertiesLootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.TagPredicate;
import net.minecraft.predicate.entity.*;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.floatprovider.ConstantFloatProvider;
import net.minecraft.util.math.floatprovider.UniformFloatProvider;

public class ModEnchantments {
	// placeholder
	public static final RegistryKey<Enchantment> EMPTY_KEY = createKey("empty");
	public static final Enchantment EMPTY = Enchantment.builder(Enchantment.definition(RegistryEntryList.empty(), 1, 1, Enchantment.constantCost(0), Enchantment.constantCost(0), 0)).build(EMPTY_KEY.getValue());
	// helmet
	public static final RegistryKey<Enchantment> ASSIMILATION = createKey("assimilation");
	public static final RegistryKey<Enchantment> PERCEPTION = createKey("perception");
	public static final RegistryKey<Enchantment> VEIL = createKey("veil");
	// chestplate
	public static final RegistryKey<Enchantment> ADRENALINE = createKey("adrenaline");
	public static final RegistryKey<Enchantment> AMPHIBIOUS = createKey("amphibious");
	public static final RegistryKey<Enchantment> STRAFE = createKey("strafe");
	public static final RegistryKey<Enchantment> WARDENSPINE = createKey("wardenspine");
	// leggings
	public static final RegistryKey<Enchantment> DASH = createKey("dash");
	public static final RegistryKey<Enchantment> GALE = createKey("gale");
	public static final RegistryKey<Enchantment> SLIDE = createKey("slide");
	// boots
	public static final RegistryKey<Enchantment> BOUNCY = createKey("bouncy");
	public static final RegistryKey<Enchantment> BUOY = createKey("buoy");
	public static final RegistryKey<Enchantment> STICKY = createKey("sticky");
	// sword
	public static final RegistryKey<Enchantment> BERSERK = createKey("berserk");
	public static final RegistryKey<Enchantment> FROSTBITE = createKey("frostbite");
	// bow
	public static final RegistryKey<Enchantment> CHAOS = createKey("chaos");
	public static final RegistryKey<Enchantment> DELAY = createKey("delay");
	public static final RegistryKey<Enchantment> PHASING = createKey("phasing");
	// crossbow
	public static final RegistryKey<Enchantment> BRIMSTONE = createKey("brimstone");
	public static final RegistryKey<Enchantment> SCATTER = createKey("scatter");
	public static final RegistryKey<Enchantment> TORCH = createKey("torch");
	// trident
	public static final RegistryKey<Enchantment> LEECH = createKey("leech");
	public static final RegistryKey<Enchantment> WARP = createKey("warp");
	// mace
	public static final RegistryKey<Enchantment> METEOR = createKey("meteor");
	public static final RegistryKey<Enchantment> THUNDERSTRUCK = createKey("thunderstruck");
	// mining tool
	public static final RegistryKey<Enchantment> MOLTEN = createKey("molten");
	// pickaxe
	public static final RegistryKey<Enchantment> EXTRACTING = createKey("extracting");
	// axe
	public static final RegistryKey<Enchantment> BEHEADING = createKey("beheading");
	public static final RegistryKey<Enchantment> LUMBERJACK = createKey("lumberjack");
	// shovel
	public static final RegistryKey<Enchantment> BURY = createKey("bury");
	public static final RegistryKey<Enchantment> SCOOPING = createKey("scooping");
	// fishing rod
	public static final RegistryKey<Enchantment> DISARM = createKey("disarm");
	public static final RegistryKey<Enchantment> GRAPPLE = createKey("grapple");

	private static RegistryKey<Enchantment> createKey(String id) {
		return RegistryKey.of(RegistryKeys.ENCHANTMENT, Enchancement.id(id));
	}

	public static Enchantment create(Identifier id, RegistryEntryList<Item> supportedItems, int maxLevel, AttributeModifierSlot slot, EffectsAdder effectsAdder) {
		if (FabricLoader.getInstance().isDevelopmentEnvironment() && !ModEnchantmentTagProvider.ALL_ENCHANCEMENT_ENCHANTMENTS.contains(id)) {
			ModEnchantmentTagProvider.ALL_ENCHANCEMENT_ENCHANTMENTS.add(id);
		}
		Enchantment.Builder builder = Enchantment.builder(Enchantment.definition(supportedItems, 5, maxLevel, Enchantment.leveledCost(5, 6), Enchantment.leveledCost(20, 6), 2, slot));
		effectsAdder.addEffects(builder);
		return builder.build(id);
	}

	public static void bootstrap(Registerable<Enchantment> registerable) {
		registerable.register(EMPTY_KEY, EMPTY);
		// lookup
		RegistryEntryLookup<DamageType> damageTypeLookup = registerable.getRegistryLookup(RegistryKeys.DAMAGE_TYPE);
		RegistryEntryLookup<Enchantment> enchantmentLookup = registerable.getRegistryLookup(RegistryKeys.ENCHANTMENT);
		RegistryEntryLookup<EntityType<?>> entityTypeLookup = registerable.getRegistryLookup(RegistryKeys.ENTITY_TYPE);
		RegistryEntryLookup<Item> itemLookup = registerable.getRegistryLookup(RegistryKeys.ITEM);
		// helmet
		registerable.register(ASSIMILATION, create(ASSIMILATION.getValue(),
				itemLookup.getOrThrow(ItemTags.HEAD_ARMOR_ENCHANTABLE),
				2,
				AttributeModifierSlot.HEAD,
				builder -> {
					builder.addNonListEffect(
							ModEnchantmentEffectComponentTypes.MODIFY_CONSUMPTION_TIME,
							new MultiplyEnchantmentEffect(EnchantmentLevelBasedValue.linear(0.875F, -0.125F)));
					builder.addEffect(
							EnchantmentEffectComponentTypes.TICK,
							new AutomateEatingEnchantmentEffect(NumberRange.IntRange.atMost(14)),
							EntityPropertiesLootCondition.builder(LootContext.EntityTarget.THIS, EntityPredicate.Builder.create().periodicTick(20)));
				}));
		registerable.register(PERCEPTION, create(PERCEPTION.getValue(),
				itemLookup.getOrThrow(ItemTags.HEAD_ARMOR_ENCHANTABLE),
				2,
				AttributeModifierSlot.HEAD,
				builder -> {
					builder.addNonListEffect(
							ModEnchantmentEffectComponentTypes.ENTITY_XRAY,
							new AddEnchantmentEffect(EnchantmentLevelBasedValue.linear(8)));
					builder.addNonListEffect(
							ModEnchantmentEffectComponentTypes.NIGHT_VISION,
							new AddEnchantmentEffect(EnchantmentLevelBasedValue.linear(0.25F, 0.75F)));
				}));
		registerable.register(VEIL, create(VEIL.getValue(),
				itemLookup.getOrThrow(ItemTags.HEAD_ARMOR_ENCHANTABLE),
				2,
				AttributeModifierSlot.HEAD,
				builder -> {
					builder.addEffect(
							ModEnchantmentEffectComponentTypes.HIDE_LABEL_BEHIND_WALLS);
					builder.addNonListEffect(
							ModEnchantmentEffectComponentTypes.MODIFY_DETECTION_RANGE,
							new MultiplyEnchantmentEffect(new EnchantmentLevelBasedValue.Fraction(EnchantmentLevelBasedValue.constant(1), EnchantmentLevelBasedValue.linear(2))));
				}));
		// chestplate
		registerable.register(ADRENALINE, create(ADRENALINE.getValue(),
				itemLookup.getOrThrow(ItemTags.CHEST_ARMOR_ENCHANTABLE),
				2,
				AttributeModifierSlot.CHEST,
				builder -> builder.addNonListEffect(
						ModEnchantmentEffectComponentTypes.RAGE,
						new RageEffect(
								new AddEnchantmentEffect(EnchantmentLevelBasedValue.constant(0)),
								new AddEnchantmentEffect(EnchantmentLevelBasedValue.linear(0.2F / 14)),
								new AddEnchantmentEffect(EnchantmentLevelBasedValue.linear(0.02857142857F))
						)
				)));
		registerable.register(AMPHIBIOUS, create(AMPHIBIOUS.getValue(),
				itemLookup.getOrThrow(ItemTags.CHEST_ARMOR_ENCHANTABLE),
				2,
				AttributeModifierSlot.CHEST,
				builder -> {
					builder.addEffect(
							ModEnchantmentEffectComponentTypes.EXTEND_WATER_TIME);
					builder.addEffect(
							ModEnchantmentEffectComponentTypes.EXTENDED_WATER_SPIN_ATTACK);
					builder.addEffect(
							EnchantmentEffectComponentTypes.TICK,
							ExtinguishEnchantmentEffect.INSTANCE,
							() -> HasExtendedWaterTimeLootCondition.INSTANCE);
					builder.addEffect(
							EnchantmentEffectComponentTypes.TICK,
							new SetExtendedWaterTimeEffect(
									EnchantmentLevelBasedValue.linear(6, 4)),
							() -> WetLootCondition.INSTANCE);
					builder.addEffect(
							EnchantmentEffectComponentTypes.ATTRIBUTES,
							new AttributeEnchantmentEffect(Enchancement.id("enchantment.amphibious"),
									EntityAttributes.BURNING_TIME,
									EnchantmentLevelBasedValue.linear(-0.25F),
									EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE));
					builder.addEffect(
							EnchantmentEffectComponentTypes.ATTRIBUTES,
							new AttributeEnchantmentEffect(Enchancement.id("enchantment.amphibious"),
									EntityAttributes.OXYGEN_BONUS,
									EnchantmentLevelBasedValue.linear(1.5F),
									EntityAttributeModifier.Operation.ADD_VALUE));
					builder.addEffect(
							EnchantmentEffectComponentTypes.ATTRIBUTES,
							new AttributeEnchantmentEffect(Enchancement.id("enchantment.amphibious"),
									EntityAttributes.SUBMERGED_MINING_SPEED,
									EnchantmentLevelBasedValue.linear(2),
									EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
					builder.addEffect(
							EnchantmentEffectComponentTypes.ATTRIBUTES,
							new AttributeEnchantmentEffect(Enchancement.id("enchantment.amphibious"),
									EntityAttributes.WATER_MOVEMENT_EFFICIENCY,
									EnchantmentLevelBasedValue.linear(0.5F),
									EntityAttributeModifier.Operation.ADD_VALUE));
				}));
		registerable.register(STRAFE, create(STRAFE.getValue(),
				itemLookup.getOrThrow(ItemTags.CHEST_ARMOR_ENCHANTABLE),
				2,
				AttributeModifierSlot.CHEST,
				builder -> builder.addNonListEffect(
						ModEnchantmentEffectComponentTypes.DIRECTION_BURST,
						new DirectionBurstEffect(
								new AddEnchantmentEffect(EnchantmentLevelBasedValue.linear(1.25F, -0.5F)),
								new AddEnchantmentEffect(EnchantmentLevelBasedValue.constant(1.1F)),
								new AddEnchantmentEffect(EnchantmentLevelBasedValue.constant(0.8F))))));
		registerable.register(WARDENSPINE, create(WARDENSPINE.getValue(),
				itemLookup.getOrThrow(ItemTags.CHEST_ARMOR_ENCHANTABLE),
				2,
				AttributeModifierSlot.CHEST,
				builder -> {
					builder.exclusiveSet(enchantmentLookup.getOrThrow(ModEnchantmentTags.WARDENSPINE_EXCLUSIVE_SET));
					builder.addEffect(
							EnchantmentEffectComponentTypes.DAMAGE_PROTECTION,
							new AddEnchantmentEffect(EnchantmentLevelBasedValue.linear(8)),
							AllOfLootCondition.builder(
									DamageSourcePropertiesLootCondition.builder(DamageSourcePredicate.Builder.create().tag(TagPredicate.unexpected(ModDamageTypeTags.BYPASSES_WARDENSPINE))),
									() -> AttackerBehindLootCondition.INSTANCE
							));
					builder.addEffect(
							EnchantmentEffectComponentTypes.POST_ATTACK,
							EnchantmentEffectTarget.VICTIM,
							EnchantmentEffectTarget.ATTACKER,
							AllOfEnchantmentEffects.allOf(
									new DamageEntityEnchantmentEffect(
											EnchantmentLevelBasedValue.linear(2),
											EnchantmentLevelBasedValue.linear(2),
											damageTypeLookup.getOrThrow(DamageTypes.THORNS)),
									new ApplyMobEffectEnchantmentEffect(
											RegistryEntryList.of(StatusEffects.DARKNESS),
											EnchantmentLevelBasedValue.linear(4),
											EnchantmentLevelBasedValue.linear(4),
											EnchantmentLevelBasedValue.constant(0),
											EnchantmentLevelBasedValue.constant(0)
									),
									new PlaySoundEnchantmentEffect(
											RegistryEntry.of(ModSoundEvents.ENTITY_GENERIC_WARDENSPINE),
											ConstantFloatProvider.create(1),
											ConstantFloatProvider.create(1)
									)
							),
							AllOfLootCondition.builder(
									DamageSourcePropertiesLootCondition.builder(DamageSourcePredicate.Builder.create().tag(TagPredicate.unexpected(ModDamageTypeTags.BYPASSES_WARDENSPINE))),
									() -> AttackerBehindLootCondition.INSTANCE
							));
				}));
		// leggings
		registerable.register(DASH, create(DASH.getValue(),
				itemLookup.getOrThrow(ItemTags.LEG_ARMOR_ENCHANTABLE),
				2,
				AttributeModifierSlot.LEGS,
				builder -> builder.addNonListEffect(
						ModEnchantmentEffectComponentTypes.ROTATION_BURST,
						new RotationBurstEffect(
								new AddEnchantmentEffect(EnchantmentLevelBasedValue.linear(1, -0.25F)),
								new AddEnchantmentEffect(EnchantmentLevelBasedValue.linear(0.85F, 0.2F)),
								new AddEnchantmentEffect(EnchantmentLevelBasedValue.constant(3)),
								new AddEnchantmentEffect(EnchantmentLevelBasedValue.linear(1.3F, -0.15F))))));
		registerable.register(GALE, create(GALE.getValue(),
				itemLookup.getOrThrow(ItemTags.LEG_ARMOR_ENCHANTABLE),
				2,
				AttributeModifierSlot.LEGS,
				builder -> builder.addNonListEffect(
						ModEnchantmentEffectComponentTypes.AIR_JUMP,
						new AirJumpEffect(
								new AddEnchantmentEffect(EnchantmentLevelBasedValue.linear(1)),
								new AddEnchantmentEffect(EnchantmentLevelBasedValue.constant(1.45F)),
								new AddEnchantmentEffect(EnchantmentLevelBasedValue.constant(0.5F)),
								new AddEnchantmentEffect(EnchantmentLevelBasedValue.constant(0.5F))))));
		registerable.register(SLIDE, create(SLIDE.getValue(),
				itemLookup.getOrThrow(ItemTags.LEG_ARMOR_ENCHANTABLE),
				2,
				AttributeModifierSlot.LEGS,
				builder -> {
					builder.addNonListEffect(
							ModEnchantmentEffectComponentTypes.SLAM,
							new AddEnchantmentEffect(EnchantmentLevelBasedValue.constant(0.5F)));
					builder.addNonListEffect(
							ModEnchantmentEffectComponentTypes.SLIDE,
							new AddEnchantmentEffect(EnchantmentLevelBasedValue.linear(0.21F, 0.065F)));
				}));
		// boots
		registerable.register(BOUNCY, create(BOUNCY.getValue(),
				itemLookup.getOrThrow(ItemTags.FOOT_ARMOR_ENCHANTABLE),
				2,
				AttributeModifierSlot.FEET,
				builder -> {
					builder.exclusiveSet(enchantmentLookup.getOrThrow(ModEnchantmentTags.BOUNCY_EXCLUSIVE_SET));
					builder.addEffect(
							ModEnchantmentEffectComponentTypes.BOUNCE);
					builder.addNonListEffect(
							ModEnchantmentEffectComponentTypes.CHARGE_JUMP,
							new ChargeJumpEffect(
									new AddEnchantmentEffect(EnchantmentLevelBasedValue.constant(2)),
									new AddEnchantmentEffect(EnchantmentLevelBasedValue.linear(0.6F, 0.4F))));
				}));
		registerable.register(BUOY, create(BUOY.getValue(),
				itemLookup.getOrThrow(ItemTags.FOOT_ARMOR_ENCHANTABLE),
				2,
				AttributeModifierSlot.FEET,
				builder -> {
					builder.addEffect(
							ModEnchantmentEffectComponentTypes.EXTEND_WATER_TIME);
					builder.addNonListEffect(
							ModEnchantmentEffectComponentTypes.BOOST_IN_FLUID,
							new AddEnchantmentEffect(EnchantmentLevelBasedValue.linear(1, 0.5F)));
					builder.addEffect(
							ModEnchantmentEffectComponentTypes.FLUID_WALKING);
					builder.addEffect(
							ModEnchantmentEffectComponentTypes.PREVENT_SWIMMING);
					builder.addNonListEffect(
							ModEnchantmentEffectComponentTypes.MODIFY_SUBMERGED_MOVEMENT_SPEED,
							new ModifySubmergedMovementSpeedEffect(
									new AddEnchantmentEffect(EnchantmentLevelBasedValue.linear(0.35F)),
									SubmersionGate.WATER_ONLY));
					builder.addEffect(
							EnchantmentEffectComponentTypes.TICK,
							new SetExtendedWaterTimeEffect(
									EnchantmentLevelBasedValue.linear(6, 4)),
							() -> WetLootCondition.INSTANCE);
					builder.addEffect(
							EnchantmentEffectComponentTypes.TICK,
							new ConditionalAttributeEnchantmentEffect(
									new AttributeEnchantmentEffect(Enchancement.id("enchantment.buoy"),
											EntityAttributes.STEP_HEIGHT,
											EnchantmentLevelBasedValue.constant(1),
											EntityAttributeModifier.Operation.ADD_VALUE),
									HasExtendedWaterTimeLootCondition.INSTANCE));
					builder.addEffect(
							EnchantmentEffectComponentTypes.TICK,
							new ConditionalAttributeEnchantmentEffect(
									new AttributeEnchantmentEffect(Enchancement.id("enchantment.buoy"),
											EntityAttributes.SAFE_FALL_DISTANCE,
											EnchantmentLevelBasedValue.linear(2),
											EntityAttributeModifier.Operation.ADD_VALUE),
									HasExtendedWaterTimeLootCondition.INSTANCE));
				}));
		registerable.register(STICKY, create(STICKY.getValue(),
				itemLookup.getOrThrow(ItemTags.FOOT_ARMOR_ENCHANTABLE),
				2,
				AttributeModifierSlot.FEET,
				builder -> {
					builder.addNonListEffect(
							ModEnchantmentEffectComponentTypes.HONEY_TRAIL,
							new AddEnchantmentEffect(EnchantmentLevelBasedValue.linear(1.5F))
					);
					builder.addNonListEffect(
							ModEnchantmentEffectComponentTypes.WALL_JUMP,
							new AddEnchantmentEffect(EnchantmentLevelBasedValue.linear(0.5F, 0.2F)));
				}));
		// sword
		registerable.register(BERSERK, create(BERSERK.getValue(),
				itemLookup.getOrThrow(ItemTags.SWORD_ENCHANTABLE),
				2,
				AttributeModifierSlot.MAINHAND,
				builder -> builder.addNonListEffect(
						ModEnchantmentEffectComponentTypes.RAGE,
						new RageEffect(
								new AddEnchantmentEffect(EnchantmentLevelBasedValue.linear(0.175F)),
								new AddEnchantmentEffect(EnchantmentLevelBasedValue.constant(0)),
								new AddEnchantmentEffect(EnchantmentLevelBasedValue.constant(0))
						)
				)));
		registerable.register(FROSTBITE, create(FROSTBITE.getValue(),
				itemLookup.getOrThrow(ItemTags.SWORD_ENCHANTABLE),
				2,
				AttributeModifierSlot.MAINHAND,
				builder -> {
					builder.exclusiveSet(enchantmentLookup.getOrThrow(ModEnchantmentTags.FROSTBITE_EXCLUSIVE_SET));
					builder.addEffect(
							EnchantmentEffectComponentTypes.POST_ATTACK,
							EnchantmentEffectTarget.ATTACKER,
							EnchantmentEffectTarget.VICTIM,
							new FreezeEnchantmentEffect(EnchantmentLevelBasedValue.linear(6)),
							DamageSourcePropertiesLootCondition.builder(DamageSourcePredicate.Builder.create().isDirect(true)));
				}));
		// bow
		registerable.register(CHAOS, create(CHAOS.getValue(),
				itemLookup.getOrThrow(ItemTags.BOW_ENCHANTABLE),
				2,
				AttributeModifierSlot.MAINHAND,
				builder -> builder.addEffect(
						ModEnchantmentEffectComponentTypes.APPLY_RANDOM_STATUS_EFFECT,
						new ApplyRandomStatusEffectEffect(
								new AddEnchantmentEffect(EnchantmentLevelBasedValue.linear(4)),
								ModStatusEffectTags.CHAOS_UNCHOOSABLE
						)
				)));
		registerable.register(DELAY, create(DELAY.getValue(),
				itemLookup.getOrThrow(ItemTags.BOW_ENCHANTABLE),
				2,
				AttributeModifierSlot.MAINHAND,
				builder -> builder.addEffect(
						ModEnchantmentEffectComponentTypes.DELAYED_LAUNCH,
						new DelayedLaunchEffect(
								new AddEnchantmentEffect(EnchantmentLevelBasedValue.constant(10)),
								new AddEnchantmentEffect(EnchantmentLevelBasedValue.constant(3)),
								new AddEnchantmentEffect(EnchantmentLevelBasedValue.linear(0.5F, 0.25F)),
								true
						)
				)));
		registerable.register(PHASING, create(PHASING.getValue(),
				itemLookup.getOrThrow(ItemTags.BOW_ENCHANTABLE),
				2,
				AttributeModifierSlot.MAINHAND,
				builder -> builder.addEffect(
						ModEnchantmentEffectComponentTypes.PHASE_THROUGH_BLOCKS_AND_FLOAT,
						new AddEnchantmentEffect(EnchantmentLevelBasedValue.linear(2, 1))
				)));
		// crossbow
		registerable.register(BRIMSTONE, create(BRIMSTONE.getValue(),
				itemLookup.getOrThrow(ItemTags.CROSSBOW_ENCHANTABLE),
				2,
				AttributeModifierSlot.MAINHAND,
				builder -> {
					builder.exclusiveSet(enchantmentLookup.getOrThrow(ModEnchantmentTags.BRIMSTONE_EXCLUSIVE_SET));
					builder.addEffect(
							ModEnchantmentEffectComponentTypes.ALLOW_CROSSBOW_COOLDOWN_RELOADING);
					builder.addNonListEffect(
							ModEnchantmentEffectComponentTypes.BRIMSTONE,
							new BrimstoneEffect(
									new AddEnchantmentEffect(EnchantmentLevelBasedValue.linear(0.5F))
							));
				}));
		registerable.register(SCATTER, create(SCATTER.getValue(),
				itemLookup.getOrThrow(ItemTags.CROSSBOW_ENCHANTABLE),
				2,
				AttributeModifierSlot.MAINHAND,
				builder -> {
					builder.addEffect(
							ModEnchantmentEffectComponentTypes.ALLOW_CROSSBOW_COOLDOWN_RELOADING);
					builder.addEffect(
							ModEnchantmentEffectComponentTypes.ALLOW_LOADING_PROJECTILE,
							new AllowLoadingProjectileEffect(
									Enchancement.id("crossbow_amethyst"),
									Items.AMETHYST_SHARD,
									false
							)
					);
					builder.addEffect(
							ModEnchantmentEffectComponentTypes.SCATTER_SHOT,
							new ScatterShotEffect(
									new AddEnchantmentEffect(EnchantmentLevelBasedValue.linear(6)),
									new AddEnchantmentEffect(EnchantmentLevelBasedValue.linear(8)),
									Items.AMETHYST_SHARD
							)
					);
				}));
		registerable.register(TORCH, create(TORCH.getValue(),
				itemLookup.getOrThrow(ItemTags.CROSSBOW_ENCHANTABLE),
				2,
				AttributeModifierSlot.MAINHAND,
				builder -> {
					builder.addEffect(
							ModEnchantmentEffectComponentTypes.ALLOW_LOADING_PROJECTILE,
							new AllowLoadingProjectileEffect(
									Enchancement.id("crossbow_torch"),
									Items.TORCH,
									true
							)
					);
					builder.addEffect(
							ModEnchantmentEffectComponentTypes.RAPID_CROSSBOW_FIRE);
				}));
		// trident
		registerable.register(LEECH, create(LEECH.getValue(),
				itemLookup.getOrThrow(ItemTags.TRIDENT_ENCHANTABLE),
				2,
				AttributeModifierSlot.MAINHAND,
				builder -> {
					builder.addEffect(
							ModEnchantmentEffectComponentTypes.ALLOW_INTERRUPTION
					);
					builder.addNonListEffect(
							ModEnchantmentEffectComponentTypes.MULTIPLY_CHARGE_TIME,
							new MultiplyEnchantmentEffect(EnchantmentLevelBasedValue.linear(3, -1))
					);
					builder.addEffect(
							ModEnchantmentEffectComponentTypes.LEECHING_TRIDENT,
							new LeechingTridentEffect(
									new AddEnchantmentEffect(EnchantmentLevelBasedValue.constant(1)),
									new AddEnchantmentEffect(EnchantmentLevelBasedValue.constant(1)),
									new AddEnchantmentEffect(EnchantmentLevelBasedValue.linear(3))
							));
					builder.addEffect(
							EnchantmentEffectComponentTypes.POST_ATTACK,
							EnchantmentEffectTarget.ATTACKER,
							EnchantmentEffectTarget.ATTACKER,
							new HealEnchantmentEffect(EnchantmentLevelBasedValue.linear(0.5F)),
							DamageSourcePropertiesLootCondition.builder(DamageSourcePredicate.Builder.create().isDirect(true)));
					builder.addEffect(
							EnchantmentEffectComponentTypes.POST_ATTACK,
							EnchantmentEffectTarget.ATTACKER,
							EnchantmentEffectTarget.VICTIM,
							new SpawnParticlesEnchantmentEffect(
									ParticleTypes.DAMAGE_INDICATOR,
									SpawnParticlesEnchantmentEffect.entityPosition(0.5F),
									SpawnParticlesEnchantmentEffect.entityPosition(0.5F),
									SpawnParticlesEnchantmentEffect.scaledVelocity(0),
									SpawnParticlesEnchantmentEffect.scaledVelocity(0),
									ConstantFloatProvider.create(0)
							),
							DamageSourcePropertiesLootCondition.builder(DamageSourcePredicate.Builder.create().isDirect(true)));
				}));
		registerable.register(WARP, create(WARP.getValue(),
				itemLookup.getOrThrow(ItemTags.TRIDENT_ENCHANTABLE),
				2,
				AttributeModifierSlot.MAINHAND,
				builder -> {
					builder.addEffect(
							ModEnchantmentEffectComponentTypes.ALLOW_INTERRUPTION
					);
					builder.addNonListEffect(
							ModEnchantmentEffectComponentTypes.MULTIPLY_CHARGE_TIME,
							new MultiplyEnchantmentEffect(EnchantmentLevelBasedValue.linear(3, -1))
					);
					builder.addEffect(
							ModEnchantmentEffectComponentTypes.TELEPORT_ON_HIT,
							new TeleportOnHitEffect(
									true,
									false)
					);
				}));
		// mace
		registerable.register(METEOR, create(METEOR.getValue(),
				itemLookup.getOrThrow(ItemTags.MACE_ENCHANTABLE),
				2,
				AttributeModifierSlot.MAINHAND,
				builder -> {
					builder.exclusiveSet(enchantmentLookup.getOrThrow(ModEnchantmentTags.MACE_EXCLUSIVE_SET));
					builder.addEffect(
							ModEnchantmentEffectComponentTypes.ALLOW_INTERRUPTION
					);
					builder.addNonListEffect(
							ModEnchantmentEffectComponentTypes.ERUPTION,
							new EruptionEffect(
									new AddEnchantmentEffect(EnchantmentLevelBasedValue.constant(0.5F)),
									new AddEnchantmentEffect(EnchantmentLevelBasedValue.constant(1.35F)),
									new AddEnchantmentEffect(EnchantmentLevelBasedValue.linear(4))
							)
					);
					builder.addNonListEffect(
							ModEnchantmentEffectComponentTypes.MULTIPLY_CHARGE_TIME,
							new MultiplyEnchantmentEffect(EnchantmentLevelBasedValue.linear(4, -1))
					);
					builder.addEffect(
							EnchantmentEffectComponentTypes.POST_ATTACK,
							EnchantmentEffectTarget.ATTACKER,
							EnchantmentEffectTarget.VICTIM,
							AllOfEnchantmentEffects.allOf(
									BuryEffect.INSTANCE,
									new IgniteEnchantmentEffect(EnchantmentLevelBasedValue.linear(6)),
									new SmashEffect(EnchantmentLevelBasedValue.linear(2.5F)),
									new SpawnParticlesWithCountEnchantmentEffect(
											new SpawnParticlesEnchantmentEffect(
													ParticleTypes.LAVA,
													SpawnParticlesEnchantmentEffect.entityPosition(0.5F),
													SpawnParticlesEnchantmentEffect.entityPosition(0.5F),
													SpawnParticlesEnchantmentEffect.fixedVelocity(UniformFloatProvider.create(-1, 1)),
													SpawnParticlesEnchantmentEffect.scaledVelocity(1),
													ConstantFloatProvider.create(1)
											),
											EnchantmentLevelBasedValue.constant(48)
									)
							),
							EntityPropertiesLootCondition.builder(
									LootContext.EntityTarget.DIRECT_ATTACKER,
									EntityPredicate.Builder.create()
											.flags(EntityFlagsPredicate.Builder.create().flying(false))
											.movement(MovementPredicate.fallDistance(NumberRange.DoubleRange.atLeast(1.5)))
							));
				}));
		registerable.register(THUNDERSTRUCK, create(THUNDERSTRUCK.getValue(),
				itemLookup.getOrThrow(ItemTags.MACE_ENCHANTABLE),
				2,
				AttributeModifierSlot.MAINHAND,
				builder -> {
					builder.exclusiveSet(enchantmentLookup.getOrThrow(ModEnchantmentTags.MACE_EXCLUSIVE_SET));
					builder.addEffect(
							ModEnchantmentEffectComponentTypes.ALLOW_INTERRUPTION
					);
					builder.addEffect(
							ModEnchantmentEffectComponentTypes.CHAIN_LIGHTNING,
							new AddEnchantmentEffect(EnchantmentLevelBasedValue.linear(0.35F))
					);
					builder.addNonListEffect(
							ModEnchantmentEffectComponentTypes.MULTIPLY_CHARGE_TIME,
							new MultiplyEnchantmentEffect(EnchantmentLevelBasedValue.linear(4, -1))
					);
					builder.addNonListEffect(
							ModEnchantmentEffectComponentTypes.LIGHTNING_DASH,
							new LightningDashEffect(
									new AddEnchantmentEffect(EnchantmentLevelBasedValue.constant(0.5F)),
									new AddEnchantmentEffect(EnchantmentLevelBasedValue.constant(3)),
									new AddEnchantmentEffect(EnchantmentLevelBasedValue.linear(0.8F, 0.3F)),
									new AddEnchantmentEffect(EnchantmentLevelBasedValue.linear(1)),
									new AddEnchantmentEffect(EnchantmentLevelBasedValue.linear(0.7F, 0.3F))
							));
				}));
		// mining tool
		registerable.register(MOLTEN, create(MOLTEN.getValue(),
				itemLookup.getOrThrow(ItemTags.MINING_ENCHANTABLE),
				1,
				AttributeModifierSlot.MAINHAND,
				builder -> {
					builder.exclusiveSet(enchantmentLookup.getOrThrow(ModEnchantmentTags.SILK_TOUCH_EXCLUSIVE_SET));
					builder.addEffect(
							ModEnchantmentEffectComponentTypes.SMELT_MINED_BLOCKS);
				}));
		// pickaxe
		registerable.register(EXTRACTING, create(EXTRACTING.getValue(),
				itemLookup.getOrThrow(ItemTags.PICKAXES),
				2,
				AttributeModifierSlot.MAINHAND,
				builder -> {
					builder.exclusiveSet(enchantmentLookup.getOrThrow(ModEnchantmentTags.SILK_TOUCH_EXCLUSIVE_SET));
					builder.addNonListEffect(
							ModEnchantmentEffectComponentTypes.MINE_ORE_VEINS,
							new AddEnchantmentEffect(EnchantmentLevelBasedValue.linear(0.5F)));
				}));
		// axe
		registerable.register(BEHEADING, create(BEHEADING.getValue(),
				itemLookup.getOrThrow(ItemTags.AXES),
				2,
				AttributeModifierSlot.MAINHAND,
				builder -> builder.addEffect(
						ModEnchantmentEffectComponentTypes.HEAD_DROPS,
						EnchantmentEffectTarget.ATTACKER,
						EnchantmentEffectTarget.VICTIM,
						new AddEnchantmentEffect(EnchantmentLevelBasedValue.linear(0.5F)),
						EntityPropertiesLootCondition.builder(LootContext.EntityTarget.ATTACKER, EntityPredicate.Builder.create().type(EntityTypePredicate.create(entityTypeLookup, EntityType.PLAYER)))
				)));
		registerable.register(LUMBERJACK, create(LUMBERJACK.getValue(),
				itemLookup.getOrThrow(ItemTags.AXES),
				2,
				AttributeModifierSlot.MAINHAND,
				builder -> builder.addNonListEffect(
						ModEnchantmentEffectComponentTypes.FELL_TREES,
						new AddEnchantmentEffect(EnchantmentLevelBasedValue.linear(0.25F)))));
		// shovel
		registerable.register(BURY, create(BURY.getValue(),
				itemLookup.getOrThrow(ItemTags.SHOVELS),
				2,
				AttributeModifierSlot.MAINHAND,
				builder -> builder.addEffect(
						ModEnchantmentEffectComponentTypes.BURY_ENTITY,
						new AddEnchantmentEffect(EnchantmentLevelBasedValue.linear(16, -8)))));
		registerable.register(SCOOPING, create(SCOOPING.getValue(),
				itemLookup.getOrThrow(ItemTags.SHOVELS),
				4,
				AttributeModifierSlot.MAINHAND,
				builder -> {
					builder.addEffect(
							EnchantmentEffectComponentTypes.DAMAGE,
							new AddEnchantmentEffect(EnchantmentLevelBasedValue.linear(0.5F)));
					builder.addEffect(
							EnchantmentEffectComponentTypes.EQUIPMENT_DROPS,
							EnchantmentEffectTarget.ATTACKER,
							EnchantmentEffectTarget.VICTIM,
							new AddEnchantmentEffect(EnchantmentLevelBasedValue.linear(0.01F)),
							EntityPropertiesLootCondition.builder(LootContext.EntityTarget.ATTACKER, EntityPredicate.Builder.create().type(EntityTypePredicate.create(entityTypeLookup, EntityType.PLAYER))));
				}));
		// fishing rod
		registerable.register(DISARM, create(DISARM.getValue(),
				itemLookup.getOrThrow(ItemTags.FISHING_ENCHANTABLE),
				2,
				AttributeModifierSlot.MAINHAND,
				builder -> builder.addEffect(
						ModEnchantmentEffectComponentTypes.DISARMING_FISHING_BOBBER,
						new DisarmingFishingBobberEffect(
								false,
								new AddEnchantmentEffect(EnchantmentLevelBasedValue.linear(1.5F))
						))));
		registerable.register(GRAPPLE, create(GRAPPLE.getValue(),
				itemLookup.getOrThrow(ItemTags.FISHING_ENCHANTABLE),
				2,
				AttributeModifierSlot.MAINHAND,
				builder -> builder.addEffect(
						ModEnchantmentEffectComponentTypes.GRAPPLING_FISHING_BOBBER,
						new AddEnchantmentEffect(EnchantmentLevelBasedValue.linear(1)))));
	}

	public interface EffectsAdder {
		void addEffects(Enchantment.Builder builder);
	}
}
