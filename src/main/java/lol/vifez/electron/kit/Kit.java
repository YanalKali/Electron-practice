package lol.vifez.electron.kit;

import lol.vifez.electron.kit.enums.KitType;
import lol.vifez.electron.util.ItemBuilder;
import lombok.Data;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/*
 * Copyright (c) 2025 Vifez. All rights reserved.
 * Unauthorized use or distribution is prohibited.
 */

@Data
public class Kit {

    private final String name;
    private List<String> description = new ArrayList<>();

    private ItemStack[] contents = new ItemStack[]{}, armorContents = new ItemStack[]{};
    private Material icon = Material.IRON_SWORD;

    private ChatColor color = ChatColor.AQUA;
    private KitType kitType = KitType.REGULAR;

    private int weight = 0;
    private boolean ranked = false;

    public ItemStack getDisplayItem() {
        List<String> list = new ArrayList<>(description);

        list.add("&r");
        list.add("&aClick here to queue this kit!");

        return new ItemBuilder(icon)
                .name(color + name)
                .lore(list)
                .build();
    }
}