------------------------------------------------------
Version 1.21.4-r6
------------------------------------------------------
- merge https://github.com/MoriyaShiine/enchancement/pull/231
- add apex hoe enchantment
- - "Deals extra damage. Attacking from a distance always results in a critical hit."
- rebalance hoe stats to have less attack speed, more attack damage, and a bit of attack reach
- assimilation no longer automatically eats food while in combat
- armadillos now get scared from sliding players
- slightly adjust velocity for slide jumping and using meteor
- channeling now chains lightning on melee and ranged attack
- meteor and thunderstruck now charge faster
- buff thunderstruck range and damage
- fix thunderstruck sometimes damaging you anyway even if you hit an entity
- using fall damage negating enchantments now cancels thunderstruck
- fix wind burst always granting fall immunity
- remove impaled enchanting material overrides since the mod is dead
- enforce config match packet now runs every 5 seconds
- merge freeEnchantingBookMerging config into overhaulEnchantingTable config
- fix level inconsistencies with item renaming
- fix arrow dupe when piercing is enabled
- tipped arrows now convert into regular arrows when dropping as an item after hitting an entity
- fishing bobbers now pull the root vehicle of hooked entities

------------------------------------------------------
Version 1.21.4-r5
------------------------------------------------------
- fix https://github.com/MoriyaShiine/enchancement/issues/229
- fix infinite dash exploit
- fix meteor ignoring bury conditions
- sticky wall jump fall reduction is now dependent on the strength of the jump

------------------------------------------------------
Version 1.21.4-r4
------------------------------------------------------
- massively optimize projectile loading model replacement rendering

------------------------------------------------------
Version 1.21.4-r3
------------------------------------------------------
- merge https://github.com/MoriyaShiine/enchancement/pull/222
- fix https://github.com/MoriyaShiine/enchancement/issues/223
- fix https://github.com/MoriyaShiine/enchancement/issues/227
- fix https://github.com/MoriyaShiine/enchancement/issues/228
- fix trailing lolcat comma
- fix desyncs with several enchantments
- fix slam sometimes not negating fall damage
- fix bounce sound not playing
- fix brimstone not triggering entity killed advancements
- fix rare crash with fell trees component combined with other tree felling mods
- migrate inverted bounce status to component
- projectiles that bypass armor now have much greater damage falloff when bypassing invulnerability frames
- assimilation no longer eats disallowed food while in offhand
- amphibious now reduces wet timer when using riptide by 3 seconds
- dash is now slightly weaker
- gale no longer automatically activates when holding jump
- gale now allows you to briefly glide when holding jump
- sliding and slamming now have visual indicators
- bounce now charges slightly faster
- buoy now negates fall damage when landing on any fluid
- buoy now reduces fire time from lava (15 -> 2 seconds)
- chaos now works with all persistent projectiles
- riptide is now slightly stronger with single level mode enabled
- riptide no longer has water drag
- scatter can now be interrupted
- bosses can no longer be disarmed
- maces now deal more damage per block fallen
- animal armor can now be enchanted with boots enchantments
- enchanted armor now has additional damage reduction

------------------------------------------------------
Version 1.21.4-r2
------------------------------------------------------
- merge https://github.com/MoriyaShiine/enchancement/pull/215
- fix https://github.com/MoriyaShiine/enchancement/issues/220

------------------------------------------------------
Version 1.21.4-r1
------------------------------------------------------
- update to 1.21.4

------------------------------------------------------
Version 1.21.2-r6
------------------------------------------------------
- fix brimstone not working when backed up against a block
- disarm now has a cooldown on the user when successfully used, and now applies cooldown on the target for longer
- disarm now prioritizes active item before mainhand and offhand
- grapple no longer increases bouncy strength
- slamming is now canceled by honey effects
- split scatter shot cooldown into own component

------------------------------------------------------
Version 1.21.2-r5
------------------------------------------------------
- fix mod projectiles not rendering from certain angles
- brimstone and life drain damage sources now bypass resistance
- increase slide speed
- movement enchantments now scale properly with attribute modifiers such as from swiftness or slowness

------------------------------------------------------
Version 1.21.2-r4
------------------------------------------------------
- merge https://github.com/MoriyaShiine/enchancement/pull/208
- merge https://github.com/MoriyaShiine/enchancement/pull/209
- fix https://github.com/MoriyaShiine/enchancement/issues/206
- code cleanup

------------------------------------------------------
Version 1.21.2-r3
------------------------------------------------------
- merge https://github.com/MoriyaShiine/enchancement/pull/205
- honey trail duration now scales with level
- slamming is now canceled by flying, gliding, riding a vehicle, or climbing

------------------------------------------------------
Version 1.21.2-r2
------------------------------------------------------
- merge https://github.com/MoriyaShiine/enchancement/pull/204
- enchantments with empty description translations no longer attempt to render
- overhauled enchanting table descriptions now append a dash to be consistent with item descriptions

------------------------------------------------------
Version 1.21.2-r1
------------------------------------------------------
- update to 1.21.2
- fix https://github.com/MoriyaShiine/enchancement/issues/203

