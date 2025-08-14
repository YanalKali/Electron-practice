package lol.vifez.electron.util.nametag;

import lol.vifez.electron.util.nametag.adapter.NameTagAdapter;
import lol.vifez.electron.util.nametag.listener.NameTagListener;
import lol.vifez.electron.util.nametag.task.NameTagTask;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;

@Getter @Setter
public final class NameTagAPI {

    private static NameTagAPI aria;

    private final JavaPlugin plugin;
    private final NameTagAdapter adapter;
    private final NameTagHandler handler;
    private final Scoreboard scoreboard;

    public NameTagAPI(JavaPlugin plugin, NameTagAdapter adapter, Scoreboard scoreboard, long delay) {
        aria = this;

        this.plugin = plugin;
        this.adapter = adapter;
        this.handler = new NameTagHandler(aria);
        this.scoreboard = scoreboard;

        new NameTagTask(this, adapter).runTaskTimerAsynchronously(plugin, 0, delay < 0 ? 0 : delay);
        plugin.getServer().getPluginManager().registerEvents(new NameTagListener(this), plugin);
    }

    public static NameTagAPI getInstance() {
        if(aria == null) {
            throw new UnsupportedOperationException("Aria instance is null");
        }

        return aria;
    }
}