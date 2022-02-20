# Center
- Push other players with only body!
- Bukkit: `Spigot`
- Type: `SoloBattle`
- API Version: `LATEST`
- Minecraft Version: `1.14+`



# How to play
- Never seank!
- Never fall!
- Push other players with body!




# Play Video
- [Youtube](https://youtu.be/qqDl5FUvpEE)



# Config
```yaml
Center:
  title: Center
  min-player-count: 2
  max-player-count: 5
  waiting-time: 20
  time-limit: 120
  active: true
  icon: END_ROD
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
  - §cNever Sneak!
  - §cNever FALL!
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
    color: BLUE
    food-level-change: true
    player-hurt: true
```



# Warning
- **Y** of `location` is the height of check for player falling