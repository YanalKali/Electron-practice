package lol.vifez.electron.leaderboard.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Dependency;
import lol.vifez.electron.Practice;
import lol.vifez.electron.leaderboard.menu.LeaderboardMenu;
import org.bukkit.entity.Player;

@CommandAlias("leaderboard|lb|leaderboards")
public class LeaderboardCommand extends BaseCommand {

    @Dependency
    private Practice instance;

    @Default
    public void onLeaderboard(Player player) {
        new LeaderboardMenu(instance).openMenu(player);
    }
}