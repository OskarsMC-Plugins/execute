# execute <a href="https://discord.gg/KfmcRzv6Gh"><img src="https://img.shields.io/discord/840618521611337759?color=pink&label=Discord&logo=discord&logoColor=pink&style=for-the-badge"></a>
A plugin to schedule execution of commands on multiple platforms

## Platforms:
<table>
    <tr>
        <th>Spigot</th>
        <th>Velocity</th>
        <th>BungeeCord</th>
    </tr>
    <tr>
        <td><a href="https://bstats.org/plugin/bukkit/execute-spigot/11307"> <img src="https://img.shields.io/bstats/servers/11307?color=green&label=Servers%3A&style=for-the-badge"></a></td>
        <td><a href="https://bstats.org/plugin/velocity/execute-velocity/11308"> <img src="https://img.shields.io/bstats/servers/11308?color=green&label=Servers%3A&style=for-the-badge"> </a></td>
        <td><a href="https://bstats.org/plugin/bungeecord/execute-bungee/11309"> <img src="https://img.shields.io/bstats/servers/11309?color=green&label=Servers%3A&style=for-the-badge"></a></td>
    </tr>
</table>

## Configuration:
```toml
# execute
# Currently supported platforms: Spigot, Velocity
# Compatible platforms: Spigot, BungeeCord, Velocity
[plugin]
    enabled=true
    # If you would like to opt out from including the name of the commands excecuted below in stats. THIS DOES NOT OPT YOU OUT FROM BSTATS!
    include-command-in-stats=true

# Commands:
# Use cron expressions to schedule commands (https://crontab.guru/ to generate cron expressions if you are unfamiliar)
# If you use OskarsMC-Plugins/broadcast, use "broadcast-id <id>"
[commands]
    "* * * * *" = [
        "say This Message Is Sent Every Minute!",
        "say And so is this one!"
    ]

    "0 0 * * *" = [
        "say Midnight Strikes!"
    ]

# Please don't touch
[developer-info]
    config-version=0.1
```
The configuration uses UNIX Cron Expressions.

# Downloads
Get the latest release <a href="https://github.com/OskarsMC-Plugins/execute">here</a>