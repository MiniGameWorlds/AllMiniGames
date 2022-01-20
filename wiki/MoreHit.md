# MoreHit
- Just hit!
- Type: `TeamBattle`
- Available Bukkit: `Spigot` <!--  Write bukkit, If event of minigame is only available in specific bukkit-->
- API Version: `LATEST`
- Available Minecraft Versions: `1.14+`

# How to play
- Hit other team player to get score

# Play Video
- [Youtube](https://www.youtube.com/watch?v=LhD347CTv2o)

# Config
```yaml
MoreHit:
  title: MoreHit
  min-player-count: 2
  max-player-count: 6
  waiting-time: 10
  time-limit: 20
  active: true
  icon: STICK
  location:
    ==: org.bukkit.Location
    world: world
    x: 0.0
    y: 4.0
    z: 0.0
    pitch: 0.0
    yaw: 0.0
  tutorial:
  - 'Hit other team member: +1'
  custom-data:
    chatting: true
    score-notifying: true
    block-break: false
    block-place: false
    pvp: true
    pve: true
    inventory-save: true
    minigame-respawn: true
    live-gamemode: SURVIVAL
    dead-gamemode: SPECTATOR
    color: RESET
    groupChat: true
    teamPvp: true
    teamRegisterMode: NONE
```

# Warning
- nothing