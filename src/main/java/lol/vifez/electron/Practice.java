package lol.vifez.electron;

import co.aikar.commands.BukkitCommandManager;
import com.github.retrooper.packetevents.PacketEvents;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.LongSerializationPolicy;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import io.github.thatkawaiisam.assemble.Assemble;
import lol.vifez.electron.arena.commands.ArenaCommand;
import lol.vifez.electron.arena.commands.ArenasCommand;
import lol.vifez.electron.arena.manager.ArenaManager;
import lol.vifez.electron.chat.MessageCommand;
import lol.vifez.electron.chat.ReplyCommand;
import lol.vifez.electron.commands.admin.*;
import lol.vifez.electron.commands.staff.MoreCommand;
import lol.vifez.electron.kit.KitManager;
import lol.vifez.electron.kit.commands.KitCommands;
import lol.vifez.electron.kit.commands.KitEditorCommand;
import lol.vifez.electron.leaderboard.Leaderboard;
import lol.vifez.electron.leaderboard.command.LeaderboardCommand;
import lol.vifez.electron.listener.MatchListener;
import lol.vifez.electron.listener.SpawnListener;
import lol.vifez.electron.match.MatchManager;
import lol.vifez.electron.mongo.MongoAPI;
import lol.vifez.electron.mongo.MongoCredentials;
import lol.vifez.electron.placeholderapi.ElectronPlaceholders;
import lol.vifez.electron.profile.ProfileManager;
import lol.vifez.electron.profile.command.ProfileCommand;
import lol.vifez.electron.profile.repository.ProfileRepository;
import lol.vifez.electron.queue.QueueManager;
import lol.vifez.electron.scoreboard.PracticeScoreboard;
import lol.vifez.electron.tab.ElectronTab;
import lol.vifez.electron.util.*;
import lol.vifez.electron.util.adapter.ItemStackArrayTypeAdapter;
import lol.vifez.electron.util.menu.MenuAPI;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.refinedev.api.skin.SkinAPI;
import xyz.refinedev.api.tablist.TablistHandler;

import java.io.File;

public final class Practice extends JavaPlugin {

    @Getter private ConfigFile arenasFile;
    @Getter private ConfigFile kitsFile;
    @Getter private ConfigFile tabFile;

    @Getter private MongoAPI mongoAPI;
    @Getter private Gson gson;
    @Getter private Location spawnLocation;
    @Getter private FileConfiguration languageConfig;

    @Getter private ProfileManager profileManager;
    @Getter private ArenaManager arenaManager;
    @Getter private KitManager kitManager;
    @Getter private MatchManager matchManager;
    @Getter private QueueManager queueManager;
    @Getter private Leaderboard leaderboards;

    @Override
    public void onLoad() {
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
        PacketEvents.getAPI().init();
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        languageConfig = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "language.yml"));

        PacketEvents.getAPI().load();

        sendTitle();
        initConfig();
        initLibraries();
        initModels();
        registerCommands();
        initDesign();
        initListeners();
    }

    private void initListeners() {
        new SpawnListener(this);
        new MatchListener(this);
        new MenuAPI(this);
    }

    private void registerCommands() {
        BukkitCommandManager manager = new BukkitCommandManager(this);
        manager.registerCommand(new ArenaCommand(this, arenaManager));
        manager.registerCommand(new ArenasCommand(this));
        manager.registerCommand(new ElectronCommand(this));
        manager.registerCommand(new KitCommands(this));
        manager.registerCommand(new ProfileCommand(profileManager));
        manager.registerCommand(new BuildModeCommand());
        manager.registerCommand(new KitEditorCommand(this));
        manager.registerCommand(new LeaderboardCommand());
        manager.registerCommand(new MessageCommand());
        manager.registerCommand(new ReplyCommand());
        manager.registerCommand(new MoreCommand());
        manager.registerCommand(new EloCommand());
    }

    private void sendTitle() {
        sendMsg(" ");
        sendMsg("&bElectron Practice &7[Beta]");
        sendMsg(" ");
        sendMsg(translateMsg("Version", getDescription().getVersion()));
        sendMsg(translateMsg("Protocol", getServer().getBukkitVersion()));
        sendMsg(translateMsg("Spigot", getServer().getName()));
        sendMsg(" ");
        sendMsg(translateMsg("Author", String.join(", ", getDescription().getAuthors())));
        sendMsg(" ");
    }

    private void sendMsg(String msg) {
        Bukkit.getConsoleSender().sendMessage(CC.translate(msg));
    }

    private String translateMsg(String info, String detail) {
        return " &7> &f" + info + ": &b" + detail;
    }

    private void initConfig() {
        arenasFile = new ConfigFile(this, "arenas.yml");
        kitsFile = new ConfigFile(this, "kits.yml");
        tabFile = new ConfigFile(this, "tab.yml");

        if (!tabFile.getConfiguration().contains("enabled")) {
            sendMsg("&c[ERROR] tab.yml is missing essential data!");
        } else {
            sendMsg("&aSuccessfully loaded tab.yml!");
        }
    }

    private void initLibraries() {
        gson = new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .setLongSerializationPolicy(LongSerializationPolicy.STRING)
                .registerTypeAdapter(ItemStack[].class, new ItemStackArrayTypeAdapter())
                .create();

        mongoAPI = new MongoAPI(new MongoCredentials(
                getConfig().getString("mongo.host"),
                getConfig().getInt("mongo.port"),
                getConfig().getString("mongo.database"),
                getConfig().getString("mongo.user"),
                getConfig().getString("mongo.password"))
        );

        spawnLocation = SerializationUtil.deserializeLocation(getConfig().getString("settings.spawn-location", "world,0,100,0,0,0"));

        if (getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new ElectronPlaceholders(this).register();
        }
    }

    private void initModels() {
        profileManager = new ProfileManager(new ProfileRepository(mongoAPI, gson));
        arenaManager = new ArenaManager(this);
        kitManager = new KitManager(this);
        matchManager = new MatchManager(this);
        queueManager = new QueueManager(this);
        leaderboards = new Leaderboard(profileManager);
    }

    private void initDesign() {
        if (getConfig().getBoolean("scoreboard.enabled")) {
            new Assemble(this, new PracticeScoreboard(this));
        }

        if (tabFile.getBoolean("enabled")) {
            TablistHandler tablistHandler = new TablistHandler(this);
            SkinAPI skinAPI = new SkinAPI(this, gson);

            tablistHandler.setIgnore1_7(false);
            tablistHandler.setupSkinCache(skinAPI);
            tablistHandler.init(PacketEvents.getAPI());
            tablistHandler.registerAdapter(new ElectronTab(this, getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")), 20);
        }
    }

    @Override
    public void onDisable() {
        if (profileManager != null) profileManager.close();
        if (arenaManager != null) arenaManager.close();
        if (kitManager != null) kitManager.close();

        PacketEvents.getAPI().terminate();
    }

    public static Practice get() {
        return getPlugin(Practice.class);
    }
}