# HiddenArchers
- Like ninja
- Type: `TeamBattle`
- Available Bukkit: `Spigot` <!--  Write bukkit, If event of minigame is only available in specific bukkit-->
- API Version: `LATEST`
- Available Minecraft Versions: `1.14+`

# How to play
- Hit invisible player

# Play Video
- [Youtube](https://www.youtube.com/watch?v=qgSD38AAc6A)

# Config
```yaml
HiddenArcher:
  title: HiddenArcher
  min-player-count: 2
  max-player-count: 8
  waiting-time: 10
  time-limit: 180
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