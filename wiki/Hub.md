# Hub
- Teleport to the Hub
- Bukkit: `Spigot`
- Type: `Fake`
- API Version: `LATEST`
- Minecraft Version: `1.14+`



# How to play
- Player will teleport to the `location`



# Play Video
- [Youtube](https://youtu.be/8BLEKaSAu-U)



# Config
```yaml
Hub:
  title: Hub
  min-players: 1
  max-players: 1
  waiting-time: 1
  play-time: 1
  active: true
  icon: EMERALD_BLOCK
  view: true
  scoreboard: true
  location:
    ==: org.bukkit.Location
    world: world
    x: 0.0
    y: 0.0
    z: 0.0
    pitch: 0.0
    yaw: 0.0
  tutorial:
  - ''
  custom-data:
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
    color: GREEN
    food-level-change: true
    player-hurt: true
```
- `location`: hub spawn location

# Warning
- This is fake minigame, not real minigame. Just use for teleportation