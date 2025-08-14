package lol.vifez.electron.kit;

import lol.vifez.electron.Practice;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/*
 * Copyright (c) 2025 Vifez. All rights reserved.
 * Unauthorized use or distribution is prohibited.
 */

@Getter
public class KitManager {

    private final Practice instance;
    private final Map<String, Kit> kits;

    public KitManager(Practice instance) {
        this.instance = instance;
        this.kits = new ConcurrentHashMap<>();

        ConfigurationSection section = instance.getKitsFile().getConfiguration().getConfigurationSection("kits");

        if (section != null) {
            section.getKeys(false).forEach(key -> kits.put(key.toLowerCase(), instance.getGson().fromJson(section.getString(key), Kit.class)));
        }
    }

    public Kit getKit(String name) {
        return kits.get(name.toLowerCase());
    }

    public void save(Kit kit) {
        kits.putIfAbsent(kit.getName().toLowerCase(), kit);
    }

    public void delete(Kit kit) {
        kits.remove(kit.getName().toLowerCase());

        instance.getKitsFile().getConfiguration().set("kits." + kit.getName().toLowerCase(), null);
        instance.getKitsFile().save();
    }

    public void close() {
        kits.values().forEach(kit -> instance.getKitsFile().getConfiguration().set("kits." + kit.getName().toLowerCase(), instance.getGson().toJson(kit)));
        instance.getKitsFile().save();
    }
}
