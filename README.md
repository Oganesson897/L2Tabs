# L2Tabs

This mod add inventory tabs that displays in survival inventory or curio inventory. 
It contains:
- The player inventory
- Player attribute page
- Curios page

## Installation
Requirements:
- Requires L2Serial for serialization, automatic packet
- Requires L2Library for datapack config sync, auto menu layout.

Compatibility: Supports Curios

## Builtin Pages
### Player Attribue Page
This page contains a list of vanilla and forge player attributes, and
more attributes can be added to the list through datapacks using 
`data/<modid>/l2tabs_config/attribute_entry/`

When you hover onto a line, it will show calculation details about how you
get the final value, by showing all modifiers.
### Curios Page
This page is an expanded version of the Curios inventory menu: it is capable
of showing up to 54 slots, instead of merely 8 slots provided by Curios.

## New Tab Registration

New tabs can be registered by calling `TabRegistry.registerTabs`.
The registration requires title and item icon, but they can be overrided by the
tab itself. Tab registration can only happen in enqueued block in `FMLClientSetupEvent`.