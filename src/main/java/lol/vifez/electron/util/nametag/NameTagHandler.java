package lol.vifez.electron.util.nametag;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

@RequiredArgsConstructor
@Getter
public class NameTagHandler {

    private final NameTagAPI api;

    public void setNameTag(Player player, Player target, String prefix, String suffix) {
        Scoreboard scoreboard = player.getScoreboard();

        if (scoreboard.equals(api.getScoreboard())) {
            scoreboard = api.getPlugin().getServer().getScoreboardManager().getNewScoreboard();

            player.setScoreboard(scoreboard);
        }

        Team team = scoreboard.getTeam(getTeamName(target));
        if (team == null) {
            team = scoreboard.registerNewTeam(getTeamName(target));
        }

        team.setPrefix(ChatColor.translateAlternateColorCodes('&', prefix));
        team.setSuffix(ChatColor.translateAlternateColorCodes('&', suffix));

        if (!team.hasEntry(getTeamName(target))) {
            team.addEntry(target.getName());
        }
    }

    public void removeNameTag(Player player) {
        Scoreboard scoreboard = player.getScoreboard();

        if (scoreboard.equals(api.getScoreboard())) {
            scoreboard = api.getPlugin().getServer().getScoreboardManager().getNewScoreboard();

            player.setScoreboard(scoreboard);
        }

        Team team = scoreboard.getTeam(getTeamName(player));

        if (team == null) return;

        if (team.hasEntry(getTeamName(player))) {
            team.removeEntry(getTeamName(player));
        }

        team.removePlayer(player);
        team.unregister();
    }

    private String getTeamName(Player player) {
        String teamName = "nt_" + player.getName();

        if (teamName.length() > 16) teamName = teamName.substring(0, 16);

        return teamName;
    }
}
