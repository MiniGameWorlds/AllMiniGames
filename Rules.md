# Rules
- Manage example minigames of [MiniGameWorld](https://github.com/MiniGameWorlds/MiniGameWorld) in this project
- All minigames api version will maintain `LATEST`
- Minigames not released are not stable

## Base plugin.yml format
```yaml
name: AllMiniGames
main: com.worldbiomusic.allgames.AllMiniGamesMain
version: <version>
author: worldbiomusic
api-version: 1.14
depend: [MiniGameWorld]
```

## When Release each minigames
- Change `name`, `version` in `plugin.yml` for each minigames
- Register only released minigames in `AllMiniGamesMain.java`
- Update resource in `Spigot`, `Paper` forum