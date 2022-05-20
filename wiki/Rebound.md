# Rebound
- Shoot others in zero gravity with rebound(recoil)
- Bukkit: `Spigot`
- Type: `SoloBattle`
- API Version: `LATEST`
- Minecraft Version: `1.14+`



# How to play
- Shoot others in zero gravity
- Arrow gives recoil(rebound) to shooter and hit player
- Player who has highest score will be glow

# Play Video
- [Youtube](https://www.youtube.com/watch?v=5HrHjrckS38)



# Config
```yaml
Rebound:
  title: Rebound
  min-players: 2
  max-players: 20
  waiting-time: 20
  play-time: 180
  active: true
  icon: DISPENSER
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
  - Shoot others in zero gravity
  - Arrow gives recoil(rebound) to shooter and hit player
  - Player who has highest score will be glow
  custom-data:
    shoot-cooldown: 1.0
    shoot-power: 1.0
    reverse-mode: false
    projectile-gravity: true
    chat: true
    score-notifying: true
    block-break: false
    block-place: false
    pvp: false
    pve: false
    inventory-save: true
    minigame-respawn: true
    live-gamemode: SURVIVAL
    dead-gamemode: SPECTATOR
    color: AQUA
    food-level-change: true
    player-hurt: false
```
- `shoot-cooldown`: shoot cooldown per second (decimal)
- `shoot-power`: shoot power (decimal)
- `reverse-mode`: if true, rebound will be reversed (true / false)
- `projectile-gravity`: if false, arrow has zero gravity (true / false)



# Warning
- Nothing