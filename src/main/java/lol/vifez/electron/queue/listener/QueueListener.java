package lol.vifez.electron.queue.listener;

import lol.vifez.electron.Practice;
import lol.vifez.electron.kit.menu.kiteditor.KitSelectionMenu;
import lol.vifez.electron.leaderboard.menu.LeaderboardMenu;
import lol.vifez.electron.profile.Profile;
import lol.vifez.electron.profile.menu.settings.SettingsMenu;
import lol.vifez.electron.queue.Queue;
import lol.vifez.electron.queue.menu.RankedMenu;
import lol.vifez.electron.queue.menu.UnrankedMenu;
import lol.vifez.electron.util.CC;
import lol.vifez.electron.hotbar.Hotbar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

/*
 * Copyright (c) 2025 Vifez. All rights reserved.
 * Unauthorized use or distribution is prohibited.
 */

public class QueueListener implements Listener {

    private final Practice instance;

    public QueueListener(Practice instance) {
        this.instance = instance;

        instance.getServer().getPluginManager().registerEvents(this, instance);
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Queue queue = instance.getQueueManager().getQueue(player.getUniqueId());

        if (queue != null) {
            queue.remove(player);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player player = event.getPlayer();

            ItemStack itemInHand = player.getInventory().getItemInHand();

            if (itemInHand != null && itemInHand.isSimilar(Hotbar.UNRANKED.getItem())) {
                event.setCancelled(true);
                new UnrankedMenu(instance).openMenu(player);
            }

            if (itemInHand != null && itemInHand.isSimilar(Hotbar.LEADERBOARDS.getItem())) {
                event.setCancelled(true);
                new LeaderboardMenu(instance).openMenu(player);
            }

            if (itemInHand != null && itemInHand.isSimilar(Hotbar.SETTINGS.getItem())) {
                event.setCancelled(true);
                Profile profile = instance.getProfileManager().getProfile(player.getUniqueId());
                if (profile != null) {
                    new SettingsMenu(instance, profile).openMenu(player);
                } else {
                    player.sendMessage(CC.translate("&cProfile not found!"));
                }
            }


            if (itemInHand != null && itemInHand.isSimilar(Hotbar.RANKED.getItem())) {
                event.setCancelled(true);
                new RankedMenu(instance).openMenu(player);


            } else if (itemInHand.isSimilar(Hotbar.LEAVE_QUEUE.getItem())) {
                event.setCancelled(true);

                Queue queue = instance.getQueueManager().getQueue(player.getUniqueId());

                if (queue != null) {
                    queue.remove(player);
                }

                player.getInventory().setContents(Hotbar.getSpawnItems());
                player.getInventory().setArmorContents(null);

                CC.sendMessage(player, "&cYou left the queue!");
            } else if (itemInHand.isSimilar(Hotbar.KIT_EDITOR.getItem())) {
                event.setCancelled(true);

                new KitSelectionMenu(instance).openMenu(player);
            } else if (itemInHand.isSimilar(Hotbar.LEADERBOARDS.getItem())) {
                event.setCancelled(true);

                new LeaderboardMenu(instance).openMenu(player);
            }
        }
    }
}
