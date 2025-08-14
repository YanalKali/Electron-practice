package lol.vifez.electron.arena.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import lol.vifez.electron.Practice;
import lol.vifez.electron.arena.menu.ArenasMenu;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/*
 * Copyright (c) 2025 Vifez. All rights reserved.
 * Unauthorized use or distribution is prohibited.
 */

@CommandAlias("arenas")
@CommandPermission("electron.arena.list")
public class ArenasCommand extends BaseCommand {

    private final Practice plugin;

    public ArenasCommand(Practice plugin) {
        this.plugin = plugin;
    }

    @Default
    public void onArenasCommand(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players.");
            return;
        }

        Player player = (Player) sender;
        new ArenasMenu(plugin.getArenaManager()).openMenu(player);
    }
}