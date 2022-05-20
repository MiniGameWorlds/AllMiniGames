# Parkour
- Parkour with event blocks and find the finish block to get score!
- Bukkit: `Spigot`
- Type: `SoloBattle`
- API Version: `LATEST`
- Minecraft Version: `1.14+`


# How to play
- Find and step on the finish block!
- Many event blocks exist: FINISH, RESPAWN, UP-TP, DOWN-TP, FLICKER, HEAL, JUMP, DEBUFF
## Event blocks
- `finish`: score +1, teleport to spawn
- `respawn`: teleport to spawn
- `up-teleport`: up-teleport to 3 block
- `down-teleport`: down-teleport to 3 block
- `flicker`: disappear after 3 seconds, and appear after 3 seconds
- `heal`: heal player's health, hunger and remove all potion effects
- `jump`: jump to the sky
- `debuff`: give random debuff potion effect
## Endless
- `custom-data.endless` option
- > If true, player who reached the finish block will teleport to the spawn with +1 score
- > If false, game will be finished when any player reached the finish block 


# Play Video
- [Youtube](https://youtu.be/61dIXzXI7ME)

# Config
```yaml
Parkour:
  title: Parkour
  min-players: 2
  max-players: 10
  waiting-time: 15
  play-time: 300
  active: true
  icon: LILY_PAD
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
  - Find and step on the finish block!
  - 'Many event blocks exist: FINISH, RESPAWN, UP-TP, DOWN-TP, FLICKER, HEAL, JUMP,
    DEBUFF'
  custom-data:
    endless: false
    event-blocks:
      respawn: ORANGE_STAINED_GLASS
      down-teleport: LIGHT_GRAY_STAINED_GLASS
      flicker: LIGHT_BLUE_STAINED_GLASS
      up-teleport: GRAY_STAINED_GLASS
      debuff: MAGENTA_STAINED_GLASS
      heal: PINK_STAINED_GLASS
      finish: GLOWSTONE
      jump: WHITE_STAINED_GLASS
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
- `custom-data.endless`: (true / false)
#### custom-data.event-blocks
- `finish`: (Block Material)
- `respawn`: (Block Material)
- `up-teleport`: (Block Material)
- `down-teleport`: (Block Material)
- `flicker`: (Block Material)
- `heal`: (Block Material)
- `jump`: (Block Material)
- `debuff`: (Block Material)

# Warning
- **NEVER** shut down your server before all `FLICKER` blokcs appear again (at least 6 seconds)
- There can be multiple `finish` blocks