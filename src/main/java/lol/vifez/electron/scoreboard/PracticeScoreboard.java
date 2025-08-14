package lol.vifez.electron.scoreboard;

import io.github.thatkawaiisam.assemble.AssembleAdapter;
import lol.vifez.electron.Practice;
import lol.vifez.electron.kit.enums.KitType;
import lol.vifez.electron.match.Match;
import lol.vifez.electron.elo.EloUtil;
import lol.vifez.electron.match.enums.MatchState;
import lol.vifez.electron.profile.Profile;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/*
 * Copyright (c) 2025 Vifez. All rights reserved.
 * Unauthorized use or distribution is prohibited.
 */

public class PracticeScoreboard implements AssembleAdapter {

    private final Practice instance;
    private final AtomicInteger titleIndex = new AtomicInteger(0);

    public PracticeScoreboard(Practice instance) {
        this.instance = instance;
    }

    @Override
    public String getTitle(Player player) {
        Profile profile = instance.getProfileManager().getProfile(player.getUniqueId());
        Match match = instance.getMatchManager().getMatch(profile.getUuid());

        if (!profile.isScoreboardEnabled()) return "";

        if (match != null) {
            if (match.getMatchState() == MatchState.STARTED) {
                if (match.getKit().getKitType() == KitType.BOXING) {
                    return instance.getConfig().getString("scoreboard.in-boxing.title");
                } else {
                    return instance.getConfig().getString("scoreboard.in-game.title");
                }
            } else if (match.getMatchState() == MatchState.STARTING) {
                return instance.getConfig().getString("scoreboard.match-starting.title");
            } else if (match.getMatchState() == MatchState.ENDING) {
                return instance.getConfig().getString("scoreboard.match-ending.title");
            }
        } else if (instance.getQueueManager().getQueue(profile.getUuid()) != null) {
            return instance.getConfig().getString("scoreboard.in-queue.title");
        }

        return instance.getConfig().getString("scoreboard.in-lobby.title");
    }

    @Override
    public List<String> getLines(Player player) {
        List<String> list = new ArrayList<>();

        Profile profile = instance.getProfileManager().getProfile(player.getUniqueId());
        Match match = instance.getMatchManager().getMatch(profile.getUuid());

        String globalElo = String.valueOf(EloUtil.getGlobalElo(profile));
        String division = profile.getDivision().getPrettyName();

        if (!profile.isScoreboardEnabled()) return new ArrayList<>();

        if (match != null) {
            int hits = match.getHitsMap().get(profile.getUuid());

            if (match.getMatchState() == MatchState.STARTED) {
                if (match.getKit().getKitType() == KitType.BOXING) {
                    for (String str : instance.getConfig().getStringList("scoreboard.in-boxing.lines")) {
                        list.add(str
                                .replace("<ping>", instance.getProfileManager().getProfile(player.getUniqueId()).getPing() + "")
                                .replace("<opponent-ping>", match.getOpponent(player).getPing() + "")
                                .replace("<opponent>", match.getOpponent(player).getName())
                                .replace("<duration>", match.getDuration())
                                .replace("<difference>", (hits < 0 ? "&c" + hits : hits == 0 ? "&e" : "&a") + hits)
                                .replace("<their-hits>", String.valueOf(match.getHitsMap().get(match.getOpponent(player).getUuid())))
                                .replace("<your-hits>", String.valueOf(hits))
                                .replace("<global-elo>", globalElo)
                                .replace("<division>", division));
                    }
                } else {
                    for (String str : instance.getConfig().getStringList("scoreboard.in-game.lines")) {
                        list.add(str
                                .replace("<ping>", instance.getProfileManager().getProfile(player.getUniqueId()).getPing() + "")
                                .replace("<opponent-ping>", match.getOpponent(player).getPing() + "")
                                .replace("<opponent>", match.getOpponent(player).getName())
                                .replace("<duration>", match.getDuration())
                                .replace("<global-elo>", globalElo)
                                .replace("<division>", division));
                    }
                }
            } else if (match.getMatchState() == MatchState.ENDING) {
                for (String str : instance.getConfig().getStringList("scoreboard.match-ending.lines")) {
                    list.add(str
                            .replace("<winner>", match.getWinner() == null ? "None" : match.getWinner().getName())
                            .replace("<loser>", match.getWinner() == null ? player.getName() + " " + match.getOpponent(player).getName() : match.getOpponent(match.getWinner().getPlayer()).getName()));
                }
            } else if (match.getMatchState() == MatchState.STARTING) {
                for (String str : instance.getConfig().getStringList("scoreboard.match-starting.lines")) {
                    list.add(str
                            .replace("<winner>", match.getWinner() == null ? "None" : match.getWinner().getName())
                            .replace("<loser>", match.getWinner() == null ? player.getName() + " " + match.getOpponent(player).getName() : match.getOpponent(match.getWinner().getPlayer()).getName()));
                }
            }
        } else if (instance.getQueueManager().getQueue(profile.getUuid()) != null) {
            for (String str : instance.getConfig().getStringList("scoreboard.in-queue.lines")) {
                list.add(str.replace("<online>", Bukkit.getOnlinePlayers().size() + "")
                        .replace("<in-queue>", instance.getQueueManager().getAllQueueSize() + "")
                        .replace("<kit>", instance.getQueueManager().getQueue(profile.getUuid()).getKit().getName())
                        .replace("<time>", instance.getQueueManager().getQueue(profile.getUuid()).getQueueTime(profile.getUuid()))
                        .replace("<playing>", instance.getMatchManager().getAllMatchSize() + "")
                        .replace("<username>", player.getName())
                        .replace("<global-elo>", globalElo)
                        .replace("<division>", division));
            }
        } else {
            for (String str : instance.getConfig().getStringList("scoreboard.in-lobby.lines")) {
                list.add(str.replace("<online>", Bukkit.getOnlinePlayers().size() + "")
                        .replace("<in-queue>", instance.getQueueManager().getAllQueueSize() + "")
                        .replace("<playing>", instance.getMatchManager().getAllMatchSize() + "")
                        .replace("<ping>", instance.getProfileManager().getProfile(player.getUniqueId()).getPing() + "")
                        .replace("<username>", player.getName())
                        .replace("<global-elo>", globalElo)
                        .replace("<division>", division));
            }
        }
        return list;
    }
}