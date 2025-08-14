package lol.vifez.electron.listener;

import lol.vifez.electron.Practice;
import lol.vifez.electron.kit.enums.KitType;
import lol.vifez.electron.match.Match;
import lol.vifez.electron.elo.EloUtil;
import lol.vifez.electron.match.enums.MatchState;
import lol.vifez.electron.match.event.MatchEndEvent;
import lol.vifez.electron.profile.Profile;
import lol.vifez.electron.util.CC;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/*
 * Copyright (c) 2025 Vifez. All rights reserved.
 * Unauthorized use or distribution is prohibited.
 */

public class MatchListener implements Listener {

    private final Practice instance;

    public MatchListener(Practice instance) {
        this.instance = instance;

        instance.getServer().getPluginManager().registerEvents(this, instance);
    }

    @EventHandler
    public void onDamageWhileStart(EntityDamageEvent event) {
        if(event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            Profile profile = instance.getProfileManager().getProfile(player.getName());

            if(profile.inMatch() && (profile.getMatch().getMatchState() == MatchState.STARTING || profile.getMatch().getMatchState() == MatchState.ENDING || profile.getMatch().getMatchState() == MatchState.ENDED)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Profile profile = instance.getProfileManager().getProfile(player.getName());

        if (!profile.inMatch()) return;

        Profile killer = profile.getMatch().getOpponent(player);

        event.getDrops().clear();

        event.setDeathMessage(null);
        event.setNewExp(0);
        event.setNewLevel(0);
        event.setNewTotalExp(0);
        event.setKeepInventory(false);
        event.setKeepLevel(false);

        profile.getMatch().setWinner(killer);
        instance.getMatchManager().end(profile.getMatch());
    }

    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (instance.getProfileManager().getProfile(player.getName()).inMatch()) {
            if (event.getInventory() != null && !event.getInventory().equals(player.getInventory())) {
                event.getInventory().clear();

                event.setCursor(null);
                player.setItemOnCursor(null);

                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onCraft(CraftItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Profile profile = instance.getProfileManager().getProfile(player.getUniqueId());

        if (!profile.inMatch() || profile.getMatch().getMatchState() != MatchState.STARTED) return;

        if (profile.getMatch().isWaterKill() && player.getLocation().getBlock().getType() == Material.WATER) {
            profile.getMatch().setWinner(profile.getMatch().getOpponent(player));
            player.setHealth(0);
        }
    }

    @EventHandler
    public void onInteract(InventoryInteractEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (instance.getProfileManager().getProfile(player.getName()).inMatch()) {
            if (event.getInventory() != null && !event.getInventory().equals(player.getInventory())) {
                event.getInventory().clear();

                player.setItemOnCursor(null);
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Profile profile = instance.getProfileManager().getProfile(player.getName());

        if (!profile.inMatch()) return;

        profile.getMatch().setWinner(profile.getMatch().getOpponent(player));
        instance.getMatchManager().end(profile.getMatch());
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player damager = (Player) event.getDamager();
            Profile profile = instance.getProfileManager().getProfile(damager.getUniqueId());

            if (profile.inMatch() && !event.isCancelled()) {
                profile.getMatch().getHitsMap().put(damager.getUniqueId(), profile.getMatch().getHitsMap().get(damager.getUniqueId()) + 1);

                if (profile.getMatch().getKit().getKitType() == KitType.BOXING) {
                    event.setDamage(0);

                    if (profile.getMatch().getHitsMap().get(damager.getUniqueId()) >= 100) {
                        profile.getMatch().setWinner(profile);
                        profile.getMatch().getOpponent(profile.getPlayer()).getPlayer().setHealth(0);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onMatchEnd(MatchEndEvent event) {
        Profile profileOne = event.getProfileOne();
        Profile profileTwo = event.getProfileTwo();
        Match match = event.getMatch();

        if (match.getWinner() == null) {
            CC.sendMessage(profileOne.getPlayer(), "&cMatch has ended!");
            CC.sendMessage(profileTwo.getPlayer(), "&cMatch has ended!");

            return;
        }

        Profile winner = match.getWinner();
        Profile loser = match.getOpponent(winner.getPlayer());
        Profile[] players = {winner, loser};

        int winnerElo = EloUtil.getNewRating(winner.getElo(match.getKit()), loser.getElo(match.getKit()), true);
        int loserElo = EloUtil.getNewRating(loser.getElo(match.getKit()), winner.getElo(match.getKit()), false);

        winner.setElo(match.getKit(), winner.getElo(match.getKit()) + winnerElo);
        loser.setElo(match.getKit(), loser.getElo(match.getKit()) + loserElo);

        for (Profile profile : players) {
            if (match.isRanked()) {
                CC.sendMessage(profile.getPlayer(), " ");
                CC.sendMessage(profile.getPlayer(), "&b&lMatch Results");
                CC.sendMessage(profile.getPlayer(), " ");
                CC.sendMessage(profile.getPlayer(), "&7* &fWinner: &a" + winner.getName() + " &a[+" + winnerElo + "]");
                CC.sendMessage(profile.getPlayer(), "&7* &fLoser: &c" + loser.getName() + " &c[-" + loserElo + "]");
                CC.sendMessage(profile.getPlayer(), "&r");
                CC.sendMessage(profile.getPlayer(), "    &b&l[GG] &6&l[Rematch]");
                CC.sendMessage(profile.getPlayer(), " ");

                profile.checkDivision(match.getKit());
            } else {
                CC.sendMessage(profile.getPlayer(), " ");
                CC.sendMessage(profile.getPlayer(), "&b&lMatch Results");
                CC.sendMessage(profile.getPlayer(), " ");
                CC.sendMessage(profile.getPlayer(), "&7* &fWinner: &a" + winner.getName());
                CC.sendMessage(profile.getPlayer(), "&7* &fLoser: &c" + loser.getName());
                CC.sendMessage(profile.getPlayer(), "&r");
                CC.sendMessage(profile.getPlayer(), "    &b&l[GG] &6&l[Rematch]");
                CC.sendMessage(profile.getPlayer(), " ");
            }
        }
    }
}