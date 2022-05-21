# Bridge
- Drop other player from the bridge
- Bukkit: `Spigot` 
- Type: `SoloBattle`
- API Version: `LATEST`
- Minecraft Version: `1.14+`

# How to play
- Drop other players
- There are 3 skills
1. `JUMP`: jumpt to the sky
2. `SPEED`: become more faster for 3 seconds
3. `HIDE`: hide for 3 seconds

# Play Video
- [Youtube](https://www.youtube.com/watch?v=_QPvGaJrpvc)

# Config
```yaml
Bridge:
  title: Bridge
  min-players: 2
  max-players: 4
  waiting-time: 20
  play-time: 180
  active: true
  icon: REPEATER
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
  - Don't fall down from bridge
  - Use skills with RIGHT_CLICK items
  custom-data:
    items:
    - ==: org.bukkit.inventory.ItemStack
      v: 2865
      type: FISHING_ROD
    skill-cooldown: 10
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
    food-level-change: true
    player-hurt: true
```
- `items`: items given to players when the game starts
- `skill-cooldown`: Skill cooldown per second

# Warning
- Player will die if fall down below the y of `location`