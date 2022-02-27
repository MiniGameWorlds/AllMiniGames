# Dropper
- Drop and get scores!
- Bukkit: `Spigot` 
- Type: `SoloBattle`
- API Version: `LATEST`
- Minecraft Version: `1.14+`



# How to play
- Players will get scores if step on the `score-blocks`
- Players will respawn to `location` if step on the `respawn-block`
- If a player died, respawn to `location`



# Play Video
- [Youtube](https://youtu.be/uWyEfxu35-k)



# Config
```yaml
Dropper:
  title: Dropper
  min-player-count: 2
  max-player-count: 10
  waiting-time: 20
  time-limit: 60
  active: true
  icon: OAK_TRAPDOOR
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
  - Drop and get scores!
  custom-data:
    score-blocks:
      BLUE_WOOL: 1
      GREEN_WOOL: 2
      YELLOW_WOOL: 3
      ORANGE_WOOL: 4
      RED_WOOL: 5
      BLACK_WOOL: -3
    respawn-block: GLASS
    health: 1
    chat: true
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
- `score-blocks`: Blocks with scores
- `respawn-block`: Respawn block
- `health`: All players will have maximum `health` when the game starts



# Warning
- Fill bottom of minigame map with `respawn-block` to make players respwan when hit the ground