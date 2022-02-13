# FallingItem
- Let's pickup items falling from the sky!
- Bukkit: `Spigot` 
- Type: `SoloBattle`
- API Version: `LATEST`
- Minecraft Version: `1.14+`



# How to play
- Pickup items with different scores
- Items will be removed after a while



# Play Video
- [Youtube]()



# Config
```yaml
FallingItem:
  title: FallingItem
  min-player-count: 2
  max-player-count: 10
  waiting-time: 20
  time-limit: 120
  active: true
  icon: FEATHER
  view: true
  scoreboard: true
  location:
    ==: org.bukkit.Location
    world: world
    x: 0.0
    y: 4.0
    z: 0.0
    pitch: 0.0
    yaw: 0.0
  tutorial:
  - Pick up items fallen from the sky!
  custom-data:
    items:
      IRON_INGOT: 2
      DIAMOND: 4
      COAL: 1
      TNT: -3
      GOLD_INGOT: 3
    spawn-delay: 1.0
    spawn-pos1:
      ==: org.bukkit.Location
      world: world
      x: 0.0
      y: 4.0
      z: 0.0
      pitch: 0.0
      yaw: 0.0
    spawn-pos2:
      ==: org.bukkit.Location
      world: world
      x: 0.0
      y: 4.0
      z: 0.0
      pitch: 0.0
      yaw: 0.0
    finish-score: 50
    item-live-time: 10
    chat: true
    score-notifying: true
    block-break: false
    block-place: false
    pvp: true
    pve: true
    inventory-save: true
    minigame-respawn: true
    live-gamemode: SURVIVAL
    dead-gamemode: SPECTATOR
    color: BLUE
    food-level-change: false
    player-hurt: true
```
- `items`: Items with different scores (If want apple with 10 score, add `APPLE: 10`)
- `spawn-delay`: Item spawn delay (second (can include decimals))
- `spawn-pos1`: First position of items drop 
- `spawn-pos2`: Second position of items drop
- `finish-score`: If a player reach `finish-score`, game will finish
- `item-live-time`: Item will be remove after `item-live-item` seconds (sec)





# Warning
- Setup `spawn-pos1` and `spawn-pos2` before play, because items will drop between the two positions