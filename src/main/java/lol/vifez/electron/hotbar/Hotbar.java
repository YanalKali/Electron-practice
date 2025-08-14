package lol.vifez.electron.hotbar;

import lol.vifez.electron.util.ItemBuilder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/*
 * Copyright (c) 2025 Vifez. All rights reserved.
 * Unauthorized use or distribution is prohibited.
 */

@RequiredArgsConstructor
@Getter
public enum Hotbar {
    QUEUES(new ItemBuilder(Material.IRON_SWORD)
            .name("&bQueues")
            .lore("&r",
                    "&7Compete in different modes against",
                    "&7Multiple people to practice your skills!",
                    "&r",
                    "&aRight click to open the menu!")
            .build()),

    UNRANKED(new ItemBuilder(Material.IRON_SWORD)
            .name("&bUnranked Queue &7(Right Click)")
            .build()),

    RANKED(new ItemBuilder(Material.DIAMOND_SWORD)
            .name("&bRanked Queue &7(Right Click)")
            .build()),

    LEADERBOARDS(new ItemBuilder(Material.EMERALD)
            .name("&bLeaderboards &7(Right Click)")
            .build()),

    KIT_EDITOR(new ItemBuilder(Material.BOOK)
            .name("&bKit Editor &7(Right Click)")
            .build()),

    SETTINGS(new ItemBuilder(Material.REDSTONE_COMPARATOR)
            .name("&bSettings &7(Right Click)")
            .build()),

    LEAVE_QUEUE(new ItemBuilder(Material.REDSTONE)
            .name("&cLeave Queue &7(Right Click)")
            .build());


    private final ItemStack item;

    public static ItemStack[] getSpawnItems() {
        return new ItemStack[]{Hotbar.UNRANKED.getItem(),
                Hotbar.RANKED.getItem(),
                null,
                null,
                Hotbar.LEADERBOARDS.getItem(),
                null,
                null,
                // + 4 slots
                Hotbar.KIT_EDITOR.getItem(),
                Hotbar.SETTINGS.getItem()
        };

    }
}