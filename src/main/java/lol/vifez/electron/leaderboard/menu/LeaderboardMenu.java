package lol.vifez.electron.leaderboard.menu;

import lol.vifez.electron.Practice;
import lol.vifez.electron.elo.EloUtil;
import lol.vifez.electron.kit.Kit;
import lol.vifez.electron.profile.Profile;
import lol.vifez.electron.util.ItemBuilder;
import lol.vifez.electron.util.menu.Menu;
import lol.vifez.electron.util.menu.button.Button;
import lol.vifez.electron.util.menu.button.impl.EasyButton;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

/*
 * Copyright (c) 2025 Vifez. All rights reserved.
 * Unauthorized use or distribution is prohibited.
 */

@RequiredArgsConstructor
public class LeaderboardMenu extends Menu {

    private final Practice instance;

    @Override
    public String getTitle(Player player) {
        return "&eLeaderboard";
    }

    @Override
    public int getSize() {
        return 45;
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        List<Profile> topPlayers = instance.getProfileManager().getProfiles().values().stream()
                .sorted(Comparator.comparingInt(EloUtil::getGlobalElo).reversed())
                .limit(10)
                .collect(Collectors.toList());

        List<String> lore = new ArrayList<>();
        for (int i = 0; i < topPlayers.size(); i++) {
            Profile profile = topPlayers.get(i);
            int globalElo = EloUtil.getGlobalElo(profile);
            lore.add("&b" + (i + 1) + ". &e" + profile.getName() + " &7- &b" + globalElo);
        }

        buttons.put(4, new EasyButton(
                new ItemBuilder(Material.NETHER_STAR)
                        .name("&b&lGlobal Elo Leaderboard")
                        .lore(lore)
                        .build(),
                true, true, () -> {}
        ));

        Kit[] kits = instance.getKitManager().getKits().values().toArray(new Kit[0]);
        int startIndex = 10;

        for (int i = 0; i < kits.length; i++) {
            int slot = startIndex + i;
            buttons.put(slot, new LeaderboardButton(instance, kits[i]));
        }

        int[] borderSlots = {
                0, 1, 2, 3, 5, 6, 7, 8,
                9, 17, 18, 26, 27, 35,
                36, 37, 38, 39, 40, 41, 42, 43, 44
        };

        for (int slot : borderSlots) {
            buttons.put(slot, new EasyButton(
                    new ItemBuilder(Material.STAINED_GLASS_PANE)
                            .durability((short) 15)
                            .name("&7")
                            .build(),
                    true, false, () -> {}
            ));
        }

        return buttons;
    }
}

class LeaderboardButton extends EasyButton {
    public LeaderboardButton(Practice instance, Kit kit) {
        super(new ItemBuilder(kit.getDisplayItem())
                .name(kit.getColor() + kit.getName())
                .lore(instance.getLeaderboards().getLeaderboardLayout(kit))
                .build(), true, true, () -> {}
        );
    }
}