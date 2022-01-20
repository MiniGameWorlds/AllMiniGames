# BreedMob
- Try to breed mobs!
- Available Bukkit: `Spigot` <!--  Write bukkit, If event of minigame is only available in specific bukkit-->
- Type: `Team`
- API Version: `LATEST`
- Available Minecraft Versions: `1.14+`

# How to play
- Kill mobs to get scores, but mobs will breed twice when dying

# Play Video
- [Youtube](https://www.youtube.com/watch?v=BMjKbdKJYCc)

# Config
```yaml
BreedMob:
  title: BreedMob
  min-player-count: 1
  max-player-count: 4
  waiting-time: 10
  time-limit: 120
  active: true
  icon: ZOMBIE_SPAWN_EGG
  location:
    ==: org.bukkit.Location
    world: world
    x: 0.0
    y: 4.0
    z: 0.0
    pitch: 0.0
    yaw: 0.0
  tutorial:
  - 'Kill mob: +1'
  - 'Death: spectator'
  - Mobs will lay two mobs when die
  custom-data:
    mobs:
    - ZOMBIE
    - SKELETON
    - SPIDER
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
    color: RED
```
- `custom-data.mobs`: breeding mob list (e.g. `CREEPER`, `IRON_GOLEM`)

# Warning
- Creeper can ruin games
- Not offensive mobs may follow a player (e.g. cow)