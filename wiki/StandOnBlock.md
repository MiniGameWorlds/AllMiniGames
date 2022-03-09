# StandOnBlock
- Find and stand on the block!
- Bukkit: `Spigot`
- Type: `SoloBattle`
- API Version: `LATEST`
- Minecraft Version: `1.14+`



# How to play
- There are random blocks below your foots
- Only one block will be remained and other blocks will be disappeared
- Remain block will show in player's inventory bar
- Never fall from the block!



# Play Video
- [Youtube](https://youtu.be/1-kPChBkEio)



# Config
```yaml
StandOnBlock:
  title: StandOnBlock
  min-player-count: 2
  max-player-count: 10
  waiting-time: 20
  time-limit: 120
  active: true
  icon: ARMOR_STAND
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
    - There are random blocks
    - Only one block will be remained while other blocks will be disappeared
    - Never fall from the block
  custom-data:
    blocks:
    - RED_WOOL
    - ORANGE_WOOL
    - YELLOW_WOOL
    - GREEN_WOOL
    - BLUE_WOOL
    - PURPLE_WOOL
    - WHITE_WOOL
    - BLACK_WOOL
    pos1:
      ==: org.bukkit.Location
      world: world
      x: 0.0
      y: 4.0
      z: 0.0
      pitch: 0.0
      yaw: 0.0
    pos2:
      ==: org.bukkit.Location
      world: world
      x: 0.0
      y: 4.0
      z: 0.0
      pitch: 0.0
      yaw: 0.0
    disappear-delay: 5.0
    disappear-delay-decrease: 0.2
    void-time: 3.0
    chat: true
    score-notifying: true
    block-break: false
    block-place: false
    pvp: false
    pve: true
    inventory-save: true
    minigame-respawn: true
    live-gamemode: SURVIVAL
    dead-gamemode: SPECTATOR
    color: GREEN
    food-level-change: false
    player-hurt: true
```
- `blocks`: Block list that fills the area (Material)
- `pos1`: Area first position
- `pos2`: Area second position
- `disappear-delay`: Delay that blocks will be disappeared after (sec) (decimals)
- `disappear-delay-decrease`: Delay decrease amount after every round (sec) (decimals)
- `void-time`: The time that the void lasts (sec) (decimals)




# Warning
- Setup `pos1` and `pos2` before play