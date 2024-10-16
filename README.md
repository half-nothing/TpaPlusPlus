> This branch develops version 1.21.1 fabric

# TPA++
TPA++ is an async, open source and customisable TPA mod. While the mod still functions out of the box, there are many in-depth options to configure and change to your liking. Additionally, TPA++ has some features under the "/tpaplusplus" command that allows you to make mass changes to your configuration easily and quickly. Check out the [wiki](https://github.com/SuperRicky14/TpaPlusPlus/wiki) for more information.
> **TPA++ is the first TPA mod to run on Forge, NeoForge, Fabric and Quilt.** Additionally, TPA++ run's on every version from **1.20.1 - 1.20.5-SNAPSHOT** (*inclusive*).

# What can TPA++ do?
TPAPlusPlus currently features eight custom commands, here is a quick run-through over each of them (Command arguments marked with $ are optional!):
* `/tpa [player]` • Sends a teleport request to any player.
* `/tpahere [player]` • Sends a teleport here request to any player.
* `/tpaaccept $[player]` • Accepts the latest teleport request. If a player is specified, accepts the teleport request from that player.
* `/tpadeny $[player]` • Denies the latest teleport request. If a player is specified, denies the teleport request from that player.
* `/tpacancel $[player]` • Cancels the last teleport request that you sent. If a player is specified, cancels the teleport request to that player.
* `/tptoggle` • Toggles on/off teleport request's for the player running the command.
* `/back` • After a player dies, running this command will teleport them to their latest death. This can be disabled in the config.
* `/tpblock` • Prevents a player from sending teleport requests to you.
* `/tpunblock` • Allows a blocked player to send teleport requests to you again.
* `/tpaplusplus` • [This command has its own documentation on the wiki!](https://github.com/SuperRicky14/TpaPlusPlus/wiki/TPAPlusPlus-Server-Management-Command)

TPA++ also features some unique and exclusive features, here is a run through over the main ones:
* `Command Windups` • Controls how long any command takes to execute. For example, you can make /tpa take 5 seconds, and /tpaaccept take 3 seconds. Since update 1.5.2 you can now use decimal numbers in your windups!
* `Command Windup Distance` • Controls how far away a player can move from their original starting point, after the command's "*windup countdown*" has started.
* `Range Checks` • Controls the minimum / maximum distance a player can be from another player before sending / accepting a teleport request.
* `Dimension Checks` • Prevents players from teleporting between dimensions, i.e. they must be in the same dimension to TPA to eachother.
* `Message Control` • Lets you modify every message sent by TPA++. You can add color codes, emoji, rewrite any sentences to whatever you want (there's seriously no restrictions, go wild).
* `Command Control` • Lets you modify all the commands (what players type in chat), so if you want you can change `/tpa` to `/whatever`
* `Automatic Update Checking` • Automatically check's for updates to TPA++ so you never miss out on the latest features!
* `Command Cooldowns` • Controls the interval before a player can execute the same command again (requested in [feature request #1](https://github.com/SuperRicky14/TpaPlusPlus/issues/1)).

## Upcoming / Planned Features
* `Global Cooldown` • Controls the interval before a player can run any TPA++ command sequentially after another.

# Additional Information / FAQ:
* `Is TPA++ server-side only?` • Yes, TPA++ is server sided and always will remain. This means you can run a (*neoforge*/*forge*/*fabric*/*quilt*) server and let completely vanilla clients join, or add this mod to an existing modpack without having to add it to everyone's client.
* `Does this mod work in singleplayer / open-to-LAN` • Yes, the mod functions perfectly in singleplayer / open-to-lan. The mod also runs perfectly fine on [e4mc](https://www.curseforge.com/minecraft/mc-mods/e4mc) / [essential](https://essential.gg/) / [world host](https://modrinth.com/mod/world-host), allowing your friends to connect to your worlds and still run fine with TPA++ (useful if you want to run TPA with your friends but don't want to setup a whole server for it).
* `Will you backport TPA++?` • Backports are not planned.
* `I found a bug / problem with the mod.` • You can report this to me on my [Github's issue page.](https://github.com/SuperRicky14/TpaPlusPlus/issues)
* `I want to see feature [x] implemented!` • Again, you can fill out the feature request form on the [issue page!](https://github.com/SuperRicky14/TpaPlusPlus/issues)
* `Something I want to change can't be modified in the config!` • You can suggest what option you want to see added on the [issue page!](https://github.com/SuperRicky14/TpaPlusPlus/issues)
* `Can I modify / fork / contribute to TPA++?` • Yes, there is a [contributing guide](https://github.com/SuperRicky14/TpaPlusPlus/blob/master/CONTRIBUTING.md) on TPA++'s Github, you are free to do whatever you want with the mod under the [MIT License](https://github.com/SuperRicky14/TpaPlusPlus/blob/master/LICENSE)

## Dependencies:
While I tried to minimize dependancies at first, in order to support all mod loaders, it is much quicker and faster for me to push out updates using these mods than if I wrote everything myself. Additionally, these are pretty common dependencies, so you likely already have these in your modpack.
### NeoForge and Forge:
* [Architectury API](https://modrinth.com/mod/architectury-api)
* [Kotlin for Forge](https://www.curseforge.com/minecraft/mc-mods/kotlin-for-forge)
### Fabric and Quilt
* [Architectury API](https://modrinth.com/mod/architectury-api)
* [Forge Config API Port](https://modrinth.com/mod/forge-config-api-port)
* [Fabric Language Kotlin](https://modrinth.com/mod/fabric-language-kotlin)


## Thanks to all developers:
<a href="https://github.com/SuperRicky14/TpaPlusPlus/graphs/contributors">
  <img src="https://contrib.rocks/image?repo=SuperRicky14/TpaPlusPlus"  alt="icon"/>
</a>

[I would also like to be one of the contributors?](./CONTRIBUTING.md)

## Support
> Links: [CurseForge](https://www.curseforge.com/minecraft/mc-mods/tpaplusplus) / [Modrinth](https://modrinth.com/mod/pPuyOJU7) / [Wiki](https://github.com/SuperRicky14/TpaPlusPlus/wiki).
