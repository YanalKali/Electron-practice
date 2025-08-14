package lol.vifez.electron.tab;

import lol.vifez.electron.Practice;
import lol.vifez.electron.util.CC;
import lol.vifez.electron.util.StringUtil;
import lombok.RequiredArgsConstructor;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import xyz.refinedev.api.tablist.adapter.TabAdapter;
import xyz.refinedev.api.tablist.setup.TabEntry;

import java.util.ArrayList;
import java.util.List;

/*
 * Copyright (c) 2025 Vifez. All rights reserved.
 * Unauthorized use or distribution is prohibited.
 */

@RequiredArgsConstructor
public class ElectronTab implements TabAdapter {

    private final Practice instance;
    private final boolean isPlaceholderAPI;

    @Override
    public String getHeader(Player player) {
        return StringUtil.listToString(instance.getConfig().getStringList("tab.header"));
    }

    @Override
    public String getFooter(Player player) {
        return StringUtil.listToString(instance.getConfig().getStringList("tab.footer"));
    }

    @Override
    public List<TabEntry> getLines(Player player) {
        List<TabEntry> entries = new ArrayList<>();

        int i = 0;
        for (String str : instance.getConfig().getStringList("lines.left")) {
            entries.add(new TabEntry(0, i, CC.colorize(isPlaceholderAPI ? PlaceholderAPI.setPlaceholders(player, str) : str)));
            i++;
        }

        i = 0;
        for (String str : instance.getConfig().getStringList("lines.middle")) {
            entries.add(new TabEntry(1, i, CC.colorize(isPlaceholderAPI ? PlaceholderAPI.setPlaceholders(player, str) : str)));
            i++;
        }

        i = 0;
        for (String str : instance.getConfig().getStringList("lines.right")) {
            entries.add(new TabEntry(2, i, CC.colorize(isPlaceholderAPI ? PlaceholderAPI.setPlaceholders(player, str) : str)));
            i++;
        }

        i = 0;
        for (String str : instance.getConfig().getStringList("lines.far-right")) {
            entries.add(new TabEntry(3, i, CC.colorize(isPlaceholderAPI ? PlaceholderAPI.setPlaceholders(player, str) : str)));
            i++;
        }

        return entries;
    }
}
