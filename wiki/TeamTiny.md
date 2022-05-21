# TeamTiny
- Hit the mob with members!
- Bukkit: `Spigot` 
- Type: `Team`
- API Version: `LATEST`
- Minecraft Version: `1.14+`

# How to play
- `Hit mob`: +1

# Play Video
- [Youtube](https://www.youtube.com/watch?v=ZQE_WaW3VTA&t=3s)

# Config
```yaml
TeamTiny:
  title: TeamTiny
  min-players: 1
  max-players: 10
  waiting-time: 20
  play-time: 60
  active: true
  icon: STONE_BUTTON
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
  - 'Hit mob: ยงa+1'
  custom-data:
    mob: BAT
    shootDelay: 1
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
    color: GRAY
    food-level-change: true
    player-hurt: true
```
- `mob`: Mob type to hit for score

# Warning

- Build minigame map to keep mob from running away