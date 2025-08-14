package lol.vifez.electron.util.menu.button;

import lol.vifez.electron.util.menu.Menu;
import lol.vifez.electron.util.menu.MenuAPI;
import lol.vifez.electron.util.menu.pagination.PaginatedMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class ButtonListener implements Listener {

    public static void onInventoryClose(Player player) {
        final Menu openMenu = MenuAPI.getOpenedMenus().get(player.getUniqueId());

        if (openMenu != null) {
            openMenu.setInventory(player.getOpenInventory().getTopInventory());
            openMenu.onClose(player);

            MenuAPI.getOpenedMenus().remove(player.getUniqueId());
            if (openMenu instanceof PaginatedMenu) return;
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onInventoryClose(final InventoryCloseEvent event) {
        final Player player = (Player) event.getPlayer();
        final Menu openMenu = MenuAPI.getOpenedMenus().get(player.getUniqueId());

        if (openMenu != null) {
            openMenu.setInventory(event.getInventory());
            openMenu.onClose(player);

            MenuAPI.getOpenedMenus().remove(player.getUniqueId());
        }
    }

    @EventHandler
    public void onButtonPress(final InventoryClickEvent event) {
        final Player player = (Player) event.getWhoClicked();
        final Menu openMenu = MenuAPI.getOpenedMenus().get(player.getUniqueId());

        if (openMenu != null) {
            if (event.getSlot() != event.getRawSlot()) {
                if (event.getClick() == ClickType.SHIFT_LEFT || event.getClick() == ClickType.SHIFT_RIGHT) {
                    event.setCancelled(true);
                }

                return;
            }

            if (openMenu.getButtons().containsKey(event.getSlot())) {
                final Button button = openMenu.getButtons().get(event.getSlot());
                final boolean cancel = button.cancelEvent(player, event.getSlot(), event.getClick());

                event.setCancelled(cancel);
                button.onClick(player, event.getSlot(), event.getClick());

                if (MenuAPI.getOpenedMenus().containsKey(player.getUniqueId())) {
                    final Menu newMenu = MenuAPI.getOpenedMenus().get(player.getUniqueId());

                    if (newMenu == openMenu) {
                        final boolean buttonUpdate = button.updateOnClick(player, event.getSlot(), event.getClick());

                        if (buttonUpdate) {
                            player.updateInventory();
                        }
                    }
                }
            }
        }
    }
}