------------------------------------------------------
Version 1.21-r12
------------------------------------------------------
- fix eruption effect fire applying for significantly longer than intended
- fix `enchancement:always_selectable` enchantment tag not functioning
- add non-treasure overhaul mode config option
- apply random status effect now requires less charge for positive effects
- over-overkill is now more difficult to obtain
- localize default enchanting material item tag

------------------------------------------------------
Version 1.21-r11
------------------------------------------------------
- perception now works with shaders and true darkness

------------------------------------------------------
Version 1.21-r10
------------------------------------------------------
- fix https://github.com/MoriyaShiine/enchancement/issues/202

------------------------------------------------------
Version 1.21-r9
------------------------------------------------------
- fix crash with empty stacks in enchanting table
- add default enchanting material
- strafe now only requires one press to activate by default
- add inverted bounce option
- rename some config options, keybindings, and components
- update Russian translation (thanks BackupCup!)
- update Turkish translation (thanks Hexasan!)

------------------------------------------------------
Version 1.21-r8
------------------------------------------------------
- fix https://github.com/MoriyaShiine/enchancement/issues/201
- fix frozen guardians acting weird
- sticky no longer makes (and now clears) honey spots in fluids

------------------------------------------------------
Version 1.21-r7
------------------------------------------------------
- fix instant damage and instant health being more potent than expected when applied without a tipped arrow

------------------------------------------------------
Version 1.21-r6
------------------------------------------------------
- fix modify detection range crash

------------------------------------------------------
Version 1.21-r5
------------------------------------------------------
- fix unbound value and missing key crashes
- projectile damage falloff now applies to tipped arrows with instant health and instant damage

------------------------------------------------------
Version 1.21-r4
------------------------------------------------------
- don't crash when encountering an enchantment without a translation key

------------------------------------------------------
Version 1.21-r3
------------------------------------------------------
- fix vaults crashing and add cleaner replacement logic
- add extracting compat for diamond in the rough, malum, nature's spirit, and spelunkery

------------------------------------------------------
Version 1.21-r2
------------------------------------------------------
- merge https://github.com/MoriyaShiine/enchancement/pull/199
- fix multiply charge time component with porting lib
- sticky can no longer slide on ice

------------------------------------------------------
Version 1.21-r1
------------------------------------------------------
- update to 1.21
- fix https://github.com/MoriyaShiine/enchancement/issues/184
- update russian translation (thanks BackupCup!)
- rebalance a significant amount of the mod, please reread the readme/description and try things out! the following list contains some of the major changes
- add sticky enchantment
- add meteor enchantment
- add thunderstruck enchantment
- molten can now be applied to all mining tools
- adrenaline is now a chestplate enchantment
- gale is now a leggings enchantment
- brimstone is no longer hitscan and can be heard/seen approaching
- projectiles are now shot faster and take entity velocity into account to artificially increase target hitboxes
- condense many config options to reduce clutter

------------------------------------------------------
Version 1.20.6-r7
------------------------------------------------------
- fix https://github.com/MoriyaShiine/enchancement/issues/180

------------------------------------------------------
Version 1.20.6-r6
------------------------------------------------------
- slide no longer sets velocity and instead adds velocity; cobwebs, ice, and other blocks now affect sliding
- slide now skips on water if you have buoy
- slamming is now followed by sliding if holding slide key and not holding jump key
- slamming on pointed dripstone now instantly kills you
- increase sliding camera height slightly
- fix step height modifiers stacking

------------------------------------------------------
Version 1.20.6-r5
------------------------------------------------------
- merge https://github.com/MoriyaShiine/enchancement/pull/190
- fix https://github.com/MoriyaShiine/enchancement/issues/189
- extended water (amphibious/buoy) now lasts for 10 seconds instead of 8
- extended water is now applied from all potions, not just splash water, and lasts for 30 seconds
- scatter in creative mode now automatically charges with amethyst shards

------------------------------------------------------
Version 1.20.6-r4
------------------------------------------------------
- merge https://github.com/MoriyaShiine/enchancement/pull/185
- merge https://github.com/MoriyaShiine/enchancement/pull/186
- merge https://github.com/MoriyaShiine/enchancement/pull/187
- merge https://github.com/MoriyaShiine/enchancement/pull/188
- add enchancement:always_selectable enchantment tag
- rename enchancement:unselectable enchantment tag to enchancement:never_selectable
- add mace enchantment descriptions
- allowTreasureEnchantmentsInEnchantingTable is now set to true by default
- fix creative crash with enchantments added by feature flags
- fix scatter not having a cooldown
- fix sweeping_edge enchantment description key

------------------------------------------------------
Version 1.20.6-r3
------------------------------------------------------
- fix renaming items in an anvil removing enchantments

------------------------------------------------------
Version 1.20.6-r2
------------------------------------------------------
- wolf armor now has durability

------------------------------------------------------
Version 1.20.6-r1
------------------------------------------------------
- update to 1.20.6
- refactor some config options
- add toggles for chestplate air mobility and trident loyalty + remove efficiency enchantment and make it toggleable for all enchanted tools
- lumberjack now modifies mining speed depending on the size of the tree
- gale now cycles between 16 different colors if you have higher levels
- mod projectiles can no longer hit entities currently being ridden by the user