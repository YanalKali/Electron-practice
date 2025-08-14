package lol.vifez.electron.util.menu.button.impl;

import lol.vifez.electron.util.ItemBuilder;
import lol.vifez.electron.util.menu.Menu;
import lol.vifez.electron.util.menu.button.Button;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class BackButton extends Button {
    private final Menu back;

    public BackButton(final Menu back) {
        this.back = back;
    }

    @Override
    public ItemStack getItem(final Player player) {
        final ItemBuilder item = new ItemBuilder(Material.ARROW).name("&8« &7Go back");
        return item.build();
    }

    @Override
    public void onClick(Player player, int i, ClickType clickType) {
        this.back.openMenu(player);
    }
}