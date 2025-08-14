package lol.vifez.electron.queue.menu;

import lol.vifez.electron.Practice;
import lol.vifez.electron.util.ItemBuilder;
import lol.vifez.electron.util.menu.Menu;
import lol.vifez.electron.util.menu.button.Button;
import lol.vifez.electron.util.menu.button.impl.EasyButton;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/*
 * Copyright (c) 2025 Vifez. All rights reserved.
 * Unauthorized use or distribution is prohibited.
 */

@RequiredArgsConstructor
public class RankedMenu extends Menu {

    private final Practice instance;

    @Override
    public String getTitle(Player player) {
        return "&b&lRanked Queue";
    }

    @Override
    public int getSize() {
        return 45;
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        int[] borderSlots = {
                0, 1, 2, 3, 4, 5, 6, 7, 8,
                9, 17, 18, 26, 27, 35,
                36, 37, 38, 39, 40, 41, 42, 43, 44
        };

        for (int slot : borderSlots) {
            buttons.put(slot, new EasyButton(
                    new ItemBuilder(Material.STAINED_GLASS_PANE)
                            .durability((short) 15)
                            .name("&7")
                            .build(),
                    true, false, () -> {}));
        }

        return buttons;
    }
}