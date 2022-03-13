# SuperMob
- Baby boss monster can use several skills!
- Type: `SoloBattle`
- Bukkit: `Spigot` <!--  Write bukkit, If event of minigame is only available in specific bukkit-->
- API Version: `LATEST`
- Minecraft Versions: `1.14+`

# How to play
- Damage boss monster to get score
- Kill other monster to get a little score

# Play Video
- [Youtube](https://www.youtube.com/watch?v=qep_l8NKJ9Y)

# Config
```yaml
SuperMob:
  title: SuperMob
  min-player-count: 1
  max-player-count: 5
  waiting-time: 10
  play-time: 120
  active: true
  icon: ZOMBIE_HEAD
  location:
    ==: org.bukkit.Location
    world: world
    x: 0.0
    y: 4.0
    z: 0.0
    pitch: 0.0
    yaw: 0.0
  tutorial:
  - 'Hit Super Mob: +(damage)'
  custom-data:
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
```


# Warning
- boss monster is a baby zombie, so block space with walls
- skills: `spawn other mobs`, `get speed effect`, `get increase damage effect`, `damage resistance`
