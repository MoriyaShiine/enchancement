package moriyashiine.enchancement.common.registry;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.enchantment.EmptyEnchantment;
import moriyashiine.enchancement.common.enchantment.FrostbiteEnchantment;
import moriyashiine.enchancement.common.enchantment.MoltenEnchantment;
import moriyashiine.enchancement.common.enchantment.ShovelEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModEnchantments {
	public static final Enchantment EMPTY = new EmptyEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.ARMOR);

	public static final Enchantment ASSIMILATION = new EmptyEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.ARMOR_HEAD, EquipmentSlot.HEAD);
	public static final Enchantment BUFFET = new EmptyEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.ARMOR_HEAD, EquipmentSlot.HEAD);
	public static final Enchantment PERCEPTION = new EmptyEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.ARMOR_HEAD, EquipmentSlot.HEAD);

	public static final Enchantment AMPHIBIOUS = new EmptyEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.ARMOR_CHEST, EquipmentSlot.CHEST);

	public static final Enchantment ACCELERATION = new EmptyEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.ARMOR_LEGS, EquipmentSlot.LEGS);

	public static final Enchantment BOUNCY = new EmptyEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.ARMOR_FEET, EquipmentSlot.FEET);
	public static final Enchantment GALE = new EmptyEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.ARMOR_FEET, EquipmentSlot.FEET);

	public static final Enchantment FROSTBITE = new FrostbiteEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.WEAPON, EquipmentSlot.MAINHAND);

	public static final Enchantment CHAOS = new EmptyEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.BOW, EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND);
	public static final Enchantment DELAY = new EmptyEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.BOW, EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND);

	public static final Enchantment WARP = new EmptyEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.TRIDENT, EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND);

	public static final Enchantment DISARM = new EmptyEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.FISHING_ROD, EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND);
	public static final Enchantment GRAPPLE = new EmptyEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.FISHING_ROD, EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND);

	public static final Enchantment MOLTEN = new MoltenEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.DIGGER, EquipmentSlot.MAINHAND);

	public static final Enchantment SCOOPING = new ShovelEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.DIGGER, EquipmentSlot.MAINHAND);


	public static void init() {
		Registry.register(Registry.ENCHANTMENT, new Identifier(Enchancement.MOD_ID, "assimilation"), ASSIMILATION);
		Registry.register(Registry.ENCHANTMENT, new Identifier(Enchancement.MOD_ID, "buffet"), BUFFET);
		Registry.register(Registry.ENCHANTMENT, new Identifier(Enchancement.MOD_ID, "perception"), PERCEPTION);

		Registry.register(Registry.ENCHANTMENT, new Identifier(Enchancement.MOD_ID, "amphibious"), AMPHIBIOUS);

		Registry.register(Registry.ENCHANTMENT, new Identifier(Enchancement.MOD_ID, "acceleration"), ACCELERATION);

		Registry.register(Registry.ENCHANTMENT, new Identifier(Enchancement.MOD_ID, "bouncy"), BOUNCY);
		Registry.register(Registry.ENCHANTMENT, new Identifier(Enchancement.MOD_ID, "gale"), GALE);

		Registry.register(Registry.ENCHANTMENT, new Identifier(Enchancement.MOD_ID, "frostbite"), FROSTBITE);

		Registry.register(Registry.ENCHANTMENT, new Identifier(Enchancement.MOD_ID, "chaos"), CHAOS);
		Registry.register(Registry.ENCHANTMENT, new Identifier(Enchancement.MOD_ID, "delay"), DELAY);

		Registry.register(Registry.ENCHANTMENT, new Identifier(Enchancement.MOD_ID, "warp"), WARP);

		Registry.register(Registry.ENCHANTMENT, new Identifier(Enchancement.MOD_ID, "disarm"), DISARM);
		Registry.register(Registry.ENCHANTMENT, new Identifier(Enchancement.MOD_ID, "grapple"), GRAPPLE);

		Registry.register(Registry.ENCHANTMENT, new Identifier(Enchancement.MOD_ID, "molten"), MOLTEN);

		Registry.register(Registry.ENCHANTMENT, new Identifier(Enchancement.MOD_ID, "scooping"), SCOOPING);
	}
}
