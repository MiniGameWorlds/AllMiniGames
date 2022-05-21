# PassMob
- Pass mobs to the next!
- Type: `TeamBattle`
- Bukkit: `Spigot` 
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
  min-players: 2
  max-players: 8
  waiting-time: 10
  play-time: 120
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
    red-location:
      ==: org.bukkit.Location
      world: world
      x: 0.0
      y: 0.0
      z: 0.0
      pitch: 0.0
      yaw: 0.0
    blue-location:
      ==: org.bukkit.Location
      world: world
      x: 0.0
      y: 0.0
      z: 0.0
      pitch: 0.0
      yaw: 0.0
    mob-spawn-delay: 15
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
- `red-location`: red team location
- `blue-location`: blue team location
- `mob-spawn-delay`: random mob spawn delay per second at each team area

# Warning
- Before playing, must setup `red-location`, `blue-location`
- Never set difficulty to `peaceful`
