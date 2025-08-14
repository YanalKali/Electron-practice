package lol.vifez.electron.util.menu.button.impl;

import lol.vifez.electron.util.menu.button.Button;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
@Getter
@Setter
public class DisplayButton extends Button {

    private ItemStack itemStack;
    private boolean cancel;

    public DisplayButton(ItemStack stack) {
        this.itemStack = stack;
        this.cancel = true;
    }

    @Override
    public ItemStack getItem(Player player) {
        if (this.itemStack == null) {
            return new ItemStack(Material.AIR);
        } else {
            return this.itemStack;
        }
    }

    @Override
    public boolean cancelEvent(Player player, int slot, ClickType type) {
        return this.cancel;
    }
}
