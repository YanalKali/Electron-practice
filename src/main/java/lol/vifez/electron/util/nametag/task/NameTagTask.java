package lol.vifez.electron.util.nametag.task;

import lol.vifez.electron.util.nametag.NameTagAPI;
import lol.vifez.electron.util.nametag.adapter.NameTagAdapter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

@RequiredArgsConstructor
public class NameTagTask extends BukkitRunnable {

    private final NameTagAPI api;
    private final NameTagAdapter adapter;

    @Override
    public void run() {
        for (Player player : api.getPlugin().getServer().getOnlinePlayers()) {
            String prefix = adapter.getPrefix(player);
            String suffix = adapter.getSuffix(player);

            for (Player online : api.getPlugin().getServer().getOnlinePlayers()) {
                api.getHandler().setNameTag(online, player, prefix, suffix);
            }
        }
    }
}
