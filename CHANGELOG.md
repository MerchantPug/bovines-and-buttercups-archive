## Content
- Reworked the Ranch structure.
  - Overhaul the structure to make it appear more ranch-like.
  - Added more ranches that you can find in your world, outside of the flower forest.
  - There is only one chest for general loot, that way players who don't care about Mooblooms can still benefit from the structure.

Honestly, the ranches were one of the weakest parts of the mod, because most people just didn't understand the point of them being a structure that teaches the player to breed moobloom types.
I also did not like that they'd give biome specifics outside of the biome, and to mention the above, people sorta saw it moreso as free loot, rather than to maybe try something out with them.
The ranches now forgo the breeding loot table, and the more functional look, to give it more of a distinctly ranch-y appearance, and they spawn in multiple biomes because the player might think "hey, I found something similar in the Flower Forest, let's try bringing Mooblooms here."

## Internal
- Renamed `bovinesandbuttercups:chests/ranch_general` loot table to `bovinesandbuttercups:chests/ranch`.
- Removed `bovinesandbuttercups:chests/ranch_breeding` and sub loot tables.
- Split `bovinesandbuttercups:ranch` structure into a few structures, found in the `bovinesandbuttercups/ranch` subfolder
- Split the `bovinesandbuttercups:has_structure/ranch` biome tag into a few tags for each structure.
- Added new `#bovinesandbuttercups:ranch` structure tag.

## Bugfixes
- [FABRIC] Fixed a crash when attempting to add mooblooms to flower forests.