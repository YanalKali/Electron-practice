package lol.vifez.electron.profile;

import com.google.gson.annotations.SerializedName;
import com.mongodb.client.model.Filters;
import lol.vifez.electron.Practice;
import lol.vifez.electron.kit.Kit;
import lol.vifez.electron.divisions.Divisions;
import lol.vifez.electron.match.Match;
import lol.vifez.electron.queue.Queue;
import lol.vifez.electron.util.CC;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/*
 * Copyright (c) 2025 Vifez. All rights reserved.
 * Unauthorized use or distribution is prohibited.
 */

@RequiredArgsConstructor
@Getter @Setter
public class Profile {

    @SerializedName("_id")
    private final UUID uuid;

    private Player lastMessagedPlayer;

    private String name, currentQueue = "";
    private Divisions division = Divisions.SILVER_I;

    private int wins = 0, losses = 0, winStreak = 0;
    private boolean editMode = false, buildMode = false;

    private Map<String, ItemStack[]> kitLoadout = new HashMap<>();
    private Map<String, Integer> kitWins = new HashMap<>();
    private Map<String, Integer> eloMap = new HashMap<>();

    private boolean scoreboardEnabled = true;
    private boolean privateMessagingEnabled = true;
    private String worldTime = "DAY";

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid) != null && Bukkit.getPlayer(uuid).isOnline() ? Bukkit.getPlayer(uuid) : null;
    }

    public Player getLastMessagedPlayer() {
        return lastMessagedPlayer;
    }

    public void setLastMessagedPlayer(Player player) {
        this.lastMessagedPlayer = player;
    }

    public boolean isScoreboardEnabled() {
        return scoreboardEnabled;
    }

    public void setScoreboardEnabled(boolean enabled) {
        this.scoreboardEnabled = enabled;
        saveToMongoDB();
    }

    public String getWorldTime() {
        return worldTime;
    }

    public void setWorldTime(String worldTime) {
        this.worldTime = worldTime;
    }

    public boolean isPrivateMessagingEnabled() {
        return privateMessagingEnabled;
    }

    public void setPrivateMessagingEnabled(boolean privateMessagingEnabled) {
        this.privateMessagingEnabled = privateMessagingEnabled;
    }

    private void saveToMongoDB() {
        ProfileManager profileManager = Practice.get().getProfileManager();
        profileManager.getProfileRepository().getCollection()
                .updateOne(Filters.eq("_id", uuid.toString()),
                        new org.bson.Document("$set", new org.bson.Document("scoreboardEnabled", scoreboardEnabled)));
    }

    public int getPing() {
        Player player = getPlayer();

        if (player != null) {
            try {
                Object entityPlayer = player.getClass().getMethod("getHandle").invoke(player);

                return (int) entityPlayer.getClass().getField("ping").get(entityPlayer);
            } catch (Exception e) {
                e.printStackTrace();
                return -1;
            }
        }

        return -1;
    }

    public int getElo(Kit kit) {
        return eloMap.getOrDefault(kit.getName().toLowerCase(), 1000);
    }

    public void setElo(Kit kit, int elo) {
        eloMap.put(kit.getName().toLowerCase(), elo);
    }

    public boolean inMatch() {
        return Practice.get().getMatchManager().getMatch(uuid) != null;
    }

    public Match getMatch() {
        return Practice.get().getMatchManager().getMatch(uuid);
    }

    public Queue getQueue() {
        return Practice.get().getQueueManager().getQueue(uuid);
    }

    public void checkDivision(Kit kit) {
        int elo = getElo(kit);
        Divisions playerDivision = getDivision();

        for (Divisions division : Divisions.values()) {
            if (elo >= division.getMinimumElo()) {
                playerDivision = division;
            } else break;
        }

        if (this.division != playerDivision) {
            this.division = playerDivision;

            CC.sendMessage(getPlayer(), "&aYou are now in " + playerDivision.getPrettyName() + " &adivision!");
        }
    }
}