package lol.vifez.electron.queue;

import lol.vifez.electron.Practice;
import lol.vifez.electron.kit.Kit;
import lol.vifez.electron.queue.listener.QueueListener;
import lol.vifez.electron.queue.task.ActionBarTask;
import lol.vifez.electron.queue.task.QueueTask;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/*
 * Copyright (c) 2025 Vifez. All rights reserved.
 * Unauthorized use or distribution is prohibited.
 */

@Getter
public class QueueManager {

    private final Practice instance;
    private final Map<String, Queue> queueMap;
    private final Map<UUID, Queue> playersQueue;

    public QueueManager(Practice instance) {
        this.instance = instance;
        this.queueMap = new ConcurrentHashMap<>();
        this.playersQueue = new HashMap<>();

        for (Kit kit : instance.getKitManager().getKits().values()) {
            queueMap.put(kit.getName(), new Queue(instance, kit));

            if (kit.isRanked()) {
                queueMap.put("ranked_" + kit.getName(), new Queue(instance, kit));
            }
        }

        new QueueTask(this).runTaskTimerAsynchronously(instance, 20L, 20L);
        new ActionBarTask(instance).runTaskTimer(instance, 0L, 20L);
        new QueueTask(this).runTaskTimerAsynchronously(instance, 20L, 20L);
        new QueueListener(instance);
    }

    public Queue getQueue(Kit kit, boolean ranked) {
        return queueMap.get((ranked ? "ranked_" : "") + kit.getName());
    }

    public Queue getQueue(UUID uuid) {
        return queueMap.get(instance.getProfileManager().getProfile(uuid).getCurrentQueue());
    }

    public int getAllQueueSize() {
        int i = 0;

        for (Queue ignored : queueMap.values()) {
            i += playersQueue.size();
        }

        return i;
    }
}
