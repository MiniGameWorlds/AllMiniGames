# HiddenArchers
- Like ninja
- Type: `TeamBattle`
- Bukkit: `Spigot` 
- API Version: `LATEST`
- Minecraft Versions: `1.14+`

# How to play
- Hit invisible player

# Play Video
- [Youtube](https://www.youtube.com/watch?v=qgSD38AAc6A)

# Config
```yaml
HiddenArcher:
  title: HiddenArcher
  min-players: 2
  max-players: 8
  waiting-time: 10
  play-time: 180
  active: true
  icon: BOW
  location:
    ==: org.bukkit.Location
    world: world
    x: 0.0
    y: 4.0
    z: 0.0
    pitch: 0.0
    yaw: 0.0
  tutorial:
  - After game starts, everyone will hide from others with snowballs
  - 'Hit by other: die'
  custom-data:
    reloadCoolDown: 3
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
    groupChat: false
    teamPvp: true
    teamRegisterMode: FAIR_FILL
```
- `custom-data.reloadCoolDown`: snowball reload cooldown (sec)

# Warning
- nothing