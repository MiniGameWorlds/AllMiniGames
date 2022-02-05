# PassMob
- Pass mobs to the next!
- Type: `TeamBattle`
- Bukkit: `Spigot` <!--  Write bukkit, If event of minigame is only available in specific bukkit-->
- API Version: `LATEST`
- Minecraft Versions: `1.14+`

# How to play
- Kill mobs to pass to the other team
- Winner team is the team that have fewer mobs than another team
  
# Play Video
- [Youtube](https://www.youtube.com/watch?v=dH6G-9Q7Als)


# Config
```yaml
PassMob:
  title: PassMob
  min-player-count: 2
  max-player-count: 8
  waiting-time: 10
  time-limit: 120
  active: true
  icon: OAK_FENCE
  location:
    ==: org.bukkit.Location
    world: world
    x: 0.0
    y: 4.0
    z: 0.0
    pitch: 0.0
    yaw: 0.0
  tutorial:
  - At the game end, winner is the team that has less mobs
  custom-data:
    redLocation:
      ==: org.bukkit.Location
      world: world
      x: 0.0
      y: 0.0
      z: 0.0
      pitch: 0.0
      yaw: 0.0
    blueLocation:
      ==: org.bukkit.Location
      world: world
      x: 0.0
      y: 0.0
      z: 0.0
      pitch: 0.0
      yaw: 0.0
    mobSpawnDelay: 15
    chatting: true
    score-notifying: true
    block-break: false
    block-place: false
    pvp: false
    pve: true
    inventory-save: true
    minigame-respawn: false
    live-gamemode: SURVIVAL
    dead-gamemode: SPECTATOR
    color: RESET
    groupChat: true
    teamPvp: true
    teamRegisterMode: FAIR_FILL
```
- `redLocation`: red team location
- `blueLocation`: blue team location
- `mobSpawnDelay`: random mob spawn delay per second at each team area

# Warning
- Before playing, must setup `redLocation`, `blueLocation`
- Never set difficulty to `peaceful`
