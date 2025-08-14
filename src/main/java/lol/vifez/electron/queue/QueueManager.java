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

    private final Map<String, Queue> queueMap;
    private final Map<UUID, Queue> playersQueue;

    public QueueManager() {
        this.queueMap = new ConcurrentHashMap<>();
        this.playersQueue = new HashMap<>();

        for (Kit kit : Practice.getInstance().getKitManager().getKits().values()) {
            queueMap.put(kit.getName(), new Queue(Practice.getInstance(), kit));

            if (kit.isRanked()) {
                queueMap.put("ranked_" + kit.getName(), new Queue(Practice.getInstance(), kit));
            }
        }

        new QueueTask(this).runTaskTimerAsynchronously(Practice.getInstance(), 20L, 20L);
        new ActionBarTask(Practice.getInstance()).runTaskTimer(Practice.getInstance(), 0L, 20L);
        new QueueTask(this).runTaskTimerAsynchronously(Practice.getInstance(), 20L, 20L);
    }

    public Queue getQueue(Kit kit, boolean ranked) {
        return queueMap.get((ranked ? "ranked_" : "") + kit.getName());
    }

    public Queue getQueue(UUID uuid) {
        return queueMap.get(Practice.getInstance().getProfileManager().getProfile(uuid).getCurrentQueue());
    }

    public int getAllQueueSize() {
        int size = 0;

        for (Queue ignored : queueMap.values()) {
            size += playersQueue.size();
        }

        return size;
    }
}