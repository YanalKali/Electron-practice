package lol.vifez.electron.util.nametag.adapter;

import org.bukkit.entity.Player;

public interface NameTagAdapter {
    String getPrefix(Player player);
    String getSuffix(Player player);
    default void setViewer(Player player, Player viewer) {}
    default Player getViewer(Player player) {
        return null;
    }
}