# Enchancement
Enchancement is a mod that aims to completely overhaul vanilla Minecraft's enchanting system, along with some other adjacent mechanics.

Enchancement currently adds 29 unique and fun enchantments to the game:

```
Helmet Enchantments:
* Assimilation: Food is automatically eaten whenever it would not be wasted. Having food in the off hand will only check that food. Food and drinks are consumed 50% faster.
* Perception: Grants increased dark visibility and allows entities to be seen through walls within 16 blocks.
* Veil: Hostile entities need to be four times closer to detect you. Your name will not be visible through walls.

Chestplate Enchantments:
* Amphibious: Increases movement speed and mining speed while under water, extends underwater breath, and halves time spent on fire.
* Strafe: Double tapping the sprint key will push you in your movement direction. Also increases air mobility.
* Wardenspine: Damage taken from behind will be reduced by 80% and harm the attacker.

Leggings Enchantments:
* Dash: Sneaking in the air will boost you in the direction you are looking. Jumping immediately after dashing will grant a large horizontal boost.
* Slide: Sprinting will now instead cause you to slide constantly. Jumping during a slide will launch you horizontally. Sneaking in the air will cause you to slam towards the ground, knocking back entities and negating fall damage.

Boots Enchantments:
* Buoy: You can walk on fluids, and sneak to descend. Jumping while submerged will rapidly propel you upwards. Prevents floating.
* Bouncy: Landing will bounce you back up at a speed proportional to your fall speed while not sneaking. Can charge a jump boost while sneaking. Also negates fall damage.
* Gale: Allows you to jump two more times in the air.

Sword Enchantments:
* Berserk: Deals up to 50% more damage at a rate of 0.5 damage per heart missing.
* Frostbite: Slows hit entities and freezes them on death. Frozen entities can be moved or shattered, launching ice shards that can damage and freeze other entities.

Bow Enchantments:
* Chaos: Normal arrows will be given a random negative potion effect, or positive if sneaking.
* Delay: Arrows will not launch until after 15 seconds or when punching with the bow. Arrows build up to 2.5x damage during the first 5 seconds of delay.
* Phasing: Arrows have no gravity and will phase through up to 3 blocks, upon which they will regain gravity.

Crossbow Enchantments:
* Brimstone: Fires a piercing laser that ignores armor and deals more damage the longer the crossbow was charged and the further away the target is. Uses hearts instead of arrows.
* Scatter: Amethyst Shards can be loaded into the crossbow via the off hand. Shards explode into a burst of projectiles upon firing.
* Torch: Torches can be rapidly loaded into the crossbow via the off hand, and will ignite hit entities for 2 seconds. Shot torches will be placed on the ground if shot while sneaking.

Trident Enchantments:
* Leech: Heals half of a heart on melee attack, and latches onto entities on ranged attack for 6 seconds, damaging them and healing you for half of a heart per second.
* Warp: Teleports you to the location of the trident if it didn't hit an entity.

Pickaxe Enchantments:
* Extracting: Extracts materials from connected ores, leaving the stone behind. Disabled while sneaking.
* Molten: Smelts all blocks mined. Disabled while sneaking.

Axe Enchantments:
* Beheading: Slain entities have a chance to drop their head.
* Lumberjack: Connected logs will also be destroyed. Disabled while sneaking.

Shovel Enchantments:
* Bury: Interacting with an entity will bury it in the ground if the block below is soft enough.
* Scooping: The shovel will deal 1 extra heart of damage and will cause mobs to drop more loot when killed.

Fishing Rod Enchantments:
* Disarm: Steals or disables a latched entity's held item.
* Grapple: The fishing hook can latch onto blocks. When releasing, you will be launched toward that block. Pulling entities applies more velocity.
```

Of the core changes, this mod will:
* Overhaul the Enchanting Table with a new UI that removes the RNG involved in enchanting; you may simply choose the enchantments you wish to have.
* Force items to only allow one enchantment.
* Remove leveling from enchantments, meaning that the max level for all enchantments will be 1. Enchantments that are brought down to this level will act as their original max level counterpart, an example being Riptide I will function the same as Riptide III.
* Remove durability from most aspects of the game. There is an item tag to allow certain items to keep durability.
* Remove most vanilla enchantments from the game. Enchancement tries to make choosing your enchantments more fun and engaging by replacing many of the boring stat change enchantments with enchantments that grant the player unique abilities.

Enchancement also adds some quality of life changes:
* Infinity is now allowed on Crossbows.
* All Tridents will have Loyalty built in as a native effect.
* Channeling will now work during any weather, not just when thundering. Channeling will also create lightning that does not create fire or destroy items, and will ignite entities for 3 seconds on melee attack.
* Fire Aspect is now usable as Flint and Steel if you are sneaking.
* Enchanted Books are free to merge with items in an Anvil.
* Luck of the Sea acts as if the item also has Lure.
* Ender Pearls no longer deal damage upon landing.
* Tridents will return from the void if thrown down there.
* Makes a select few potions a bit weaker to account for the removal of some enchantments.
* Reduces the saturation gained and Regeneration level given from Golden Apples.
* Weapon enchantments such as Fire Aspect or Frostbite will not function unless the player's attack cooldown is >= 70%.

**Q: Can I disable any of these changes?**

A: Everything in the mod is configurable down to disabling the entire mod if there are features that you wish to disable. All of the config options are self explanatory except for unbreakingChangesFlag (0 means all items are unbreakable, greater than 0 means items with x level of Unbreaking are unbreakable, and less than 0 means disabled).

**Q: Why did you remove most of the vanilla enchantments?**

A: I do not like how much of the vanilla enchanting meta relies exclusively on Protection, Sharpness, Power, and other such stat increasing enchantments. I also dislike Fortune because it incentivizes not mining ores until you have Fortune, which I find incredibly dumb.

**Q: I want to keep every enchantment anyway. How do I do this?**

A: In the config file, remove all entries from allowedEnchantments and set invertedList to true. Keep in mind that while this shouldn't cause any bugs, any balance concerns created by doing this will not be addressed as it is outside of the scope of the mod's vision.
