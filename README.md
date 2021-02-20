# Minecraft 1.12.2 Radar Mod
Radar mod made using the Forge API in Minecraft 1.12.2

A list of currently implemented features:

## Blocks

Block|Description
-------|---------
Basic Radar|Emits a redstone signal when there are players in a 3 chunk radius
Advanced Radar|Emits a redstone signal when there are players in a 10 chunk radius

:: Right click a radar to add yourself to the whitelist<br>
:: Shift + right click a radar to show who's currently on the whitelist
<br>

## Items

Item|Description
-------|---------
Tracking Core|Crafting component
Handheld Scanner|Right click to scan the current chunk for players<br>Uses 1 durability
Player Tracker| Right click on a player to start tracking them<br>Right click again to get your current distance away from them<br>Can only track in the same dimension<br>Uses 1 Tracing Core per use
Handheld Jammer|Keep in your inventory to prevent the Player Tracker from tracking you<br>Players can still bind the Player Tracker to you, but finding your distance won't work
