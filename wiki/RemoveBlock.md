# RemoveBlock
- Let's remove all blocks!
- Bukkit: `Spigot` 
- Type: `Team`
- API Version: `LATEST`
- Minecraft Version: `1.14+`

# How to play
- Remove all blocks with tools

# Play Video
- [Youtube](https://www.youtube.com/watch?v=FXTq3VUQ_l8)

# Config
```yaml
RemoveBlock:
  title: RemoveBlock
  min-player-count: 1
  max-player-count: 4
  waiting-time: 20
  play-time: 180
  active: true
  icon: STONE_PICKAXE
  view: true
  location:
    ==: org.bukkit.Location
    world: world
    x: 0.0
    y: 4.0
    z: 0.0
    pitch: 0.0
    yaw: 0.0
  tutorial:
  - 'Game Start: +180'
  - 'every 5 second: -1'
  - 'Remove ALL Blocks: Game End'
  custom-data:
    pos1:
      ==: org.bukkit.Location
      world: world
      x: 0.0
      y: 4.0
      z: 0.0
      pitch: 0.0
      yaw: 0.0
    pos2:
      ==: org.bukkit.Location
      world: world
      x: 0.0
      y: 4.0
      z: 0.0
      pitch: 0.0
      yaw: 0.0
    blocks:
    - COBWEB
    - OAK_WOOD
    - COBBLESTONE
    - DIRT
    - HAY_BLOCK
    chatting: true
    score-notifying: true
    block-break: false
    block-place: false
    pvp: false
    pve: true
    inventory-save: true
    minigame-respawn: true
    live-gamemode: SURVIVAL
    dead-gamemode: SPECTATOR
    color: RESET
    food-level-change: true
    player-hurt: true
```
- `custom-data.pos1`: Block area first position
- `custom-data.pos2`: Block area second position
- `custom-data.blocks`: Block types to fill the area

# Warning

- Before play, setup `custom-data.pos1` and `custom-data.pos2`